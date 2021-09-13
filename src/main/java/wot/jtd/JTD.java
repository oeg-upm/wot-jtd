package wot.jtd;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class JTD {

	private static Boolean defaultValues = false;
	private static Boolean removeNestedURNIds = false;
	private static Boolean removeNestedUUIds = true;
	private static Boolean includeDefaultLanguageInRDF = false;
	private static String defaultRdfNamespace = "https://oeg-upm.github.io/wot-jtd/";
	private static List<String> arrayCompactKeys = new CopyOnWriteArrayList<>();
	public static final boolean NORMALIZE_SECURITY_DEFINITIONS_SCHEMES = true;
	public static final Pattern SECURITY_DEFINITION_SCHEMES_PATTERN = Pattern.compile("^[a-z0-9]+\\_sc$");
	static {
		arrayCompactKeys.add(Vocabulary.SCOPES); 
		arrayCompactKeys.add(Vocabulary.OP); 
		arrayCompactKeys.add(Vocabulary.JSONLD_TYPE); 
		
		arrayCompactKeys.add(Vocabulary.JSONLD_CONTEXT); 
		arrayCompactKeys.add(Vocabulary.ITEMS); 
		arrayCompactKeys.add(Vocabulary.SECURITY); 
	}
	
	private JTD() {
		super();
	}
	
	// Getters & Setters
	
	public static List<String> getArrayCompactKeys() {
		return arrayCompactKeys;
	}
	public static void setArrayCompactKey(String key) {
		arrayCompactKeys.add(key); 
	}
	public static void removeArrayCompactKey(String key) {
		arrayCompactKeys.remove(key); 
	}
	
	// -- getters and setters
	
	
	
	public static Boolean getDefaultValues() {
		return defaultValues;
	}

	public static String getDefaultRdfNamespace() {
		return defaultRdfNamespace;
	}

	public static void setDefaultRdfNamespace(String defaultRdfNamespace) {
		JTD.defaultRdfNamespace = defaultRdfNamespace;
	}

	public static void setDefaultValues(Boolean defaultValues) {
		JTD.defaultValues = defaultValues;
	}
			
	public static Boolean getRemoveNestedURNIds() {
		return removeNestedURNIds;
	}

	public static void setRemoveNestedURNIds(Boolean removeNestedURNIds) {
		JTD.removeNestedURNIds = removeNestedURNIds;
	}

	public static Boolean getRemoveNestedUUIds() {
		return removeNestedUUIds;
	}

	public static void setRemoveNestedUUIds(Boolean removeNestedUUIds) {
		JTD.removeNestedUUIds = removeNestedUUIds;
	}
	
	public static boolean getIncludeDefaultLanguageInRDF() {
		return includeDefaultLanguageInRDF;
	}
	
	public static void setIncludeDefaultLanguageInRDF(Boolean includeDefaultLanguageInRDF) {
		JTD.includeDefaultLanguageInRDF = includeDefaultLanguageInRDF;
	}

	// -- JSON methods
	
	public static JsonObject parseJson(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, JsonObject.class);
	}
	
	public static Object instantiateFromJson(JsonObject json, Class<?> clazz) throws IOException {
		return JsonHandler.instantiateFromJson(json, clazz);
	}
	
	public static JsonObject toJson(Object object) throws JsonProcessingException {
		return JsonHandler.toJson(object); 
	}

	public static Thing updateJsonThingPartially(Thing thing, JsonObject partialUpdate) throws IOException, JsonPatchException {
		JsonNode node = JsonHandler.updateJsonThingPartially(thing, partialUpdate);
		return (Thing) JTD.instantiateFromJson(JTD.parseJson(node.toString()).deepCopy(), Thing.class);
	}

	// -- RDF methods
	
	public static Model toRDF(JsonObject td) throws IllegalAccessException, ClassNotFoundException, URISyntaxException, IOException, SchemaValidationException {
		Thing thing = (Thing) JTD.instantiateFromJson(td, Thing.class);
		return RdfHandler.toRDF(thing);
	}
	
	public static Model toRDF(Thing thing) throws IllegalAccessException, ClassNotFoundException, URISyntaxException, IOException, SchemaValidationException {
		return RdfHandler.toRDF(thing);
	}
	

	public static Thing fromRDF(Model model, String thingId) {
		return RdfHandler.fromRDF(model, ResourceFactory.createResource(thingId));
	}
	
	public static List<Thing> fromRDF(Model model) throws SchemaValidationException {
		return RdfHandler.fromRDF(model);
	}
	
	// -- Validation methods
	
	public static ValidationReport validateWithShape(Thing thing, Model shape) throws IllegalAccessException, ClassNotFoundException, URISyntaxException, IOException, SchemaValidationException {
		Shapes shapes = Shapes.parse(shape.getGraph());
		Model thingModel = JTD.toRDF(thing);
		return ShaclValidator.get().validate(shapes, thingModel.getGraph());
	}
	
	
	
	
}
