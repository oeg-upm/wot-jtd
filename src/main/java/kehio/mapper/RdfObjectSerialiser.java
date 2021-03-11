package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import kehio.annotations.done.RdfDatatypeCollection;
import kehio.annotations.done.RdfObject;

class RdfObjectSerialiser implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfObject.class);
	}

	
	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws  IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		
		String annotationValue = getAnnotationValue(field);
 	 	Boolean isPath = getAnnotationIsPath(field);
 	 	
 	 	List<RDFNode> objectProperties = KehioUtils.retrieveFromRdfPath(model, subject, annotationValue, isPath);
		if(objectProperties.size()==1) {
			intialiseObject(field, objectProperties.get(0), object, model);
		}else if(objectProperties.size()>1) {
			String illegalMessage = KehioUtils.concatStrings("@RdfObject is used with properties with a cardinality of 1, provided RDF contains multiple objects in the range of the property ", annotationValue, " for the subject ",subject.toString()," consider using @RdfObjectProperties.");
			throw new IllegalArgumentException(illegalMessage);
		}

		return annotationValue;
	 }	   
	
	private void intialiseObject(Field field, RDFNode objectProperty, Object object, Model model) throws IllegalAccessException, URISyntaxException {
		if(isURI(objectProperty)) {
			if(field.getType().equals(String.class)) {
				instantiateStringField(field, objectProperty, object);
			}else if(field.getType().equals(URI.class)) {
				instantiateURIField(field, objectProperty, object);
			} else {
				instantiateClassField(field, objectProperty, object, model);
			}
		}else {
			String illegalMessage = KehioUtils.concatStrings("@RdfObject must be used for properties which range in the RDF are a blank node or a URI");
			throw new IllegalArgumentException(illegalMessage);
		}
	}
	


	private void instantiateStringField(Field field, RDFNode objectProperty, Object object) throws IllegalAccessException {
		if(objectProperty.asNode().isBlank()){
			field.set(object, objectProperty.asNode().getBlankNodeLabel());
		}else if( objectProperty.asNode().isURI()) {
			field.set(object, objectProperty.toString());
		}else {
			// Nothing happens
		}
	}
	
	private void instantiateURIField(Field field, RDFNode objectProperty, Object object) throws IllegalAccessException, URISyntaxException {
		if(objectProperty.asNode().isBlank()){
			field.set(object, new URI(objectProperty.asNode().getBlankNodeLabel()));
		}else if( objectProperty.asNode().isURI()) {
			field.set(object, new URI(objectProperty.toString()));
		}else {
			// Nothing happens
		}
	}
	
	private void instantiateClassField(Field field, RDFNode objectProperty, Object object, Model model) {
		try {
			Class<?> clazzFull = Class.forName(field.getType().getCanonicalName());
			Object nestedObject = Kehio.serializeClass(clazzFull, model, objectProperty.asResource());
			field.set(object, nestedObject);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isURI(RDFNode objectProperty) {
		return objectProperty.asNode().isBlank() || objectProperty.asNode().isURI();
	}
 
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
	
	}
	
	 private String getAnnotationValue(Field field) {
		 String annotationValue = field.getAnnotation(RdfObject.class).value();
		 if (annotationValue==null || annotationValue.isEmpty()) { 
			 String illegalMessage = KehioUtils.concatStrings("@RdfObject must be initialised with a value, please review annotation for attribute ", field.getName());
			 throw new IllegalArgumentException(illegalMessage);
		 }
		 return annotationValue;
	 }
	 
	 private boolean getAnnotationIsPath(Field field) {
	 	 return field.getAnnotation(RdfObject.class).isPath();
	 }

	 
 
}
