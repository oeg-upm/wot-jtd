package kehio.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import kehio.annotations.done.RdfObjectContainer;

class RdfObjectContainerSerialiser implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfObjectContainer.class);
	}

	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		String annotationValue = getAnnotationValue(field);
		String annotationKey = getAnnotationKey(field);
 	 	boolean isPath = getAnnotationIsPath(field);
 	 	
 	 	if(annotationValue==null || annotationValue.isEmpty() || annotationKey==null || annotationKey.isEmpty())
 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectContainer MUST have 'value' and 'key' attributes instantiated, please review the annotation for attribute ", field.getName()));
 	 	
 	 	List<RDFNode> objectProperties = KehioUtils.retrieveFromRdfPath(model, subject, annotationValue, isPath);
 	 	if(!objectProperties.isEmpty()) {
 	 		Map<String,Object> instantiation = new HashMap<>();
 	 		for(int index=0; index < objectProperties.size(); index++) {
 	 			try {
	 	 			RDFNode node = objectProperties.get(index);
	 	 			Object nestedObject = instantiateClassField(field, node.asResource() , model);
	 	 			String key = instantiateKeyProperty(model, annotationKey, node.asResource(), isPath, field);
	 	 			if(key!=null && nestedObject!=null) {
	 	 				instantiation.put(key, nestedObject);
	 	 			}else {
	 	 				System.out.println(key+" - "+nestedObject);
	 	 			} 
 	 			}catch(Exception e) {
 	 				
 	 			}
 	 		}
 	 		field.set(object, instantiation);
 	 	}
		return annotationValue;
	 }	   
	
	private String instantiateKeyProperty(Model model, String keyProperty, Resource objectProperty, boolean isPath, Field field) throws IllegalArgumentException, IllegalAccessException {
		String key = null;
		List<RDFNode> objectProperties = KehioUtils.retrieveFromRdfPath(model, objectProperty, keyProperty, isPath);
		if(objectProperties.size()>1) {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectContainer must point to a unique literal through they 'key' property, more than one were retrieved. please review annotation for attribute ", field.getName()));
		}else if(objectProperties.isEmpty()) {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectContainer must point to a unique literal through they 'key' property, zero were retrieved. please review annotation for attribute ", field.getName()));
		}else {
			RDFNode node = objectProperties.get(0);
			if(node.isLiteral()) {
				key = node.asLiteral().getString();
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectContainer must point to a unique literal through they 'key' property, a non-literal was retrieved. please review annotation for attribute ", field.getName()));
			}
		}
		return key;
	}

	private Object instantiateClassField(Field field, Resource objectProperty, Model model) {
		Object nestedObject = null;
		try {
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			String typeName = pt.getTypeName();
			
			typeName = typeName.substring(typeName.indexOf(KehioUtils.START_SECOND_CLASS_TOKEN)+2, typeName.indexOf(KehioUtils.END_CLASS_TOKEN));
			Class<?> clazzFull = Class.forName(typeName);
			nestedObject = Kehio.serializeClass(clazzFull, model, objectProperty.asResource());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return nestedObject;
	}
	
	
 
	
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
	
	}
	 
	 
 
	 
	 private String getAnnotationValue(Field field) {
		 String annotationValue = field.getAnnotation(RdfObjectContainer.class).value();
		 if (annotationValue==null || annotationValue.isEmpty()) { 
			 String illegalMessage = KehioUtils.concatStrings("@RdfObjectContainer must be initialised with a value, please review annotation for attribute ", field.getName());
			 throw new IllegalArgumentException(illegalMessage);
		 }
		 return annotationValue;
	 }
	 
	 private String getAnnotationKey(Field field) {
	 	 return field.getAnnotation(RdfObjectContainer.class).key();
	 }
	 private boolean getAnnotationIsPath(Field field) {
	 	 return field.getAnnotation(RdfObjectContainer.class).isPath();
	 }


}
