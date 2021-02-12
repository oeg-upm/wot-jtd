package wot.jtd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfLiteral;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfResource;
import com.apicatalog.rdf.RdfValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
	
	public static Model toRDF(JsonObject td) throws JsonLdError {
		Model model = ModelFactory.createDefaultModel();
		InputStream tdStream = new ByteArrayInputStream(td.toString().getBytes());
		Document documentTD = JsonDocument.of(tdStream);
		RdfDataset dataset = JsonLd.toRdf(documentTD).get();
		Map<String,Resource> existingBlankNodes = new HashMap<>();
		dataset.toList().forEach(triple -> populateModel(triple, model, existingBlankNodes));
		return model;
	}

	/*private static void populateModel(RdfNQuad triple, Model model) {
		StringBuilder line = new StringBuilder();
		line.append(stringValueOf(triple.getSubject())).append(" ");
		line.append(stringValueOf(triple.getPredicate())).append(" ");
		line.append(stringValueOf(triple.getObject())).append(" ");
		
		Optional<RdfResource> graph = triple.getGraphName();
		if(graph.isPresent()) {
			line.append(stringValueOf(graph.get()));
		}
		line.append(" .");
		System.out.println(line.toString());
		InputStream stream = new ByteArrayInputStream(line.toString().getBytes());
		model.read(stream, "n3");
	}

	private static Object stringValueOf(RdfValue object) {
		StringBuilder builder = new StringBuilder();
		
		if(object.isBlankNode() || (object.isIRI() && !object.getValue().startsWith("htt"))) {
			builder.append(object.getValue());
		}else if(object.isIRI()){
			builder.append("<").append(object.getValue()).append(">");
		}else if(object.isLiteral()) {
			builder.append("\"").append(object.getValue()).append("\"");
			String datatype = object.asLiteral().getDatatype();
			Optional<String> lang = object.asLiteral().getLanguage();
			if(lang.isPresent()) {
				builder.append("@").append(lang.get());
			}else if(datatype!=null) {
				builder.append("^^").append(datatype);
			}
		}
		return builder.toString();
	}

	private static String stringValueOf(RdfResource resource) {
		StringBuilder builder = new StringBuilder();
		if(!resource.isBlankNode()) {
			builder.append("<").append(resource.toString()).append(">");
		}else {
			builder.append(resource.toString());
		}
		return builder.toString();
	}*/
	
	
	private static void populateModel(RdfNQuad triple, Model model, Map<String,Resource> existingBlankNodes) {
		Resource subject = instantiateResource(triple.getSubject(), model, existingBlankNodes);
		Property predicate = ResourceFactory.createProperty(triple.getPredicate().getValue());

		RdfValue object = triple.getObject();
		if(object.isIRI()) {
			Resource objectJena = ResourceFactory.createResource(object.getValue());
			model.add(subject, predicate, objectJena);
		}else if(object.isBlankNode()){
			Resource objectJena = instantiateBlankNode(object.getValue(), model, existingBlankNodes);
			model.add(subject, predicate, objectJena);
		}else if(object.isLiteral()) {
			RdfLiteral literal = object.asLiteral();
			Optional<String> lang = literal.getLanguage();
			String datatype = literal.getDatatype();
			Literal jenaLiteral = ResourceFactory.createPlainLiteral(literal.getValue());
			if(lang.isPresent()) {
				jenaLiteral = ResourceFactory.createLangLiteral(literal.getValue(), lang.get());
			}else if(datatype!=null) {
				RDFDatatype dt = NodeFactory.getType(datatype);
				jenaLiteral = ResourceFactory.createTypedLiteral(literal.getValue(), dt);
			}
			model.add(subject, predicate, jenaLiteral);
		}
	}
	
	private static Resource instantiateResource(RdfResource resource, Model model, Map<String,Resource> existingBlankNodes) {
		Resource instantiatedResource = null;
		if(resource.isIRI()) {
			instantiatedResource = ResourceFactory.createResource(resource.getValue());
		}else {
			instantiatedResource = instantiateBlankNode(resource.getValue(), model, existingBlankNodes);
		}
		return instantiatedResource;
	}
	
	private static Resource instantiateBlankNode(String blankNode, Model model, Map<String,Resource> existingBlankNodes) {
		Resource instantiatedResource = null;
		if(!existingBlankNodes.containsKey(blankNode)) {
			instantiatedResource =  model.createResource();
			existingBlankNodes.put(blankNode, instantiatedResource);
		}else {
			instantiatedResource = existingBlankNodes.get(blankNode);
		}
		return instantiatedResource;
	}
	
	
}
