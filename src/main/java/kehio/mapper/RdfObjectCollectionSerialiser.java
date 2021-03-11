package kehio.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import kehio.annotations.done.RdfObject;
import kehio.annotations.done.RdfObjectCollection;

class RdfObjectCollectionSerialiser implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfObjectCollection.class);
	}


	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		String annotationValue = getAnnotationValue(field);
		Boolean isPath = getAnnotationIsPath(field);
		
		List<RDFNode> objectProperties = KehioUtils.retrieveFromRdfPath(model, subject, annotationValue, isPath);
		if(!objectProperties.isEmpty()) {
			Class<?> fieldType = field.getType();
			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) field.getGenericType();
				if(pt.getTypeName().contains("java.lang.String")) {
					Stream<String> streamOfProperties = objectProperties.stream().map(RDFNode::toString);
					initCollection(field, fieldType, object, streamOfProperties);
				}else {
					initObjectCollection(field, fieldType, object, objectProperties, model);
				}
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfObjectProperties can be used only with Collections. Please review annotation in attribute ", field.getName()));
			}
		}

		return annotationValue;
	 }	   
	
	
	private void initCollection(Field field, Class<?> fieldType, Object object, Stream<?> streamOfProperties) throws IllegalArgumentException, IllegalAccessException {
		if(fieldType.equals(Collection.class) || fieldType.equals(List.class)) {
			field.set(object, streamOfProperties.collect(Collectors.toList()));
		}else if(fieldType.equals(Set.class)) {
			field.set(object, streamOfProperties.collect(Collectors.toSet()));
		}else{ // TODO: extend here with more else if the type of data structures supported
			String illegalMessage = KehioUtils.concatStrings("@RdfObjectProperties can be used only with Collections, List, and Set. Please create an issue to add more java data structures.");
			throw new IllegalArgumentException(illegalMessage);
		}
	}
	

	private void initObjectCollection(Field field, Class<?> fieldType, Object object, List<RDFNode> objectProperties, Model model) throws IllegalArgumentException, IllegalAccessException {
		// Extract collection type
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		String typeName = pt.getTypeName();
		typeName = typeName.substring(typeName.indexOf(KehioUtils.START_CLASS_TOKEN)+1, typeName.indexOf(KehioUtils.END_CLASS_TOKEN));
		try {
			Class<?> clazzFull = Class.forName(typeName);
			List<Object> instantiatedNestedObjects = new ArrayList<>();
			for(int index=0; index < objectProperties.size(); index++) {
				RDFNode node = objectProperties.get(index);
				try {
					if(node.isResource()) {
						Object serialisedObject = Kehio.serializeClass(clazzFull, model, node.asResource());
						instantiatedNestedObjects.add(serialisedObject);
					}else {
						throw new IllegalArgumentException("Throw exception in @RDFObjectCollectionSerialiser");
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			initCollection(field, fieldType, object, instantiatedNestedObjects.stream());
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
			
	}

	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
	
	}
	
	
	
	
	
	 private String getAnnotationValue(Field field) {
		 String annotationValue = field.getAnnotation(RdfObjectCollection.class).value();
		 if (annotationValue==null || annotationValue.isEmpty()) { 
			 String illegalMessage = KehioUtils.concatStrings("@RdfObjectProperties must be initialised with a value, please review annotation for attribute ", field.getName());
			 throw new IllegalArgumentException(illegalMessage);
		 }
		 return annotationValue;
	 }

	 private boolean getAnnotationIsPath(Field field) {
	 	 return field.getAnnotation(RdfObjectCollection.class).isPath();
	 }

}
