package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import kehio.annotations.RdfObjectGroup;
import kehio.annotations.RdfUrlMap;

class RdfObjectGroupMapper implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfObjectGroup.class);
	}

	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		ObjectGroupAnnotation annotation = new ObjectGroupAnnotation(field);
 	 	List<RDFNode> objectProperties = KehioUtils.objectNodes(model, subject, annotation.getValue());
 	 	
 	 	Map<String,Object> instantiation = new HashMap<>();
 	 	for(int index=0; index < objectProperties.size(); index++) {
 	 		try {
	 	 		RDFNode node = objectProperties.get(index);
	 	 		Object nestedObject = instantiateClassField(field, node.asResource() , model);
	 	 		String key = instantiateKeyProperty(model, annotation.getGroupKey(), node.asResource(), field);
	 	 		if(key!=null && nestedObject!=null) {
					// Replace agrupation key for mapped value if exists
					key = annotation.getGroupKeyInstantiated(key);
	 	 			// Instantiate object
	 	 			instantiation.put(key, nestedObject);
	 	 		}
 	 		}catch(Exception e) {
 	 			throw new IllegalArgumentException(e.toString());
 	 		}
 	 	}
 	 	if(!instantiation.isEmpty())
 	 		field.set(object, instantiation);
 	 	return annotation.getProperty();
	 }	   
	
	private String instantiateKeyProperty(Model model, String keyProperty, Resource objectProperty, Field field) {
		String key = null;
		List<RDFNode> objectProperties = KehioUtils.objectNodes(model, objectProperty, keyProperty);
		if(objectProperties.size()>1) {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must point to a unique literal through they 'key' property, more than one were retrieved. please review annotation for attribute ", field.getName()));
		}else if(objectProperties.isEmpty()) {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must point to a unique literal through they 'key' property, zero were retrieved. please review annotation for attribute ", field.getName()));
		}else {
			RDFNode node = objectProperties.get(0);
			if(node.isLiteral()) {
				key = node.asLiteral().getString();
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must point to a unique literal through they 'key' property, a non-literal was retrieved. please review annotation for attribute ", field.getName()));
			}
		}
		return key;
	}

	private Object instantiateClassField(Field field, Resource objectProperty, Model model) {
		Object nestedObject = null;
		try {
			Class<?> clazzFull = KehioUtils.extractMapValueType(field);
			nestedObject = Kehio.serializeClass(clazzFull, model, objectProperty.asResource());
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		return nestedObject;
	}
	
	
	// ---
	
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		ObjectGroupAnnotation annotation = new ObjectGroupAnnotation(field);
		
		@SuppressWarnings("unchecked")
		Map<String,Object> values = (Map<String, Object>) field.get(object);
		
		Model nestedModel = buildObjectRdf(annotation, values, subject, model.getNsPrefixMap());
		model.add(nestedModel);
	}
	
	
	private Model buildObjectRdf(ObjectGroupAnnotation annotation, Map<String,Object> values, Resource subject, Map<String,String> prefixes) throws IllegalAccessException {
		Model nestedModel = ModelFactory.createDefaultModel();
		
		for(Entry<String, Object> entry : values.entrySet()) {
			try {
				Entry<Resource, Model> nestedObjects = Kehio.deserializeClassExtended(entry.getValue(), prefixes);
				nestedModel.add(subject, ResourceFactory.createProperty(annotation.getValue()), nestedObjects.getKey());
				nestedModel.add(nestedObjects.getValue());
				if(annotation.addKey()) {
					// Replace agrgupation key for mapped value if exists
					String key = annotation.getGroupKeyInstantiated(entry.getKey());
					// inject into RDF
					String groupKey = annotation.getGroupKey();
					nestedModel.add(nestedObjects.getKey(), ResourceFactory.createProperty(groupKey), key);
				}
			}catch(Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		}
		return nestedModel;
	}
	
	
	// ---
	 
	
	private class ObjectGroupAnnotation{
		 private Field field;
		 
		 public ObjectGroupAnnotation(Field field) {
			 this.field = field;
			 checkAnnotationRestrictions();
		 }
		 
		 private void checkAnnotationRestrictions() {
				String annotationValue = getValue();
				String anotationKey = getGroupKey();
				if (annotationValue==null || annotationValue.isEmpty())
					 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must be initialised with a value. Please review annotation for attribute ", field.getName()));
				if(anotationKey==null || anotationKey.isEmpty())
					 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must be initialised with a key that must be a datatype property. Please review annotation for attribute ", field.getName()));
				Entry<String,String> mapTypes = KehioUtils.extractMapTypes(field);
				if(!mapTypes.getKey().equals(KehioUtils.STRING_CLASS))
					 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must be used with a Map which keys are Strings. Please review annotation for attribute ", field.getName()));
				if(KehioUtils.isWrappingPrimitive(mapTypes.getValue()))
					 throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectGroup must be used with a Map which values are non-wrapping primitive objects. Please review annotation for attribute ", field.getName()));
			
			}
		 	 
			private String getValue() {
				 return field.getAnnotation(RdfObjectGroup.class).value();
			}
			
			private Property getProperty() {
				 return ResourceFactory.createProperty(field.getAnnotation(RdfObjectGroup.class).value());
			}
			 
			 private String getGroupKey() {
			 	 return field.getAnnotation(RdfObjectGroup.class).key();
			 }
			 
			 private Boolean addKey() {
			 	 return field.getAnnotation(RdfObjectGroup.class).includeKey();
			 }
			 
			 
			 private Map<String,String> getMaps(){
				 Map<String,String> processedMap = new HashMap<>();
				 RdfUrlMap[] maps = field.getAnnotation(RdfObjectGroup.class).aliases();
				 if(maps!=null) {
					 for(int index=0; index < maps.length; index++) {
						 RdfUrlMap map = maps[index];
						 processedMap.put(map.key(), map.value());
					 }
				 }
				 return processedMap;
			 }
			 
			 private String getGroupKeyInstantiated(String key) {
				 Map<String,String> maps = getMaps();
	 	 			if(maps.containsKey(key))
	 	 				key = maps.get(key);
	 	 		return key;
			 }
			 
	 }

}

