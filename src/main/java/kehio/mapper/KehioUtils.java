package kehio.mapper;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.util.ResultSetUtils;

//TODO: add more support to SPARQL paths
class KehioUtils {

	protected static final String START_CLASS_TOKEN = "<";
	protected static final String START_SECOND_CLASS_TOKEN = ", ";
	protected static final String END_CLASS_TOKEN = ">";
	
	private KehioUtils() {
		
	}
	
	
	protected static List<RDFNode> retrieveFromRdfPath(Model model, Resource subject, String propertyPath, Boolean isPath) throws IllegalArgumentException, IllegalAccessException {
		List<RDFNode> range = null;
		if(isPath) {
			range = extractPathData( model,  subject,  propertyPath);
 	 	}else {
 	 		range = model.listObjectsOfProperty(subject, ResourceFactory.createProperty(propertyPath)).toList();
 	 	}
		return range;
	}
	
	
	
	private static List<RDFNode> extractPathData(Model model, Resource subject, String annotationPathValue) throws IllegalArgumentException, IllegalAccessException {
		List<RDFNode> output = new ArrayList<>();
		Query query = QueryFactory.create(concatStrings("SELECT DISTINCT ?s ?o WHERE {  <", subject.getURI(),">",annotationPathValue, " ?o . }"));
		QueryExecution qexec = null;
		try{
			qexec = QueryExecutionFactory.create(query, model);
			ResultSet results = qexec.execSelect();
			output = ResultSetUtils.resultSetToList(results, "?o");
			
		} catch (Exception e) {
			e.printStackTrace();
			output = null;
		}finally {
			if (qexec !=null)
				qexec.close();
		}
		return output;
	}
	
	
	protected static String concatStrings(String ...message) {
		StringBuilder builder = new StringBuilder();
		for(int index =0; index < message.length; index++)
			builder.append(message[index]);
		return builder.toString();
	}
}
