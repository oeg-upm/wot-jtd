package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import kehio.annotations.RdfPropertiesContainer;
import kehio.annotations.RdfUrlMap;


class RdfPropertiesContainerMapper implements RdfMapper{

	protected Set<Property> forbidenRdfProperties;
	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfPropertiesContainer.class);
	}

	public void setPropertiesNotToContain(Set<Property> forbidenRdfProperties) {
		this.forbidenRdfProperties = forbidenRdfProperties;
	}
	
	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		PropertiesContainerAnotation annotation = new PropertiesContainerAnotation(field);
		Map<String, String> collectionOfTriples = populateMapFromRDF(annotation, model, subject);
		
		field.set(object, collectionOfTriples);
		
		return null;
	}
	
	

	private Map<String, String> populateMapFromRDF(PropertiesContainerAnotation annotation , Model model, Resource subject) {
		Set<Property> ignore = annotation.getPropertiesToIgnore();
		Map<String, String> collectionOfTriples = new HashMap<>();
		StmtIterator stIterator = model.listStatements(subject, null, (RDFNode) null);
		while(stIterator.hasNext()) {
			Statement st = stIterator.next();
			if(!forbidenRdfProperties.contains(st.getPredicate()) && !ignore.contains(st.getPredicate())){
				String shortenPredicate = shortenURI(model, st.getPredicate().toString());
				String shortenObject = st.getObject().toString();
				if(st.getObject().isResource())
					shortenObject =  shortenURI(model, shortenObject);
				collectionOfTriples.put(shortenPredicate, shortenObject);
			}
		}
		return collectionOfTriples;
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
		Map<String, String> values = (Map<String, String>) annotation.getField().get(object);
		for(Entry<String, String> entry : values.entrySet()) {
			try {
				Property rdfProperty = computeFullProperty(entry.getKey(), model.getNsPrefixMap());
				populateModel(model, subject, rdfProperty, entry.getValue());
			}catch(Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
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
	
	private void populateModel(Model model, Resource subject, Property rdfProperty, String objectValue) {
		
			try {
				RDFNode objectNode = model.createLiteral(objectValue);
				model.add(subject, rdfProperty, objectNode);
			}catch (Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		
	}
	
 

	
	class PropertiesContainerAnotation{

		private Field field;
		
		public PropertiesContainerAnotation(Field field) {
			this.field = field;
			checkAnnotation();
		}
		
		private void checkAnnotation() {
			if(!field.getType().equals(Map.class))
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfContainer must be used only with a Map attribute. Please review annotation in field ", field.getName()));
			Entry<String,String> mapTypes = KehioUtils.extractMapTypes(field);
			if(!mapTypes.getKey().contains(KehioUtils.STRING_CLASS))
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfContainer must be used only with a Map that has as key type a String. Please review annotation in field ", field.getName()));			
	
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
		 	 return field.getAnnotation(RdfPropertiesContainer.class).onlyDatatypes();
		 }
		 
		 public boolean onlyObjects() {
		 	 return field.getAnnotation(RdfPropertiesContainer.class).onlyObjects();
		 }*/

		 public Field getField() {
			return field;
		}
		 
		 public Set<Property> getPropertiesToIgnore() {
			 Set<Property> ignore = new HashSet<>();
			 String[] ignorable = field.getAnnotation(RdfPropertiesContainer.class).ignore();
			 for(int index=0; index < ignorable.length; index++) {
				 ignore.add(ResourceFactory.createProperty(ignorable[index]));
			 }
			 return ignore;
		 }
		 
	
			 
	}
	
}
