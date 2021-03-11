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

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeContainer;

class RdfDatatypeContainerSerialiser implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfDatatypeContainer.class);
	}

	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		String annotationValue = getAnnotationValue(field);
		boolean byLang = getAnnotationByLang(field);
 	 	boolean byDatatype = getAnnotationByDatatype(field);
 	 	boolean isPath = getAnnotationIsPath(field);
 	 	
 	 	if(!(byLang || byDatatype))
 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeContainer MUST have 'byLang' or 'byDatatype' maked as True, please review the annotation for attribute ", field.getName()));
 	 	
 	 	List<RDFNode> objectProperties = KehioUtils.retrieveFromRdfPath(model, subject, annotationValue, isPath);
		if(!objectProperties.isEmpty()) {
			intialiseObject(field, object, objectProperties, byLang, byDatatype);
		}

		return annotationValue;
	 }	   
	
	private void intialiseObject(Field field, Object object, List<RDFNode> objectProperties, Boolean addLang, Boolean addDatatype) throws IllegalAccessException {
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			if(pt.getTypeName().contains("java.util.Map")) {
				initPrimitiveCollection(field, object, objectProperties, addLang, addDatatype);
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeContainer must be used with Map of Strings, i.e., Map<String,String>. Please review annotation in attribute ", field.getName()));
			}
		}else {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeContainer must be used with Map of Strings, i.e., Map<String,String>. Please review annotation in attribute ", field.getName()));
		}
	}
 
	private void initPrimitiveCollection(Field field, Object object, List<RDFNode> objectProperties, Boolean addLang, Boolean addDatatype) throws IllegalAccessException {
		Map<String,String> typedLiterals = new HashMap<>();
		if(addLang) {
			objectProperties.stream()
							.filter(RDFNode::isLiteral).filter(node-> !node.asLiteral().getLanguage().isEmpty())
							.forEach(node -> typedLiterals.put(node.asLiteral().getLanguage(), node.asLiteral().getValue().toString()));
		}else if(addDatatype) {
			objectProperties.stream()
			.filter(RDFNode::isLiteral)
			.forEach(node -> typedLiterals.put(node.asLiteral().getDatatypeURI(), node.asLiteral().getValue().toString()));
		}else {
			// not necesary throwing exception
		}
		field.set(object, typedLiterals);
	}
	
	// --
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
	
	}
	
	// --
	
	 private boolean getAnnotationByLang(Field field) {
		 return field.getAnnotation(RdfDatatypeContainer.class).byLang();
	 }
	 
	 private boolean getAnnotationByDatatype(Field field) {
	 	 return field.getAnnotation(RdfDatatypeContainer.class).byDatatype();
	 }
	 
	 private boolean getAnnotationIsPath(Field field) {
	 	 return field.getAnnotation(RdfDatatypeContainer.class).isPath();
	 }
 
	 
	 private String getAnnotationValue(Field field) {
		 String annotationValue = field.getAnnotation(RdfDatatypeContainer.class).value();
		 if (annotationValue==null || annotationValue.isEmpty()) { 
			 String illegalMessage = KehioUtils.concatStrings("@RdfDatatypeContainer must be initialised with a value, please review annotation for attribute ", field.getName());
			 throw new IllegalArgumentException(illegalMessage);
		 }
		 return annotationValue;
	 }


}
