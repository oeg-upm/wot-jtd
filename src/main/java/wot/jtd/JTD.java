package wot.jtd;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.jena.rdf.model.Model;
import com.apicatalog.jsonld.JsonLdError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;
import wot.jtd.vocabulary.Vocabulary;

public class JTD {

	private static Boolean defaultValues = false;
	private static Boolean showExternalValuesWarnings = false;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static List<String> relevantKeys = new CopyOnWriteArrayList<>();
	
	static {
		relevantKeys.add(Vocabulary.SCOPES); 
		relevantKeys.add(Vocabulary.OP); 
		relevantKeys.add(Vocabulary.JSONLD_TYPE); 
		relevantKeys.add(Vocabulary.JSONLD_CONTEXT); 
		relevantKeys.add(Vocabulary.ITEMS); 
		relevantKeys.add(Vocabulary.SECURITY); 
	}
	// -- getters and setters
	
	public static Boolean getDefaultValues() {
		return defaultValues;
	}

	public static void setDefaultValues(Boolean defaultValues) {
		JTD.defaultValues = defaultValues;
	}
		
	

	// -- methods
	
	public static Object instantiateFromJson(JsonObject json, Class<?> clazz) throws IOException {
		JsonObject newJson = json.deepCopy();
		expandArrays(newJson);
		return MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(newJson.toString().getBytes(), clazz);
	}
	
	public static JsonObject toJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(object);
		JsonObject jsonObject = parseJson(jsonStr);
		compactArrays(jsonObject);
		return jsonObject;
	}
	
	public static void compactArrays(JsonObject json) {
			Set<String> keys = new HashSet<>(json.keySet());
			// Find keys in the JSON that have an Array as value and which size is exactly 1, iterate over its value
			keys.stream().filter(key -> relevantKeys.contains(key) && json.get(key).isJsonArray() && json.getAsJsonArray(key).size()==1)
								  .forEach(key -> compactArray(json, key)); // iterate and compact
			
			// Find keys in the JSON that have another JSON as value, and recursively call this function
			keys.stream().filter(key -> json.get(key).isJsonObject()).forEach(key -> compactArrays(json.getAsJsonObject(key)));
			
	}
	
	private static void compactArray(JsonObject json, String key) {
			if(json.has(key) && json.getAsJsonArray(key).size()==1) {
				JsonElement element = json.getAsJsonArray(key).get(0);
				if( element instanceof JsonObject) {
					json.add(key, element);
				}else {
					json.addProperty(key, element.getAsString());
				}
			}	
	}
	

	public static void expandArrays(JsonObject json) {
		// Find keys in the JSON that have a 'primitive' value and that are expandable, then expand them
		Set<String> keys = new HashSet<>(json.keySet());

		keys.stream().filter(key -> relevantKeys.contains(key)).forEach(key -> expandArray(json,key));

		// Find keys in the JSON that have another JSON as value, and recursively call this function
		keys.stream().filter(key -> json.get(key).isJsonObject()).forEach(key -> expandArrays(json.getAsJsonObject(key)));
		/*for(String key:keys) {
			if(key.contains("actions"))
				System.out.println("here");
			if(json.get(key).isJsonObject()) {
				expandArrays(json.getAsJsonObject(key));
			}
		}*/

		// Find keys in the JSON that have an Array as value, iterate over its value
		keys.stream().filter(key -> json.get(key).isJsonArray())
							  .forEach(key -> callExpand(json.getAsJsonArray(key))); // iterate and filter elements that are objects
		/*for(String key:keys) {
			if(json.get(key).isJsonArray()) {
				callExpand(json.getAsJsonArray(key));
			}
		}*/
	}
	
	private static void callExpand(JsonArray array) {
		for(int index=0; index < array.size(); index++) {
			JsonElement elem = array.get(index);
			
			if(elem.isJsonObject()) {
				JsonObject json = elem.getAsJsonObject();
				expandArrays(json);
				array.set(index, json);
			}else if(elem.isJsonArray()) {
				callExpand( elem.getAsJsonArray());
			}
		}
	}
	
	private static void expandArray(JsonObject json, String key) {
		if(json.has(key) && !json.get(key).isJsonArray()) {
			JsonElement element = json.get(key);
			json.remove(key);
			JsonArray array = new JsonArray();
			array.add(element);
			json.add(key, array);
		}
	}
	
	public static JsonObject parseJson(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, JsonObject.class);
	}

	public static Boolean getShowExternalValuesWarnings() {
		return showExternalValuesWarnings;
	}

	public static void setShowExternalValuesWarnings(Boolean showExternalValues) {
		JTD.showExternalValuesWarnings = showExternalValues;
	}
	

	public static  Model toRDF(JsonObject td) throws JsonLdError {
		RDFHandler handler = new RDFHandler();	
		return handler.toRDF(td);
	}
	
	
	public static List<Thing> fromRDF(Model model) throws JsonLdError, IOException, SchemaValidationException {
		RDFHandler handler = new RDFHandler();	
		return handler.fromRDF(model);
	}
}
