package wot.jtd;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import wot.jtd.model.Thing;

/**
 * This class
 * @author Andrea Cimmino
 *
 */
public class JsonHandler {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private JsonHandler() {
		super();
	}
	
	// -- Protected Methods
	
	protected static JsonNode updateJsonThingPartially(Thing thing, JsonObject partialUpdate) throws IOException, JsonPatchException {
		JsonObject thingJson = thing.toJson();
		
		JsonNode partialJson = OBJECT_MAPPER.readTree(partialUpdate.toString());
		JsonNode existingThingNode = OBJECT_MAPPER.readTree(thingJson.toString());
		JsonMergePatch patch = JsonMergePatch.fromJson(partialJson);
		return  patch.apply(existingThingNode);
	}
	
	protected static Object instantiateFromJson(JsonObject json, Class<?> clazz) throws IOException {
		JsonObject newJson = json.deepCopy();
		expandArrays(newJson);
	
		if(!JTD.getRemoveNestedURNIds())
			removeNestedIds(newJson, true);

		return OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(newJson.toString().getBytes(), clazz);
	}
	
	protected static JsonObject toJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(object);
		JsonObject jsonObject = JTD.parseJson(jsonStr);
		
		compactArrays(jsonObject);
		if(!JTD.getRemoveNestedURNIds())
			removeNestedIds(jsonObject, true);
		// fix security removing id
		
		return jsonObject;
	}
	
	// -- Methods
	
	
	public static void compactArrays(JsonObject json) {
			Set<String> keys = new HashSet<>(json.keySet());
			// Find keys in the JSON that have an Array as value and which size is exactly 1, iterate over its value
			keys.stream().filter(key -> JTD.getArrayCompactKeys().contains(key) && json.get(key).isJsonArray() && json.getAsJsonArray(key).size()==1)
								  .forEach(key -> compactArray(json, key)); // iterate and compact
			
			// Find keys in the JSON that have another JSON as value, and recursively call this function
			keys.stream().filter(key -> json.get(key).isJsonObject()).forEach(key -> compactArrays(json.getAsJsonObject(key)));
			
			//Navigate over arrays
			keys.stream().filter(key -> json.get(key).isJsonArray()).forEach(key -> json.get(key).getAsJsonArray().forEach(JsonHandler::compactArraysElem));
	}
	
	private static void compactArraysElem(JsonElement elem) {
		if(elem.isJsonObject())
			compactArrays(elem.getAsJsonObject());
	}

	
	private static void removeNestedIds(JsonObject json, boolean first) {
		Set<String> keys = new HashSet<>(json.keySet());
		for(String key:keys) {
			JsonElement element = json.get(key);
			if(element.isJsonArray()) {
				JsonArray array = element.getAsJsonArray();
				for(int index=0; index < array.size(); index++) {
					JsonElement nestedElement = array.get(index);
					if(nestedElement.isJsonObject())
						removeNestedIds(nestedElement.getAsJsonObject(), false); 
				}
			}else if(element.isJsonObject()) {
				removeNestedIds(element.getAsJsonObject(), false);
			}else if(element.isJsonPrimitive() && (key.equals("id") || key.equals("@id")) && !first) {
				removeIdentifier(json, element, key);
			}
		}
	}
	
	private static void removeIdentifier(JsonObject json, JsonElement element, String key) {
		Boolean isURN = URN_PATTERN.matcher(element.getAsString()).matches();
		try{
			Boolean isUUID = isUUID(element.getAsString());
		
		if(isURN && JTD.getRemoveNestedURNIds()) {
			json.remove(key);
		}else if(isUUID && JTD.getRemoveNestedUUIds()) {
			json.remove(key);
		}else if(!isURN && !isUUID){
			json.remove(key);
		}
		}catch(Exception e) {
			System.out.println("the error was given by: "+element.getAsString());
		}
	}
	
	
	private static final Pattern URN_PATTERN = Pattern.compile(
	        "^urn:[a-z0-9][a-z0-9-]{0,31}:([a-z0-9()+,\\-.:=@;$_!*']|%[0-9a-f]{2})+/?.*$",
	        Pattern.CASE_INSENSITIVE);
	private static final Pattern UUID_PATTERN = Pattern.compile("[a-f0-9\\-]{36}");
	
	private static boolean isUUID(String uri) {
		Boolean isUUID = false;
		try {
			Matcher extracted = UUID_PATTERN.matcher(uri);
			// TODO: extract UUIds code
			if(extracted.find()){
				UUID.fromString(extracted.group());
				isUUID = true;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isUUID;
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

		keys.stream().filter(key -> JTD.getArrayCompactKeys().contains(key)).forEach(key -> expandArray(json,key));

		// Find keys in the JSON that have another JSON as value, and recursively call this function
		keys.stream().filter(key -> json.get(key).isJsonObject()).forEach(key -> expandArrays(json.getAsJsonObject(key)));
		
		// Find keys in the JSON that have an Array as value, iterate over its value
		keys.stream().filter(key -> json.get(key).isJsonArray())
							  .forEach(key -> callExpand(json.getAsJsonArray(key))); // iterate and filter elements that are objects
	
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
	
	// JSON comparison
	
	public static boolean compareJson(JsonElement element1, JsonElement element2) {
		Boolean sameJson = true;
		if (element1 instanceof JsonObject && element2 instanceof JsonObject) {
			SortedSet<String> keys1 = new TreeSet<>(element1.getAsJsonObject().keySet());
			SortedSet<String> keys2 = new TreeSet<>(element2.getAsJsonObject().keySet());
			sameJson = keys1.containsAll(keys2) && keys2.containsAll(keys1);
			for (String key : keys1) {
				JsonElement value1 = element1.getAsJsonObject().get(key);
				JsonElement value2 = element2.getAsJsonObject().get(key);
				sameJson = compareJson(value1, value2);

			}

		} else if (element1 instanceof JsonArray && element2 instanceof JsonArray) {
			JsonArray array1 = element1.getAsJsonArray();
			JsonArray array2 = element2.getAsJsonArray();
			sameJson = array1.size() == array2.size();
			for (int index_1 = 0; index_1 < array1.size(); index_1++) {
				JsonElement nestedElement1 = array1.get(index_1);
				Boolean existTheSame = false;
				int indexToRemove = 0;
				for (int index_2 = 0; index_2 < array2.size(); index_2++) {
					JsonElement nestedElement2 = array2.get(index_2);
					existTheSame = compareJson(nestedElement1, nestedElement2);
					indexToRemove = index_2;
					if (existTheSame)
						break;
				}
				if (existTheSame) {
					sameJson &= existTheSame;
					array2.remove(indexToRemove);
				} else {
					sameJson = false;
					break;
				}
			}
		} else if (element1 instanceof JsonPrimitive && element2 instanceof JsonPrimitive) {
			String primitive1 = element1.getAsJsonPrimitive().getAsString();
			String primitive2 = element2.getAsJsonPrimitive().getAsString();
			sameJson = primitive1.equals(primitive2);
		} else {
			sameJson = false;
		}

		return sameJson;
	}
	 
	

}
