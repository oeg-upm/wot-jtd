package kehio.mapper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public interface RdfMapper {

	
	public boolean hasProcesableAnnotation(Field field);
	public String fromRdfToObject(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException;
	public void fromObjectToRdf(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException;

}
