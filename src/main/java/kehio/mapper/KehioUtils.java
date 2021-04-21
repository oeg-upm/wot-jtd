package kehio.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
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
	
	// -- General methods
	
	protected static String concatStrings(String ...message) {
		StringBuilder builder = new StringBuilder();
		for(int index =0; index < message.length; index++)
			builder.append(message[index]);
		return builder.toString();
	}
	
	// -- Data retrieval in RDF methods
	
	/*protected static List<RDFNode> retrieveFromRdfPath(Model model, Resource subject, String propertyPath, Boolean isPath) throws IllegalArgumentException, IllegalAccessException {
		List<RDFNode> range = null;
		if(isPath) {
			range = extractPathData( model,  subject,  propertyPath);
 	 	}else {
 	 		range = model.listObjectsOfProperty(subject, ResourceFactory.createProperty(propertyPath)).toList();
 	 	}
		return range;
	}*/
	
	protected static List<RDFNode> objectNodes(Model model, Resource subject, Property property){
		return model.listObjectsOfProperty(subject, property).toList();
	}
	
	protected static List<RDFNode> objectNodes(Model model, Resource subject, String property){
		return model.listObjectsOfProperty(subject, ResourceFactory.createProperty(property)).toList();
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
	
	// -- Datatyped properties methods
	protected static final String STRING_CLASS = "java.lang.String"; 
	protected static final String URI_CLASS = URI.class.getName();
	protected static final String COLLECTION_CLASS = Collection.class.getName();
	
	private static final List<String> WRAPPING_PRIMITIVE_TYPES = new ArrayList<>();
	static {
		WRAPPING_PRIMITIVE_TYPES.add(STRING_CLASS);
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Long");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Float");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Double");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Character");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Boolean");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Integer");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Short");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Byte");
		WRAPPING_PRIMITIVE_TYPES.add("java.lang.Number");
	}
	
	
	
	protected static boolean isWrappingPrimitive(Field field) {
		return isWrappingPrimitive(field.getType().getName());
	}
	
	protected static boolean isWrappingPrimitive(String fieldName) {
		return WRAPPING_PRIMITIVE_TYPES.contains(fieldName);
	}
	
	  

	
	 protected static boolean isFieldClass(Field field) {
		 return !KehioUtils.isWrappingPrimitive(field) && !KehioUtils.isCollection(field);
	 }
	 
	 protected static boolean isFieldString(Field field) {
		 return field.getType().equals(String.class);
	 }
	 
	 protected static boolean isFieldURI(Field field) {
		 return field.getType().equals(URI.class);
	 }
	
	// Collections methods
	
	

	public static boolean isCollection(Field field) {
		Type fieldType = field.getType();
		Boolean correctType = fieldType.equals(Collection.class) || fieldType.equals(List.class) || fieldType.equals(Set.class);
		correctType = correctType || fieldType.equals(Queue.class) || fieldType.equals(Deque.class) || fieldType.equals(SortedSet.class);
		return correctType;
	}
	
	protected static String extractCollectionType(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		String typeName = pt.getTypeName();
		typeName = typeName.substring(typeName.indexOf(KehioUtils.START_CLASS_TOKEN)+1, typeName.indexOf(KehioUtils.END_CLASS_TOKEN));
		return typeName;
	}
	
	public static boolean isCollectionOfWrappingPrimitive(Field field) {
		Boolean correctType = isCollection(field) ;
		correctType &= field.getGenericType() instanceof ParameterizedType;
		if(correctType) {
			String innerValue = extractCollectionType(field);
			correctType &= KehioUtils.isWrappingPrimitive(innerValue);
		}
		return correctType;
	}
	
	
	
	
	// Map methods
	
	protected static Entry<String,String> extractMapTypes(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		String typeName = pt.getTypeName();
		typeName = typeName.substring(typeName.indexOf(KehioUtils.START_CLASS_TOKEN)+1, typeName.indexOf(KehioUtils.END_CLASS_TOKEN));
		String[] mapTypes = typeName.split(", ");
		return Map.entry(mapTypes[0], mapTypes[1]);
	}
	
	protected static  Class<?> extractMapValueType(Field field){
		Class<?> clazz = null;
		try {
			String mapType = KehioUtils.extractMapTypes(field).getValue();
			clazz = Class.forName(mapType);
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		return clazz;
	}
	
	// other
	
	public static boolean isURI(String url){
	    Boolean isURL = false;
		try {
	        URI uri = new URI(url);
	        isURL = uri.isAbsolute() ;//contains(":"); // true && (true is implicit as far as no exception is thrown) 
	    } catch (Exception exception) {
	    		isURL = false;
	    }
		return isURL;
	}
	
	public static boolean isValidResource(String url) {
		Boolean isURL = false;
		try {
			ResourceFactory.createResource(url);
			isURL = true;
		}catch(Exception e) {
			isURL = false;
		}
		return isURL;
	}
	
	// -- Instantiation methods
	
	/**
	 * This method receives a field ({@link Collection}, {@link Set}, {@link List}, {@link Queue}, {@link Deque}, or {@link Sortedset}) belonging to an object, and also, a generic collection containing its instantiating values.<p>
	 * As a result, this method instantiates the field of the object with the provided collection.
	 * @param field the field ({@link Collection}, {@link Set}, {@link List}, {@link Queue}, {@link Deque}, or {@link Sortedset})
	 * @param instantiatedCollection the instantiated collection
	 * @param object the generic object that has the provided field and which will be instantiated with the provided collection
	 * @throws IllegalArgumentException in case that the field is not one of {@link Collection}, {@link Set}, {@link List}, {@link Queue}, {@link Deque}, or {@link Sortedset}
	 * @throws IllegalAccessException in case the field does not belongs to the provided object
	 */
	protected static void instantiateCollection(Field field, Collection<Object> instantiatedCollection, Object object) throws IllegalArgumentException, IllegalAccessException {
		Type fieldType = field.getType();
		Collection<Object> collection = null;
		if(fieldType.equals(Collection.class) || fieldType.equals(List.class)) {
			collection = instantiatedCollection;
		}else if(fieldType.equals(Set.class)) {
			collection = new HashSet<>(instantiatedCollection);
		}else if(fieldType.equals(Queue.class) || fieldType.equals(Deque.class)) {
			collection = new LinkedList<>(instantiatedCollection);
		}else if(fieldType.equals(SortedSet.class)) {
			collection = new TreeSet<>(instantiatedCollection);
		}else{
			String illegalMessage = KehioUtils.concatStrings("@RdfDatatype and/or @RdfObject can be used with Collections, List, Set, SortedSet, Queue, Dequeue. Please create an issue to add more java data structures. Please review annotation for attribute ",field.getName());
			throw new IllegalArgumentException(illegalMessage);
		}
		if(collection!=null && !collection.isEmpty())
			field.set(object, collection);
	}
	
	 protected static void instantiateField(Field field, Object object, Object fieldValue) throws IllegalArgumentException, IllegalAccessException {
		 if(fieldValue!=null)
			 field.set(object, fieldValue);
	 }
	 
	 protected static Object instantiatedField(Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		 return field.get(object);
	 }

	 
	
	
}
