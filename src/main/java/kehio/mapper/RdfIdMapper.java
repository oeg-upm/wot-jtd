package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import kehio.annotations.RdfId;

class RdfIdMapper implements RdfMapper{

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfId.class);
	}

	
	@Override    
	public Property fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		if(field.getType().equals(String.class)) {
			instantiateStringField(field, subject, object, model);
		}else if(field.getType().equals(URI.class)) {
			instantiateURIField(field, subject, object, model);
		}else {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfId can be only used with String or java.net.URI types. Please review annotation for attribute ", field.getName()));
		}
		return null;
	 }	
	
	private void instantiateStringField(Field field, Resource objectProperty, Object object, Model model) throws IllegalArgumentException, IllegalAccessException {
		if(objectProperty.asNode().isBlank()){
			field.set(object, objectProperty.asNode().getBlankNodeLabel());
		}else if( objectProperty.asNode().isURI()) {
			field.set(object, objectProperty.toString());
		}else {
			// Nothing happens
		}
	}
	
	private void instantiateURIField(Field field, Resource objectProperty, Object object, Model model) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		if(objectProperty.asNode().isBlank()){
			field.set(object, new URI(objectProperty.asNode().getBlankNodeLabel()));
		}else if( objectProperty.asNode().isURI()) {
			field.set(object, new URI(objectProperty.toString()));
		}else {
			// Nothing happens
		}
	}

	// -- 
	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource nullSubject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		field.setAccessible(true);
		checkFieldCompatibility(field);
		Object id = field.get(object);
		Resource subject =  null;
		if(id!=null && !id.toString().trim().isEmpty()) {
			subject =  model.createResource(id.toString().trim());
			model.add(subject, RDF.type, Kehio.KEHIO_TYPE);
		}	
	}
	
	private void checkFieldCompatibility(Field field) {
		if(!field.getType().equals(String.class) && !field.getType().equals(URI.class)) {
			throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfId can be only used with String or java.net.URI types. Please review annotation for attribute ", field.getName()));
		}
	}
	
	
}
