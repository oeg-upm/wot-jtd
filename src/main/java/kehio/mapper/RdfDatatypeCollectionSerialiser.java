package kehio.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import kehio.annotations.done.RdfDatatypeCollection;


// TODO: check that we could have other type of primitive Wrapped collections
class RdfDatatypeCollectionSerialiser implements RdfMapper{

	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfDatatypeCollection.class);
	}

	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		String annotationValue = getAnnotationValue(field);
		Boolean sinkLang = getAnnotationSinkLang(field);
		Boolean sinkDatatype = getAnnotationSinkDatatype(field);
		Boolean isPath = getAnnotationIsPath(field);
		
		if((sinkLang || sinkDatatype) && !field.getType().equals(String.class))
 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("Optional 'sinkLang' and 'sinkDatatype' are only applicable for String attributes, please review the annotation for attribute ", field.getName()));
 	 	if(sinkLang && sinkDatatype)
 	 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeCollection must have only one option as True, either 'sinkLang' or 'sinkDatatype', please review the annotation for attribute ", field.getName()));
 	 	
		//
		List<RDFNode> values = KehioUtils.retrieveFromRdfPath(model, subject, annotationValue, isPath);
		
		if(!values.isEmpty()) {
			intialiseObject(field, object, values);
		}
		
		return annotationValue;
	 }	   
	
	
	

	


	// -- methods for creating regular datatyped property
	
	private void intialiseObject(Field field, Object object, List<RDFNode> objectProperties) throws IllegalArgumentException, IllegalAccessException {
		Class<?> fieldType = field.getType();
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			if(pt.getTypeName().contains("java.lang.String")) {
				initPrimitiveCollection(field, fieldType, object, objectProperties);
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeCollectionCollection must be used with Collections of Strings, i.e., Collection<String>. Please review annotation in attribute ", field.getName()));
			}
		}else {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeCollectionCollection can be used only with Collections. Please review annotation in attribute ", field.getName()));
		}
	}
 
	private void initPrimitiveCollection(Field field, Class<?> fieldType, Object object, List<RDFNode> objectProperties) throws IllegalArgumentException, IllegalAccessException {
		List<String> objects = new ArrayList<>();
		Boolean sinkLang = getAnnotationSinkLang(field);
		Boolean sinkDatatype = getAnnotationSinkDatatype(field);
		
		for(int index=0; index < objectProperties.size(); index++) {
			RDFNode node = objectProperties.get(index);
			if(node.isLiteral()) {
				if(sinkLang || sinkDatatype) {
					objects.add(objectProperties.get(index).toString());
				}else {
					objects.add(objectProperties.get(index).asLiteral().getString());
				}
			}else {
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeCollectionCollection can be used only with Collections of literals. Please review annotation in attribute ", field.getName(), ". There is a mismatch type in the RDF data with the node: ", node.toString()));
			}
		}
		
		instantiatePrimitiveCollection(field, fieldType, object, objects);
	}
	
	private void instantiatePrimitiveCollection(Field field, Class<?> fieldType, Object object, List<String> objects) throws IllegalArgumentException, IllegalAccessException {
		if(fieldType.equals(Collection.class) || fieldType.equals(List.class)) {
			Collection<String> collection = objects;
			field.set(object, collection);
		}else if(fieldType.equals(Set.class)) {
			Collection<String> collection = new HashSet<>(objects);
			field.set(object, collection);
		}else{
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatypeCollectionProperties can be used only with Collections, List, and Set. Please create an issue to add more java data structures.");
			throw new IllegalArgumentException(illegalMessage);
		}
	}
	
	 private String getAnnotationValue(Field field) {
		 String annotationValue = field.getAnnotation(RdfDatatypeCollection.class).value();
		 if (annotationValue==null || annotationValue.isEmpty()) { 
			 String illegalMessage = KehioUtils.concatStrings("@RdfDatatypeCollectionProperties must be initialised with a value, please review annotation for attribute ", field.getName());
			 throw new IllegalArgumentException(illegalMessage);
		 }
		 return annotationValue;
	 }

	// --
	
	private RdfDatatypeMapper rdfDatatypeMapper = new RdfDatatypeMapper();
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		
	}
	 
	 
	 
	 // --
	 private boolean getAnnotationSinkLang(Field field) {
		 return field.getAnnotation(RdfDatatypeCollection.class).sinkLang();
	 }
	 
	 private boolean getAnnotationSinkDatatype(Field field) {
	 	 return field.getAnnotation(RdfDatatypeCollection.class).sinkDatatype();
	 }

	 private boolean getAnnotationIsPath(Field field) {
	 	 return field.getAnnotation(RdfDatatypeCollection.class).isPath();
	 }
	 
	 
}
