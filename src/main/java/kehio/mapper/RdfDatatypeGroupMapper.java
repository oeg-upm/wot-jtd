package kehio.mapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import kehio.annotations.RdfDatatypeGroup;

class RdfDatatypeGroupMapper implements RdfMapper {

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfDatatypeGroup.class);
	}

	@Override
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		DgroupAnotation anotation = new DgroupAnotation(field);
 	 	List<RDFNode> rdfNodes = KehioUtils.objectNodes(model, subject, anotation.getAsProperty());
		
 	 	Map<String, String> map = instantiateMap(rdfNodes, anotation);		
		if(!map.isEmpty())
			KehioUtils.instantiateField(field, object,map);
		return anotation.getAsProperty();
	 }	   
	
	private Map<String,String> instantiateMap(List<RDFNode> rdfNodes, DgroupAnotation anotation ){
		Map<String, String> map =  new HashMap<>();
		for(int index=0; index < rdfNodes.size(); index++) {
 	 		RDFNode node = rdfNodes.get(index);
 	 		if(node.isLiteral()) {
 	 			Literal literal = node.asLiteral();
 	 			if(anotation.isByLang() && !literal.getLanguage().isEmpty()) {
 	 				map.put(literal.getLanguage(), literal.getValue().toString());
 	 			}else if(anotation.isByDatatype()) {
 	 				map.put(literal.getDatatypeURI(), literal.getValue().toString());
 	 			}
 	 		}
 	 	}
		return map;
	}
	
 

	
	// -- from Object to RDF
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalAccessException {
		field.setAccessible(true);
		
		DgroupAnotation anotation = new DgroupAnotation(field);
				
		@SuppressWarnings("unchecked") 
		Map<String,String> values = (Map<String, String>) field.get(object);
		if(values!=null) {
			for(Entry<String, String> entry : values.entrySet()) {
				Literal nodeLiteral = buildLiteral(anotation, entry);
				if(nodeLiteral!=null)
					model.add(subject, anotation.getAsProperty(), nodeLiteral);
			}
		}
	}
	
	private Literal buildLiteral(DgroupAnotation anotation, Entry<String, String> entry) {
		Literal literal = null;
		if(anotation.isByLang())
			literal = ResourceFactory.createLangLiteral(entry.getValue(), entry.getKey());
		if(anotation.isByDatatype()) {
			RDFDatatype dt = TypeMapper.getInstance().getTypeByName(entry.getKey());
			literal = ResourceFactory.createTypedLiteral(entry.getValue(), dt);
		}
		return literal;
	}
	


}

class DgroupAnotation{

	private Field field;
	
	public DgroupAnotation(Field field) {
		this.field = field;
		checkAnnotation();
	}
	
	private void checkAnnotation() {
		if (getValue()==null || getValue().isEmpty()) 
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeGroup must be initialised with a value, please review annotation for field ", field.getName()));
		if(!field.getType().equals(Map.class))
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeGroup must be used only with a Map wich keys are always a String and its values ajava wrapping primitive object. Please review annotation in field ", field.getName()));
		if(isByLang() && isByDatatype())
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeGroup must have as true only one flag, either 'byLang' or 'byDatatype'. Please review annotation in field ", field.getName()));
		Entry<String,String> mapTypes = KehioUtils.extractMapTypes(field);
		if(!mapTypes.getKey().equals(KehioUtils.STRING_CLASS))
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeGroup must be used with a Map wich keys are always a String. Please review annotation in field ", field.getName()));
		if(!mapTypes.getValue().equals(KehioUtils.STRING_CLASS))
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfDatatypeGroup must be used with a Map wich values are always a String. Please review annotation in field ", field.getName()));
	}
	
	 public boolean isByLang() {
		 return field.getAnnotation(RdfDatatypeGroup.class).byLang();
	 }
	 
	 public boolean isByDatatype() {
	 	 return field.getAnnotation(RdfDatatypeGroup.class).byDatatype();
	 }
	 

	 public Field getField() {
		return field;
	}
		 
 
	 public String getValue() {
		return field.getAnnotation(RdfDatatypeGroup.class).value();
	 }
	 
	 public Property getAsProperty() {
			return ResourceFactory.createProperty(field.getAnnotation(RdfDatatypeGroup.class).value());
		 }
	

}
