package wot.jtd;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.XSD;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kehio.mapper.Kehio;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class RdfHandler {

	private RdfHandler() {
		super();
	}
	
	private static final String CONTEXT_LANGUAGE_TAG = "@language";
	private static final String CONTEXT_TAG = "@context";
	
	private static Map<String,String> context = new HashMap<>();
	static {
		context.put("htv","http://www.w3.org/2011/http#");
		context.put("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		context.put("xsd","http://www.w3.org/2001/XMLSchema#");
		context.put("td","https://www.w3.org/2019/wot/td#");
		context.put("jsonschema","https://www.w3.org/2019/wot/json-schema#");
		context.put("wotsec","https://www.w3.org/2019/wot/security#");
		context.put("hctl","https://www.w3.org/2019/wot/hypermedia#");
		context.put("dct","http://purl.org/dc/terms/");
		context.put("schema","http://schema.org/");
		context.put("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	}
	
	// -- Deserialisation methods
	
	public static Model toRDF(Thing thing) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException, IOException, SchemaValidationException {
		// Extract context
		JsonObject thingJson = JTD.toJson(thing);
		JsonElement contextAbstractElement = thingJson.get(CONTEXT_TAG);		
		Map<String,String> prefixes = new HashMap<>();
		// Improve context from external specified sources
		enhanceContext(contextAbstractElement, prefixes); 
		// Load default WoT Context
		prefixes.putAll(context);
		// Check if there is a default language
		String defaultLanguage = prefixes.get(CONTEXT_LANGUAGE_TAG);
		prefixes.remove(CONTEXT_LANGUAGE_TAG);
		// Serialise and add default language if necessary
		Model model = Kehio.deserializeClass(thing, prefixes);
		if(JTD.getIncludeDefaultLanguageInRDF() && defaultLanguage!=null)
			addLanguageToModel(model, defaultLanguage);
		
		return model;
	}
	
	private static void enhanceContext(JsonElement contextAbstractElement, Map<String,String> prefixes) throws JsonProcessingException {
		if(contextAbstractElement.isJsonArray()) {
			JsonArray contextElements = contextAbstractElement.getAsJsonArray();
			for(int index=0; index < contextElements.size(); index++) {
				JsonElement contextElement = contextElements.get(index);
				if(contextElement.isJsonObject()) {
					JsonObject prefixOject = contextElement.getAsJsonObject();
					prefixOject.entrySet().forEach( entry -> prefixes.put(entry.getKey(), entry.getValue().getAsString()));
				}
			}
		}else if (contextAbstractElement.isJsonObject()) {
			JsonObject prefixOject = contextAbstractElement.getAsJsonObject();
			prefixOject.entrySet().stream().filter(json -> json.getValue().isJsonPrimitive()).forEach( entry -> prefixes.put(entry.getKey(), entry.getValue().getAsString()));
		}
	}
	
	private static void addLanguageToModel(Model model, String defaultLanguage) {
		List<Statement> statements = model.listStatements(null, null, (RDFNode) null).toList();
		for (int index = 0; index < statements.size(); index++) {
			Statement statement = statements.get(index);
			RDFNode objectNode = statement.getObject();
			if (objectNode.isLiteral()) {
				Literal literal = objectNode.asLiteral();
				boolean coditionTypes = literal.getDatatypeURI().equals(XSD.normalizedString.toString()) || literal.getDatatypeURI().equals(XSD.xstring.toString()) || literal.getDatatypeURI().isEmpty();
				if( coditionTypes && literal.getLanguage().isEmpty()) {
					model.remove(statement);
					Literal newLiteral = model.createLiteral(literal.getValue().toString(), defaultLanguage);
					model.add(statement.getSubject(), statement.getPredicate(), newLiteral);
				}	
			}
		}

	}
	
	
	// -- Serialise methods

	public static List<Thing> fromRDF(Model model) throws SchemaValidationException {
		List<Thing> things = new ArrayList<>();
		
		List<Resource> thingSubjectsWithSecurity = model.listSubjectsWithProperty(ResourceFactory.createProperty("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")).toList();
		List<Resource> thingSubjects = model.listSubjectsWithProperty(ResourceFactory.createProperty("https://www.w3.org/2019/wot/td#securityDefinitions")).toList();
		thingSubjects.retainAll(thingSubjectsWithSecurity);
		
		if(thingSubjects.isEmpty()) {
			throw new SchemaValidationException("Provided RDF data lacks URIs that are Things, subjects with mandatory properties <https://www.w3.org/2019/wot/td#hasSecurityConfiguration> and/or <https://www.w3.org/2019/wot/td#securityDefinitions> are missing");
		}else {
			for(int index=0; index < thingSubjects.size(); index++) {
				Thing tmpThing = fromRDF(model, thingSubjects.get(index));
				if(tmpThing!=null)
					things.add(tmpThing);
			}
		}

		return things;
	}
	
	private static JsonNode tdContext;
	static {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			tdContext = objectMapper.readTree("{\"value\" :\"https://www.w3.org/2019/wot/td/v1\"}").get("value");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static List<JsonNode> prepareContext(Model model){
		List<JsonNode> context = new ArrayList<>();
		context.add(tdContext);
		ObjectMapper objectMapper = new ObjectMapper();
		
		Set<Entry<String,String>> prefixes = model.getNsPrefixMap().entrySet();
		for(Entry<String,String> prefix : prefixes) {
			try {
				JsonObject contextElement = new JsonObject();
				contextElement.addProperty(prefix.getKey(), prefix.getValue());
				context.add(objectMapper.readTree(contextElement.toString()));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return context;
	}
	

	
	public static Thing fromRDF(Model model, Resource thingSubject)  {
		Thing thing = null;
		List<JsonNode> context = prepareContext(model);
		String defaultLanguage = analyseDefaultLanguage(model);
		JsonNode lang = setLanguageContext(defaultLanguage);
		if(lang!=null)
			context.add(lang);

		try {
			Thing tmpThing = (Thing) Kehio.serializeClass(Thing.class, model, thingSubject);
			
			tmpThing.setContextValues(context);
			if(tmpThing.getTitles()!=null && !tmpThing.getTitles().isEmpty() && tmpThing.getTitle()==null) {
				if(tmpThing.getTitles().containsKey("en")) {
					tmpThing.setTitle(tmpThing.getTitles().get("en"));
				}else {
					tmpThing.setTitle(tmpThing.getTitles().values().iterator().next());
				}
			}
			if(tmpThing!=null) {
				thing = Thing.fromJson(tmpThing.toJson());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	
		return thing;
	}
	
	private static JsonNode setLanguageContext(String defaultLanguage) {
		JsonNode lang = null;
		ObjectMapper objectMapper = new ObjectMapper();
		if(defaultLanguage!=null) {
			try {
				JsonObject contextElement = new JsonObject();
				contextElement.addProperty("@language", defaultLanguage);
				lang = objectMapper.readTree(contextElement.toString());
			}catch(Exception e) {
				throw new IllegalArgumentException(e.toString());
			}
		}
		return lang;
	}

	/**
	 * Returns the default language of the dataset if exists, otherwise returns null
	 * @param model
	 * @return
	 */
	private static String analyseDefaultLanguage(Model model) {
		List<Statement> statements = model.listStatements(null, null, (RDFNode) null).toList();
		String defaultLanguage = null;
		boolean notExistDefaultLanguage = false;
		Model auxiliarModel = ModelFactory.createDefaultModel();
		List<Statement> toRemove = new ArrayList<>();
		for (int index = 0; index < statements.size(); index++) {
			Statement statement = statements.get(index);
			RDFNode objectNode = statement.getObject();
			if (objectNode.isLiteral()) {
				Literal literal = objectNode.asLiteral();
				boolean coditionTypes = literal.getDatatypeURI().equals(XSD.normalizedString.toString()) || literal.getDatatypeURI().equals(XSD.xstring.toString()) || literal.getDatatypeURI().isEmpty();
				if( !literal.getLanguage().isEmpty()) {
					String language = literal.getLanguage();
					if(defaultLanguage==null)
						defaultLanguage = language;
					notExistDefaultLanguage = !language.equals(defaultLanguage);
					if(notExistDefaultLanguage)
						break;
					auxiliarModel.add(statement.getSubject(), statement.getPredicate(), model.createTypedLiteral(literal.getValue()));
					toRemove.add(statement);
				}else if( coditionTypes && literal.getLanguage().isEmpty()) {
					// Coming here means there is a literal xsd: string or normalised string that has no language
					notExistDefaultLanguage = true;
				}
			}
		}
		
		if(!notExistDefaultLanguage) {
			model.remove(toRemove);
			model.add(auxiliarModel);
		}else {
			defaultLanguage = null;
		}
		return defaultLanguage;
		
	}
}
