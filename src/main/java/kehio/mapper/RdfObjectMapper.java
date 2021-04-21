package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import kehio.annotations.RdfObject;
import kehio.annotations.RdfUrlMap;

class RdfObjectMapper implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfObject.class);
	}

	
	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException, URISyntaxException  {
		field.setAccessible(true);
		
		ObjectAnnotation annotation = new ObjectAnnotation(field);
		
		if(KehioUtils.isCollection(field)) {
 	 		// Collection case
 	 		Collection<Object> instantiatedCollection = initialiseObjectCollection(annotation, subject, model);
 	 		KehioUtils.instantiateCollection(field, instantiatedCollection, object);
		} else {
 	 		// Class, String, or URI case
			Object instantiatedField = serialiseUnitary(annotation, subject, model);
			KehioUtils.instantiateField(field, object, instantiatedField);
 	 	}
 	 	return annotation.getProperty();
	 }
	
	
	private Object serialiseUnitary(ObjectAnnotation annotation, Resource subject, Model model)   {
		Object instantiatedObject = null;
		List<RDFNode> objectProperties = KehioUtils.objectNodes(model, subject, annotation.getProperty());
		if(objectProperties.size()==1) {
			RDFNode objectProperty = objectProperties.get(0);
			if(isResource(objectProperty)) {
				Field field = annotation.getField();
				if(KehioUtils.isFieldString(field)) {
					instantiatedObject = serialiseStringField(objectProperty, annotation);
				}else if(KehioUtils.isFieldURI(field)) {
					instantiatedObject = serialiseURIField(objectProperty);
				} else {
					instantiatedObject = serialiseClassField(field.getType().getCanonicalName(), objectProperty, model);
				}
			}
		}

		return instantiatedObject;
	}
	
	private String serialiseStringField(RDFNode objectProperty, ObjectAnnotation annotation) {
		String instantiatedObject = null;
		String base = annotation.getBase();
		if(objectProperty.asNode().isBlank()){
			instantiatedObject = objectProperty.asNode().getBlankNodeLabel().replace(base, "");
		}else if( objectProperty.asNode().isURI()) {
			instantiatedObject = objectProperty.toString();
			instantiatedObject = instantiatedObject.replace(base, "");
		}
		// Change URL for alias value
		if(instantiatedObject!=null) {
			String instantiatedObjectAlias = annotation.getAliasKeyInstantiated(instantiatedObject);
			if(instantiatedObjectAlias!=null)
				instantiatedObject = instantiatedObjectAlias;
			if(annotation.getStrict() && instantiatedObjectAlias==null)
				instantiatedObject=null;
		}
		
		return instantiatedObject;
	}
	
	private URI serialiseURIField(RDFNode objectProperty) {
		URI instantiatedObject = null;
		try {
			if(objectProperty.asNode().isBlank()){
				instantiatedObject = new URI(objectProperty.asNode().getBlankNodeLabel());
			}else if( objectProperty.asNode().isURI()) {
				instantiatedObject = new URI(objectProperty.toString());
			}
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		return instantiatedObject;
	}
	
	private Object serialiseClassField(String className, RDFNode objectProperty, Model model) {
		Object instantiatedObject = null;
		try {
			Class<?> clazzFull = Class.forName(className);
			instantiatedObject = Kehio.serializeClass(clazzFull, model, objectProperty.asResource());
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		return instantiatedObject;
	}
	
	private boolean isResource(RDFNode objectProperty) {
		return objectProperty.asNode().isBlank() || objectProperty.asNode().isURI();
	}
 
	private Collection<Object> initialiseObjectCollection(ObjectAnnotation annotation, Resource subject, Model model) throws IllegalAccessException {
		Collection<Object> collection = new ArrayList<>();
		List<RDFNode> objectProperties = KehioUtils.objectNodes(model, subject, annotation.getProperty());
		for(int index=0; index < objectProperties.size(); index++) {
			RDFNode node = objectProperties.get(index);
			if(node.isResource()) {
				try {
					String subtype = KehioUtils.extractCollectionType(annotation.getField());
					Object instantiatedObject = null;
					if(subtype.equals(KehioUtils.STRING_CLASS)) {
						instantiatedObject = serialiseStringField(node, annotation) ;
					}else if( subtype.equals(KehioUtils.URI_CLASS)) {
						instantiatedObject = serialiseURIField( node) ;
					}else {
						instantiatedObject= serialiseClassField( subtype,  node,  model);
					}
					if(instantiatedObject!=null)
						collection.add(instantiatedObject);
				}catch(Exception e) {
					throw new IllegalArgumentException(e.toString());
				}
			}else {
				String illegalMessage = KehioUtils.concatStrings("@RdfObject must be used for properties which range in the RDF are a blank node or a URI (String or java URI) and/or a Class");
				throw new IllegalArgumentException(illegalMessage);
			}
		}
		return collection;
	}
	
	// -- From object to to RDF methods
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		ObjectAnnotation annotation = new ObjectAnnotation(field);

		Object instantiatedField = KehioUtils.instantiatedField(field, object);
		if(instantiatedField!=null) {
	 	 	if(KehioUtils.isFieldClass(field) && !KehioUtils.isFieldURI(field)) {
	 	 		// Class case
	 	 		deserialiseClass(instantiatedField, annotation, model, subject);
	 	 	}else if(KehioUtils.isCollection(field) ) {
	 	 		// Collection case
	 	 		deserialiseClassCollection(instantiatedField, annotation, model, subject);
	 	 	}else if(KehioUtils.isFieldString(field) || KehioUtils.isFieldURI(field)) {
	 	 		// String or URI case
	 	 		deserialiseStringOrURI(instantiatedField.toString(), annotation, model, subject);
	 	 	}else {
	 			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject must be used with a non-wrapping primitive or a Collection of non-wrapping primitives, except with String or URI which are also allowed. Please review annotation for attribute ",field.getName()));
	 	 	}
		}
	}
	
	private void deserialiseStringOrURI(String instantiatedField, ObjectAnnotation annotation, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException {
		String objectProperty = annotation.getBase()+annotation.getGroupValueInstantiated(instantiatedField);

		if(KehioUtils.isValidResource(objectProperty)) {
			model.add(subject, annotation.getProperty(), ResourceFactory.createResource(objectProperty));
		}else {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject used with a String or a URI was resolved incorrectly as ", objectProperty,". Please review annotation for attribute ",annotation.getField().getName()));
		}
	}


	private void deserialiseClass(Object instantiatedField, ObjectAnnotation annotation, Model model, Resource subject) {
		 try {
			 Entry<Resource,Model> nested = Kehio.deserializeClassExtended(instantiatedField, model.getNsPrefixMap());
			 Resource nestedSubject = nested.getKey();
			 if(nestedSubject!=null && !nested.getValue().isEmpty()) {
				 model.add(nested.getValue());
				 model.add(subject, annotation.getProperty(), nestedSubject);
			 }//else {
			//	 throw new IllegalArgumentException(KehioUtils.concatStrings("An exception occured while processing annotation @RdfObject with value, ",annotation.getProperty().toString(),". Please review attribute ",annotation.getField().getName()));
			//}
		 }catch(Exception e) {
			 throw new IllegalArgumentException(e.toString());
		 }
	}

	@SuppressWarnings("unchecked")
	private void deserialiseClassCollection(Object instantiatedField, ObjectAnnotation annotation, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException {
		Collection<Object> nestedObjects = (Collection<Object>) instantiatedField;
		Iterator<Object> nestedObjectsIterator = nestedObjects.iterator();
		while(nestedObjectsIterator.hasNext()) {
				Object nestedObject = nestedObjectsIterator.next();
				if(nestedObject instanceof String || nestedObject instanceof URI) {
					deserialiseStringOrURI(nestedObject.toString(), annotation,  model,  subject);
				}else if(!isNotProcessable(nestedObject)) {
					deserialiseClass(nestedObject, annotation, model, subject);
				}else {
					throw new IllegalArgumentException("@RdfObject error, the annotation was used with a collection of wrapping primitive that is not String. Instead, use a collection of String, or URI, or a Class. Please review field");
				}
		}
	}
	 
	private boolean isNotProcessable(Object nestedObject) {
		return nestedObject instanceof Long || nestedObject instanceof Float || nestedObject instanceof Double || nestedObject instanceof Character || nestedObject instanceof Boolean || nestedObject instanceof Integer || nestedObject instanceof Short || nestedObject instanceof Byte;
	}
	
	// -- Annotation values methods


	class ObjectAnnotation{
		
		private Field field;
		
		public ObjectAnnotation(Field field) {
			this.field = field;
			checkout();
		}
		
		private void checkout() {
			if (getValue()==null || getValue().isEmpty())
				 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject must be initialised with a value. Please review annotation for attribute ", field.getName()));
			String value = getValue();
			if(!KehioUtils.isURI(value))
				 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject must be initialised with a value that is a valid URI. Please review annotation for attribute ", field.getName()));
			if(!KehioUtils.isFieldClass(field) & !KehioUtils.isFieldURI(field) & !KehioUtils.isFieldString(field) && !(KehioUtils.isCollection(field) && isCollectionCompatible()))
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject must be used with attributes that are a URI, a String, a Class or a Collection containing one of the previous. Please review attribute ", field.getName()));
			//if(!getBase().isEmpty() && !getMaps().isEmpty())
			//	throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObject can not be used with 'map' and 'base' at the same time. Please review attribute ", field.getName()));

		}
		
		private boolean isCollectionCompatible() {
			String collectionType = KehioUtils.extractCollectionType(field);
			return collectionType.equals(KehioUtils.STRING_CLASS) || !KehioUtils.isWrappingPrimitive(collectionType) ;
		}
		
		 private String getValue() {
			 return field.getAnnotation(RdfObject.class).value();
		 }
		 
		 private Property getProperty() {
			 return ResourceFactory.createProperty(field.getAnnotation(RdfObject.class).value());
		 }
			 
		 private String getBase() {
		 	 return field.getAnnotation(RdfObject.class).base();
		 }
		 
		 private boolean getStrict() {
		 	 return field.getAnnotation(RdfObject.class).strict();
		 }
		 	 
		 	 
		 private Field getField() {
			 return field;
		 }
		 
		 private Map<String,String> getAliases(){
			 Map<String,String> processedMap = new HashMap<>();
			 RdfUrlMap[] maps = field.getAnnotation(RdfObject.class).aliases();
			 if(maps!=null) {
				 for(int index=0; index < maps.length; index++) {
					 RdfUrlMap map = maps[index];
					 processedMap.put(map.key(), map.value());
				 }
			 }
			 return processedMap;
		 }
		 
		 private String getAliasKeyInstantiated(String key) {
			return getAliases().get(key);
		 }
	
		private String getGroupValueInstantiated(String simplifiedvalue) {
			Map<String,String> maps = getAliases();
			Iterator<Entry<String, String>> iterator = maps.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if(simplifiedvalue.equals(entry.getValue())) {
					simplifiedvalue = entry.getKey();
				}
			}
			 return simplifiedvalue;
		 }
	
		 
		
		
		 
	}
}