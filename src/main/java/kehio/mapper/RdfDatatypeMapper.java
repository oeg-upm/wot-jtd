package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import kehio.annotations.done.RdfDatatype;

// TODO: match literals if they do not have lang or datatype, force match literals if they have one lang or dtype from those specified 
class RdfDatatypeMapper implements RdfMapper {

	// -- Compatibility method
	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfDatatype.class);
	}
	
	// -- from RDF to Object methods
	
	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		String annotationValue = getAnnotationValue(field);
 	 	boolean sinkLang = getAnnotationSinkLang(field);
 	 	boolean sinkDatatype = getAnnotationSinkDatatype(field);
 	 	boolean isPath = getAnnotationIsPath(field);
 	 	
 	 	
 	 	// Extracts data from an RDF path, which includes regular data properties property
 	 	List<RDFNode> literals = KehioUtils.retrieveFromRdfPath(model, subject,  annotationValue, isPath);
 	 	
		if(literals.size()==1) {
			Literal literal = literals.get(0).asLiteral();
			intialiseObject(field, literal, object, sinkLang, sinkDatatype);
		}else if(literals.size()>1) {
				// Try to find one that matches the configuration
				List<RDFNode> filteredNodes = new ArrayList<>();
				if(sinkLang) {
					filteredNodes = literals.stream().filter(node -> !node.asLiteral().getLanguage().isEmpty()).collect(Collectors.toList());
				}else  {
					filteredNodes = literals.stream().filter(node -> node.asLiteral().getLanguage().isEmpty()).collect(Collectors.toList());
				}
				if(filteredNodes.size()>1) {
					String illegalMessage = KehioUtils.concatStrings("@RdfDatatype is used with properties (or paths) with a cardinality of 1, provided RDF contains multiple objects in the range of the property ", annotationValue, " for the subject ",subject.toString()," consider using @RdfDatatypeProperties.");
					throw new IllegalArgumentException(illegalMessage);
				}else {
					Literal literal = filteredNodes.get(0).asLiteral();
					intialiseObject(field, literal, object, sinkLang, sinkDatatype);
				}
		}

		return annotationValue;
	 }	
	
	private void intialiseObject(Field field, Literal literal, Object object, Boolean sinkLang, Boolean sinkDatatype) throws IllegalArgumentException, IllegalAccessException {
		String datatype = literal.getDatatypeURI();
		String lang = literal.getLanguage();
	
		if(sinkLang && lang!=null && !lang.isEmpty()) {
			field.set(object, KehioUtils.concatStrings("",literal.getString(),"@",lang));
		}else if(sinkDatatype && datatype!=null && !datatype.isEmpty()) {
			field.set(object, KehioUtils.concatStrings("",literal.getString(),"^^",datatype));
		}else {
			try {
				field.set(object, literal.getValue());
			}catch(Exception e) {
				field.set(object, literal.getString());
			}
		}
	}
 
	// -- from Object to RDF methods
	
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		String annotationValue = getAnnotationValue(field);
		String datatype = getAnnotationDatatype(field);
		String lang = getAnnotationLang(field);
 	 	boolean sinkLang = getAnnotationSinkLang(field);
 	 	boolean sinkDatatype = getAnnotationSinkDatatype(field);
 	 	boolean isPath = getAnnotationIsPath(field);
 	
 	 	checkAnnotationConfiguration(field, annotationValue, datatype, lang, sinkLang, sinkDatatype, isPath);
 	 	
 	 	Object attributeValue = field.get(object);
 	 	
 	 	if(isPath) {
 	 		// deal with paths
 	 	}else {
 	 		if(attributeValue!=null) {
 	 			Property property = ResourceFactory.createProperty(annotationValue);
 	 	 		Literal literal = ResourceFactory.createTypedLiteral(attributeValue);
 	 	 		if(!datatype.isEmpty()) {
 	 	 			RDFDatatype dt = TypeMapper.getInstance().getTypeByName(datatype);
 	 	 			literal = ResourceFactory.createTypedLiteral(attributeValue.toString(), dt);
 	 	 		}else if(!lang.isEmpty()) {
 	 	 			literal = ResourceFactory.createLangLiteral(attributeValue.toString(), lang);
 	 	 		}else if(sinkDatatype) {
 	 	 			literal = buildSinkDatatypeLiteral(attributeValue);
 	 	 		}else if(sinkLang) {
 	 	 			literal = buildSinkLangLiteral(attributeValue);
 	 	 		}
 	 	 		model.add(subject, property, literal);
 	 		}
 	 		
 	 	}
 	 	
	}

	private Literal buildSinkDatatypeLiteral(Object attributeValue) {
		String value = attributeValue.toString();
		int splitIndex = value.lastIndexOf("^^")+2; // TODO: change this to single quote ' so it goes faster
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
	
	// -- Get annotation values methods
	
	
	private void checkAnnotationConfiguration(Field field, String annotationValue, String datatype, String lang, boolean sinkLang, boolean sinkDatatype, boolean isPath) {
		if (annotationValue == null || annotationValue.isEmpty()) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype must be initialised with a value, please review annotation for attribute ",field.getName());
			throw new IllegalArgumentException(illegalMessage);
		}else if(!datatype.isEmpty() && sinkLang) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " datatype and flag 'sinkLang' were provided. Please review annotation for attribute ",field.getName()," and keep only one.");
			throw new IllegalArgumentException(illegalMessage);
		} else if(!lang.isEmpty() && sinkDatatype) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " lang and flag 'sinkDatatype' were provided. Please review annotation for attribute ",field.getName()," and keep only one.");
			throw new IllegalArgumentException(illegalMessage);
		} else if(sinkLang && sinkDatatype) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " both flags 'sinkDatatype' and 'sinkLang' were set to True. Please review annotation for attribute ",field.getName()," and keep only one of these flags as True.");
			throw new IllegalArgumentException(illegalMessage);
		}else if(!datatype.isEmpty() && !lang.isEmpty()) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " a datatype and a lang were provided. Please review annotation for attribute ",field.getName()," and keep only one of these options.");
			throw new IllegalArgumentException(illegalMessage);
		} else if (!datatype.isEmpty() && sinkDatatype) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " a datatype was provided and also 'sinkDatatype' was set to True. Please review annotation for attribute ",field.getName()," and keep only the datatype or the sinkDatatype flag.");
			throw new IllegalArgumentException(illegalMessage);
		}else if (!lang.isEmpty() && sinkLang) {
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype error, for field ",field.getName(), " a lang was provided and also 'sinkLang' was set to True. Please review annotation for attribute ",field.getName()," and keep only the lang or the sinkLang flag.");
			throw new IllegalArgumentException(illegalMessage);
		}else if((sinkLang || sinkDatatype) && !field.getType().equals(String.class)) {
 	 		throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatype error, 'sinkLang' and 'sinkDatatype' are only applicable for String attributes, please review the annotation for attribute ", field.getName()));
		}
	}
	
	private String getAnnotationValue(Field field) {
		return  field.getAnnotation(RdfDatatype.class).value().trim();
	}

	private boolean getAnnotationSinkLang(Field field) {
		return field.getAnnotation(RdfDatatype.class).sinkLang();
	}

	private boolean getAnnotationSinkDatatype(Field field) {
		return field.getAnnotation(RdfDatatype.class).sinkDatatype();
	}

	private boolean getAnnotationIsPath(Field field) {
		return field.getAnnotation(RdfDatatype.class).isPath();
	}

	private String getAnnotationDatatype(Field field) {
		return field.getAnnotation(RdfDatatype.class).datatype().trim();
	}

	private String getAnnotationLang(Field field) {
		return field.getAnnotation(RdfDatatype.class).lang().trim();
	}

 
}
