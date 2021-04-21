package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.XSD;

import kehio.annotations.RdfDatatype;

/**
 * Implements all Collection interfaces from https://www.javatpoint.com/collections-in-java
 * @author cimmino
 *
 */
class RdfDatatypeMapper implements RdfMapper {

	// -- Compatibility method
	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfDatatype.class);
	}
	
	// -- from RDF to Object methods
	
	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		DatatypeAnotation annotation = new DatatypeAnotation(field);
		List<Object> fieldValues = serialiseToWrappingPrimitiveCollection(annotation, model, subject);
 	 	if(KehioUtils.isWrappingPrimitive(field)) {
 	 		if(!fieldValues.isEmpty())
 	 			KehioUtils.instantiateField(field, object, fieldValues.get(0));
 	 	}else if(KehioUtils.isCollectionOfWrappingPrimitive(field)) {
 	 		KehioUtils.instantiateCollection(field, fieldValues, object);
 	 	}else {
 			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatype must be used with wrapping primitive types: String, Integer, Boolean, [...]; or a Collection, a List, a Queue, or a Set containing one of these wrapping primitive types. Please review annotation for attribute ",field.getName()));
 	 	}
		return annotation.getProperty();
	 }	
	
	private List<Object> serialiseToWrappingPrimitiveCollection(DatatypeAnotation annotation, Model model, Resource subject) {
		List<Object> fieldValues = new ArrayList<>();
		Field field = annotation.getField();
		List<RDFNode> literals = KehioUtils.objectNodes(model, subject, annotation.getProperty());
		for(int index=0; index < literals.size(); index++) {
			Object fieldValue = null;
			RDFNode node = literals.get(index);
			if(node.isLiteral()) {
				Literal literal = node.asLiteral();
				if(!KehioUtils.isFieldString(field) && (literal.getValue().getClass().equals(field.getType()) || (literal.getValue() instanceof Number && field.getType().equals(Number.class)))) {
					// Non string attribute
					fieldValue = literal.getValue();
				}else {
					fieldValue = serialiseToString(annotation, literal);
				}
			}
			if(fieldValue!=null)
				fieldValues.add(fieldValue);
		}
		return fieldValues;
	}
	
	private String serialiseToString(DatatypeAnotation annotation, Literal literal) {
		String instantiatedValue = null;
		String literalValue = literal.getValue().toString();
		if(annotation.isSinkLang()) {
			instantiatedValue = KehioUtils.concatStrings(literalValue,"@",literal.getLanguage());
		}else if(annotation.isSinkDatatype()) {
			instantiatedValue = KehioUtils.concatStrings(literalValue,"^^",literal.getDatatypeURI());
		}else if(!annotation.getLang().isEmpty() && !literal.getLanguage().isEmpty() && literal.getLanguage().equals(annotation.getLang())) {
			instantiatedValue = literalValue;
		}else if(!annotation.getDatatype().isEmpty() && literal.getDatatypeURI().equals(annotation.getDatatype())) {
			instantiatedValue = literal.getValue().toString();
		}else if(annotation.getDatatype().isEmpty() && literal.getDatatypeURI().equals(XSD.xstring.getURI())){
			instantiatedValue = literal.getValue().toString();
		}
		return instantiatedValue;
	}
	
	
	
	/*private Collection<Object> serialiseToCollection(Object object, DatatypeAnotation annotation, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException {
		Collection<Object> collection = new ArrayList<>();
		String collectionType = KehioUtils.extractCollectionType(annotation.getField());
		
		List<RDFNode> literals = KehioUtils.objectNodes(model, subject, annotation.getProperty());
		for(int index=0; index < literals.size(); index++) {
			RDFNode node = literals.get(index);
			if(node.isLiteral()) {
			
			}
			if(fieldValue!=null)
				break;
			}
		}
		
		
		try {
			
			if(collectionType.equals(KehioUtils.STRING_CLASS)) { 
				instantiateNonStringCollection(object, dtypeAnnotation, model, subject, collectionClazz);	
			}else {
				instantiateStringCollection(object, dtypeAnnotation, model, subject);	
			}
		} catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		
		return collection;
	}  
	
	private void instantiateNonStringCollection(Object object, DatatypeAnotation annotation, Model model, Resource subject, Class<?> collectionClazz) throws IllegalAccessException {
		List<RDFNode> nodes = KehioUtils.objectNodes(model, subject, annotation.get);
		
		// Collection is not string
		String datatype = annotation.getDatatype();
		String expectedDatatype = TypeMapper.getInstance().getTypeByClass(collectionClazz).getURI();
		if(!datatype.isEmpty() && !datatype.equals(expectedDatatype)) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",annotation.getField().getName(), " it was annotated with the datatype ",datatype," which is incompatible for the Collection<",collectionClazz.getName(),">. Please check the annotation for this field, the datatype should be ",expectedDatatype," or similar");
			throw new IllegalArgumentException(illegalMessage);
		}
		Collection<Object> instantiatedCollection = nodes.stream().filter(node -> node.asLiteral().getDatatypeURI().equals(expectedDatatype)).collect(Collectors.toList());
		KehioUtils.instantiateCollection(dtypeAnnotation.getField(),  instantiatedCollection, object);
	}
	
	private void instantiateStringCollection(Object object, DatatypeAnotation dtypeAnnotation, Model model, Resource subject) throws IllegalAccessException {
		List<RDFNode> literals = KehioUtils.retrieveFromRdfPath(model, subject, dtypeAnnotation.getValue(), dtypeAnnotation.isPath());
		Collection<Object> collection = new ArrayList<>();
		for(int index=0; index < literals.size(); index++) {
			Literal literal = literals.get(index).asLiteral();
			String value = extractValueFromLiteral(dtypeAnnotation, literal);
			collection.add(value);
		}
		KehioUtils.instantiateCollection(dtypeAnnotation.getField(),  collection,  object);
	}*/
	
	
	// -- from Object to RDF methods
	
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
			
		DatatypeAnotation dtypeAnnotation = new DatatypeAnotation(field);
		Object attributeValue = field.get(object);
		
 	 	if(KehioUtils.isWrappingPrimitive(field)) {
 	 		 buildWrappingPrimitiveRdf(dtypeAnnotation, attributeValue, model, subject);
 	 	}else if(KehioUtils.isCollectionOfWrappingPrimitive(field)) {
 	 		buildCollectionOfWrappingPrimitiveRdf(dtypeAnnotation, attributeValue, model, subject);
 	 	}else {
 			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatype must be used with wrapping primitive types: String, Integer, Boolean, [...]; or a Collection, a List, a Queue, or a Set containing one of these wrapping primitive types. Please review annotation for attribute ",field.getName()));
 	 	}

	}
	
	@SuppressWarnings("unchecked")
	private void buildCollectionOfWrappingPrimitiveRdf(DatatypeAnotation dtypeAnnotation, Object attributeValue, Model model, Resource subject) {
		Collection<Object> values = (Collection<Object>) attributeValue;
		Iterator<Object> valuesIterator = values.iterator();
		while(valuesIterator.hasNext()) {
			Object innerValue = valuesIterator.next();
			buildWrappingPrimitiveRdf(dtypeAnnotation, innerValue, model, subject);
		}
	}

	private void buildWrappingPrimitiveRdf(DatatypeAnotation dtypeAnnotation, Object attributeValue, Model model, Resource subject) {
		if(attributeValue!=null) {
 	 			Property property = ResourceFactory.createProperty(dtypeAnnotation.getValue());
 	 	 		Literal literal = ResourceFactory.createTypedLiteral(attributeValue);
 	 	 		if(!dtypeAnnotation.getDatatype().isEmpty()) {
 	 	 			RDFDatatype dt = TypeMapper.getInstance().getTypeByName(dtypeAnnotation.getDatatype());
 	 	 			literal = ResourceFactory.createTypedLiteral(attributeValue.toString(), dt);
 	 	 		}else if(!dtypeAnnotation.getLang().isEmpty()) {
 	 	 			literal = ResourceFactory.createLangLiteral(attributeValue.toString(), dtypeAnnotation.getLang());
 	 	 		}else if(dtypeAnnotation.isSinkDatatype()) {
 	 	 			literal = buildSinkDatatypeLiteral(attributeValue);
 	 	 		}else if(dtypeAnnotation.isSinkLang()) {
 	 	 			literal = buildSinkLangLiteral(attributeValue);
 	 	 		}
 	 	 		model.add(subject, property, literal);
 	 		}
	}
	

	private Literal buildSinkDatatypeLiteral(Object attributeValue) {
		String value = attributeValue.toString();
		int splitIndex = value.lastIndexOf("^^")+2;
		String dtype = value.substring(splitIndex, value.length());
		dtype = dtype.substring(1, dtype.length()-1);
		value = value.substring(0,splitIndex-2);
		RDFDatatype dt = TypeMapper.getInstance().getTypeByName(dtype);
		return ResourceFactory.createTypedLiteral(value, dt);
	}
	
	private Literal buildSinkLangLiteral(Object attributeValue) {
		String value = attributeValue.toString();
		int splitIndex = value.lastIndexOf('@')+1;
		String lang = value.substring(splitIndex, value.length());
		value = value.substring(0,splitIndex-1);
		return ResourceFactory.createLangLiteral(value, lang);
	}
	


	class DatatypeAnotation {
		
		private Field field;
		
		public DatatypeAnotation(Field field){
			this.field = field;
	 	 	checkAnnotationConfiguration();
		}
	
		private static final String ERROR = "@RdfDatatype error, for field ";
		private void checkAnnotationConfiguration() {
			if (getValue() == null || getValue().isEmpty()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatype must be initialised with a value, please review annotation for attribute ",field.getName()));
			if(!getDatatype().isEmpty() && isSinkLang()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR,field.getName(), " datatype and flag 'sinkLang' were provided. Please review annotation for attribute ",field.getName()," and keep only one."));
			if(!getLang().isEmpty() && isSinkDatatype()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR,field.getName(), " lang and flag 'sinkDatatype' were provided. Please review annotation for attribute ",field.getName()," and keep only one."));
			if(isSinkLang() && isSinkDatatype()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR,field.getName(), " both flags 'sinkDatatype' and 'sinkLang' were set to True. Please review annotation for attribute ",field.getName()," and keep only one of these flags as True."));
			if(!getDatatype().isEmpty() && !getLang().isEmpty()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR,field.getName(), " a datatype and a lang were provided. Please review annotation for attribute ",field.getName()," and keep only one of these options."));
			if (!getDatatype().isEmpty() && isSinkDatatype()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR ,field.getName(), " a datatype was provided and also 'sinkDatatype' was set to True. Please review annotation for attribute ",field.getName()," and keep only the datatype or the sinkDatatype flag."));
			if (!getLang().isEmpty() && isSinkLang()) 
				throw new IllegalArgumentException(KehioUtils.concatStrings(ERROR ,field.getName(), " a lang was provided and also 'sinkLang' was set to True. Please review annotation for attribute ",field.getName()," and keep only the lang or the sinkLang flag."));
			if((isSinkLang() || isSinkDatatype()) && !KehioUtils.isCollectionOfWrappingPrimitive(field) && !field.getType().equals(String.class)) 
	 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatype error, 'sinkLang' and 'sinkDatatype' are only applicable for String attributes, please review the annotation for attribute ", field.getName()));
			//if(isSinkLang() || isSinkDatatype() || !getLang().isEmpty()) 
			//	throw new IllegalArgumentException( KehioUtils.concatStrings(ERROR, field.getName(), " it was used with a Collection containing a wrapping primitive that is not String, in this case flags 'sinkLang' and 'sinkDatatype', and also, 'lang' can not be used."));
			
			// TODO: datatype solo se puede usar con un field string y lang tambien
		}
		
		// -- Getters & Setters
	
		public String getValue() {
			return field.getAnnotation(RdfDatatype.class).value().trim();
		}
		
		public Property getProperty() {
			return ResourceFactory.createProperty(field.getAnnotation(RdfDatatype.class).value().trim());
		}
	
		public String getDatatype() {
			return field.getAnnotation(RdfDatatype.class).datatype().trim();
		}
	
		public String getLang() {
			return field.getAnnotation(RdfDatatype.class).lang().trim();
		}
	
		public boolean isSinkLang() {
			return field.getAnnotation(RdfDatatype.class).sinkLang();
		}
	
		public boolean isSinkDatatype() {
			return field.getAnnotation(RdfDatatype.class).sinkDatatype();
		}
	
		public Field getField() {
			return field;
		}
	}

}