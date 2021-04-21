package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import kehio.annotations.RdfContainer;
import kehio.annotations.RdfUrlMap;


class RdfContainerMapper implements RdfMapper{

	protected Set<Property> forbidenRdfProperties;

	public void setPropertiesNotToContain(Set<Property> processedProperties) {
		this.forbidenRdfProperties = processedProperties;
	}
	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfContainer.class);
	}

	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		PropertiesContainerAnotation annotation = new PropertiesContainerAnotation(field);
		Map<String, Object> collectionOfTriples = populateMapFromRDF(annotation, model, subject, true);
		
		field.set(object, collectionOfTriples);
		
		return null;
	}
	
	

	private Map<String, Object> populateMapFromRDF(PropertiesContainerAnotation annotation, Model model, Resource subject, boolean firstCall) {
		Set<Property> ignore = annotation.getPropertiesToIgnore();
		Map<String, Object> collectionOfTriples = new HashMap<>();
		StmtIterator stIterator = model.listStatements(subject, null, (RDFNode) null);
		while(stIterator.hasNext()) {
			Statement st = stIterator.next();
		
			List<String> inverseAliases = annotation.inverseAlias(st.getPredicate());
			if( (!inverseAliases.isEmpty() && !firstCall) || (firstCall && !forbidenRdfProperties.contains(st.getPredicate()) && !ignore.contains(st.getPredicate()))){
				if(inverseAliases.isEmpty()) // if empty add current property, otherwise replaces current predicate with its shorten forms, i.e., inverse alias
					inverseAliases.add(st.getPredicate().toString());
				for(int index=0; index < inverseAliases.size(); index++) {
					String shortenPredicate = inverseAliases.get(index);
					shortenPredicate = shortenURI(model, shortenPredicate);
					RDFNode object = st.getObject();
					if(object.isResource()) {
						updateCollectionOfTriplesWithResources(annotation, model, collectionOfTriples, shortenPredicate,  object.asResource());
					}else {
						updateCollectionOfTriplesWithLiterals(collectionOfTriples, shortenPredicate, object.asLiteral().getValue());
					}
				}
			}
		}
		// add suject as property if specified in annotation
		if(annotation.getIdentifiers()!=null && !annotation.getIdentifiers().isEmpty())
			annotation.getIdentifiers().stream().forEach(id -> collectionOfTriples.put(id, subject.toString()));
			
		return collectionOfTriples;
	}
	
	
	private void updateCollectionOfTriplesWithResources(PropertiesContainerAnotation annotation, Model model, Map<String, Object> collectionOfTriples, String predicate, Resource object){
		
		if(!model.contains(object.asResource(), null, (RDFNode) null)) {
			// DONE: Si el objecto no existe en el modelo como sujeto : guardarlo como <string, url>
			String shortenObject =  shortenURI(model, object.toString());
			collectionOfTriples.put(predicate, shortenObject);
		}else {
			// DONE: Si el objecto es tambien sujeto en el modelo -> entonces se traduce como un Map<String,Object>) : llamada recurisva
			Map<String, Object> nestedResource = populateMapFromRDF(annotation, model, object, false);
			// DONE: Si el objecto es tambien sujeto en el modelo y ya existe en el mapa acutal : entonces traducirlos como una collection de Map<String,Object>
			updateCollectionOfTriplesWithLiterals(collectionOfTriples, predicate, nestedResource);
			
		}
	}
	
	/*
	 * Since objects here are literals we already know that the range of the property are always literals, and thus, as object it will be collection of strings or string
	 */
	@SuppressWarnings("unchecked")
	private void updateCollectionOfTriplesWithLiterals(Map<String, Object> collectionOfTriples, String predicate, Object object) {
		Object oldValue = collectionOfTriples.remove(predicate);
		if(oldValue!=null) { // already existed
			Collection<Object> newObjects = new ArrayList<>();
			if(oldValue instanceof Collection) {
				newObjects.addAll((Collection<Object>) oldValue);
			}else {
				newObjects.add(oldValue);
			}
			newObjects.add(object);
			collectionOfTriples.put(predicate, newObjects);
		}else {
			// new element
			collectionOfTriples.put(predicate, object);
		}
	}

	private String shortenURI(Model model, String uri) {
		String shortenUri = model.shortForm(uri);
		if(shortenUri==null)
			shortenUri = uri;
		return shortenUri;
	}
	
	// --
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		PropertiesContainerAnotation annotation = new PropertiesContainerAnotation(field);
		@SuppressWarnings("unchecked")
		Map<String, Object> values = (Map<String, Object>) annotation.getField().get(object);
		populateModel(annotation, model, subject, values.entrySet());
	}
	
	@SuppressWarnings("unchecked")
	private void populateModel(PropertiesContainerAnotation annotation, Model model, Resource subject, Set<Entry<String, Object>> entries) {

		for (Entry<String, Object> entry : entries) {
			Object value = entry.getValue();
			Property property = computeFullProperty(entry.getKey(), model.getNsPrefixMap());
			if (isCollection(value)) {
				Collection<Object> newProperties = (Collection<Object>) value;
				nestedElements(annotation, new ArrayList<>(newProperties), model, subject, property);
			} else if (isMap(value)) {
				resourceObjectToRDF(annotation, model, subject, property, value);
			} else if (!isCollection(value) && !isMap(value)) {
				keyValueToRDF(annotation, model, subject, property, value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void resourceObjectToRDF(PropertiesContainerAnotation annotation, Model model, Resource subject, Property property, Object value) {
		Resource newSubject = model.createResource();
		Map<String, Object> newEntries = (Map<String, Object>) value;
		Optional<String> propertyIdentifier = newEntries.keySet().stream().filter(keyProperty -> annotation.isPropertyIdentifier(keyProperty)).findFirst();
		if(propertyIdentifier.isPresent()) { // if a property in the json/object was set as identifier create a subject with such
			String newSubjectStr = newEntries.remove(propertyIdentifier.get()).toString();
			newSubject = model.createResource(newSubjectStr);
		}
		model.add(subject, property, newSubject);
		populateModel(annotation, model, newSubject, newEntries.entrySet());
	}

	private static Boolean isCollection(Object value) {
		return value instanceof Collection;
	}
	
	private static Boolean isMap(Object value) {
		return value instanceof Map;
	}
	
	@SuppressWarnings("unchecked")
	private void nestedElements(PropertiesContainerAnotation annotation, List<Object> elements, Model model, Resource subject, Property property ) {
		for(int index=0; index < elements.size(); index++) {
			Object value = elements.get(index);
			if(isCollection(value)) {
				Collection<Object> newProperties = (Collection<Object>) value;
				nestedElements(annotation, new ArrayList<>(newProperties), model, subject, property );
			}else if(isMap(value)) {
				resourceObjectToRDF(annotation, model, subject, property, value);
			}else if( !isMap(value) && !isCollection(value)) {
				keyValueToRDF( annotation,model,  subject,  property,  value);
			}
		}
	}
	
	private static void keyValueToRDF(PropertiesContainerAnotation annotation, Model model, Resource subject, Property property, Object value) {
		String url = model.expandPrefix(value.toString());

		if(!url.equals(value) && KehioUtils.isURI(url) ) {
			Property alias = annotation.getAlias(property.getURI());
			model.add(subject, alias, ResourceFactory.createResource(url));
		}else if(KehioUtils.isURI(value.toString())) {
			Property alias = annotation.getAlias(property.getURI());
			model.add(subject, alias, ResourceFactory.createResource(value.toString()));
		}else {
			model.add(subject, property, ResourceFactory.createTypedLiteral(value));
		}
	}

	
	private Property computeFullProperty(String property, Map<String,String> prefixes) {
		Property computedProperty = ResourceFactory.createProperty(property);
		int index = property.indexOf(':');
		if(index>-1) {
			String prefix = property.substring(0,index);
			String value = property.substring(index+1,property.length());
			String url = prefixes.get(prefix);
			if(url!=null)
				computedProperty = ResourceFactory.createProperty(url+value);
		}
		return computedProperty;
	}
	
	
	
 

	
	class PropertiesContainerAnotation{

		private Field field;
		
		public PropertiesContainerAnotation(Field field) {
			this.field = field;
			checkAnnotation();
		}
		
	

		public Property getAlias(String predicate) {
			Map<String, Property> aliases = this.getAliases();
			Property aliasProperty = aliases.get(predicate);
			if(aliasProperty==null)
				aliasProperty = ResourceFactory.createProperty(predicate);
			return aliasProperty;
		}

		private void checkAnnotation() {
			if(!field.getType().equals(Map.class))
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfBlankContainer must be used only with a Map attribute. Please review annotation in field ", field.getName()));
			Entry<String,String> mapTypes = KehioUtils.extractMapTypes(field);
			if(!mapTypes.getKey().contains(KehioUtils.STRING_CLASS))
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfBlankContainer must be used only with a Map that has as key type a String. Please review annotation in field ", field.getName()));			
	
			//if(!KehioUtils.extractMapValueType(field).toString().equals(Object.class.toString()))
			//	throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfContainer must be used only with a Map that has as value a java generic Object. Please review annotation in field ", field.getName()));

			/*
			if (onlyDatatypes() && onlyObjects()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfContainer can have only one flag marked as true, either 'onlyDatatypes' or 'onlyObjects'. Please review annotation for field ", field.getName()));
			
			if(onlyDatatypes() && (!mapTypes.getValue().equals(KehioUtils.STRING_CLASS) || )
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfContainer must be used only with a Map attribute. If 'onlyDatatypes' is true, then the values of the map must be String or a Collection of Strings. Please review annotation in field ", field.getName()));
			*/
		}
		
		 
		/* public boolean onlyDatatypes() {
		 	 return field.getAnnotation(RdfBlankContainer.class).onlyDatatypes();
		 }
		 
		 public boolean onlyObjects() {
		 	 return field.getAnnotation(RdfBlankContainer.class).onlyObjects();
		 }*/

		 public Field getField() {
			return field;
		}
	
		 public Set<Property> getPropertiesToIgnore() {
			 Set<Property> ignore = new HashSet<>();
				 String[] ignorable = field.getAnnotation(RdfContainer.class).ignore();
				 for(int index=0; index < ignorable.length; index++) {
					 ignore.add(ResourceFactory.createProperty(ignorable[index]));
				 }
			 return ignore;
		 }
		 
		 public Set<String> getIdentifiers(){
			 Set<String> identifiers = new HashSet<>();
			 String[] rawIdentifiers = field.getAnnotation(RdfContainer.class).identifiers();
			 for(int index=0; index < rawIdentifiers.length; index++) {
				 identifiers.add(rawIdentifiers[index]);
			 }
			 return identifiers;
		 }
		 
		 public Boolean isPropertyIdentifier(String property){
			 return getIdentifiers().contains(property);
		 }
		 
		 public Map<String, Property> getAliases(){
			 Map<String, Property> aliases = new HashMap<>();
			 RdfUrlMap[] rawAliases = field.getAnnotation(RdfContainer.class).aliases();
			 for(int index=0; index < rawAliases.length; index++) {
				 aliases.put(rawAliases[index].key(), ResourceFactory.createProperty(rawAliases[index].value()));
			 }
			 return aliases;
		 }
	
		public List<String> inverseAlias(Property predicate) {
			List<String> inverseAlias = new ArrayList<>();
			inverseAlias.addAll( getAliases().entrySet().stream().filter(entry -> entry.getValue().equals(predicate)).map(Entry::getKey).collect(Collectors.toList()));
			return inverseAlias;
		}
			 
	}







	
}
