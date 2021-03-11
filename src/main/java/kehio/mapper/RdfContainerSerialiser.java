package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import kehio.annotations.RdfContainer;

class RdfContainerSerialiser implements RdfMapper{

	
	@Override
	public boolean hasProcesableAnnotation(Field field) {
		return field.isAnnotationPresent(RdfContainer.class);
	}

	@Override
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject)
			throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String processRdfContainer(Field field, Object object, Model model, Resource subject, Set<String> forbiddenProperties) throws IllegalAccessException {
		field.setAccessible(true);
		
		String annotationValue = null; // This annotation has no value
		
		Map<String,String> collectionOfTriples = new HashMap<>();
		model.listStatements().filterDrop(st -> forbiddenProperties.contains(st.getPredicate().toString())).forEach(st -> collectionOfTriples.put(st.getPredicate().toString(), st.getObject().toString()));
		if(!collectionOfTriples.isEmpty()) {
			intialiseObject(field, object, collectionOfTriples);
		}

		return annotationValue;
	 }	   
	
	private void intialiseObject(Field field, Object object, Map<String,String> collectionOfTriples) throws IllegalArgumentException, IllegalAccessException {
		Class<?> fieldType = field.getType();
		if(fieldType.equals(Map.class)) {
			field.set(object, collectionOfTriples);
		}else {
			String illegalMessage = KehioUtils.concatStrings("@RdfContainer can be used only with Map<String,String>. Please create an issue to add more java data structures.");
			throw new IllegalArgumentException(illegalMessage);
		}
	}

	
	@Override
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
	
	}
	
 
	
}
