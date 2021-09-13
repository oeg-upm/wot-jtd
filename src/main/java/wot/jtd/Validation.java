package wot.jtd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.vocabulary.RDF;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class Validation {

	
	/**
	 * This method validates a Thing using a JSON Schema. The validation is provided under the form of a {@link JsonObject}, which will be empty if the thing has no errors.<br>
	 * The validation has been implemented with the library <a href="https://github.com/everit-org/json-schema">everit-org json-schema</a>, check their GitHub for more details.
	 * @param thing the {@link Thing} to be validated under its {@link JsonObject} form
	 * @param schema a schema to be applied to the {@link Thing}
	 * @return a {@link JsonObject} containing the validation report, this {@link JsonObject} will be empty if the provided {@link Thing} had no errors.
	 */
	public static JsonObject jsonSchemaValidation(JsonObject thing, String schema){
		JsonObject report = new JsonObject();
		try {
			JSONObject jsonObject = new JSONObject(new JSONTokener(new ByteArrayInputStream(thing.toString().getBytes())));
			JSONObject rawSchema = new JSONObject(new JSONTokener(new ByteArrayInputStream(schema.getBytes())));
			Schema schemaObj = SchemaLoader.load(rawSchema);
			schemaObj.validate(jsonObject);
		} catch (ValidationException e) {
			report = JTD.parseJson(e.toJSON().toString());
		}
		return report;
	}
	
	/**
	 * This method validates a Thing using a JSON Schema. The validation is provided under the form of a {@link JsonObject}, which will be empty if the thing has no errors.<br>
	 * The validation has been implemented with the library <a href="https://github.com/everit-org/json-schema">everit-org json-schema</a>, check their GitHub for more details.
	 * @param thing the {@link Thing} to be validated
	 * @param schema a schema to be applied to the {@link Thing}
	 * @return a {@link JsonObject} containing the validation report, this {@link JsonObject} will be empty if the provided {@link Thing} had no errors.
	 * @throws JsonProcessingException is thrown if an error occurred translating the {@link Thing} into {@link JsonObject}
	 */
	public static JsonObject jsonSchemaValidation(Thing thing, String schema) throws JsonProcessingException{
		JsonObject report = new JsonObject();
		try {
			JSONObject jsonObject = new JSONObject(new JSONTokener(new ByteArrayInputStream(thing.toJson().toString().getBytes())));
			JSONObject rawSchema = new JSONObject(new JSONTokener(new ByteArrayInputStream(schema.getBytes())));
			Schema schemaObj = SchemaLoader.load(rawSchema);
			schemaObj.validate(jsonObject);
		} catch (ValidationException e) {
			report = JTD.parseJson(e.toJSON().toString());
		}
		return report;
	}
	
	private static final Resource THING_TYPE = ResourceFactory.createResource("https://www.w3.org/2019/wot/td#Thing");
	/**
	 * This method validates a Thing using <a href="https://www.w3.org/TR/shacl/">W3C SHACL shapes</a>. The validation is provided under the form of a {@link Model}.<br>
	 * @param thingId the resource identifying the {@link Thing}
	 * @param thing the {@link Thing} to be validated under its {@link Model} form (RDF)
	 * @param shape a SHACL shape to be applied to the {@link Thing}
	 * @return a {@link Model} containing the validation report
	 */
	public static Model shaclShapeValidation(Resource thingId, Model thing, Model shape) {
		Shapes shapes = Shapes.parse(shape.getGraph());
		Model modelAux = ModelFactory.createDefaultModel();
		modelAux.add(thing);
		modelAux.add(thingId, RDF.type, THING_TYPE);
		ValidationReport validationReport = ShaclValidator.get().validate(shapes, modelAux.getGraph());
		return validationReport.getModel();
	}
	
	/**
	 * This method validates a Thing using <a href="https://www.w3.org/TR/shacl/">W3C SHACL shapes</a>. The validation is provided under the form of a {@link Model}.<br>
	 * @param thingId the resource identifying the {@link Thing}
	 * @param thing the {@link Thing} to be validated under its {@link Model} form (RDF)
	 * @param shape a SHACL shape to be applied to the {@link Thing}
	 * @return a {@link Model} containing the validation report
	 */
	public static Model shaclShapeValidation(Resource thingId, Model thing, String shape) {
		Model shapeModel = ModelFactory.createDefaultModel();
		shapeModel.read(new ByteArrayInputStream(shape.getBytes()), null, "TURTLE");
		return shaclShapeValidation( thingId,  thing,  shapeModel);
	}
	
	/**
	  * This method validates a Thing using <a href="https://www.w3.org/TR/shacl/">W3C SHACL shapes</a>. The validation is provided under the form of a {@link Model}.<br>
	 * @param thing the {@link Thing} to be validated
	 * @param shape a SHACL shape to be applied to the {@link Thing}, its format must be Turtle
	 * @return a {@link Model} containing the validation report
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SchemaValidationException
	 */
	public static Model shaclShapeValidation(Thing thing, String shape) throws IllegalAccessException, ClassNotFoundException, URISyntaxException, IOException, SchemaValidationException {
		Resource thingId = ResourceFactory.createResource(thing.getId());
		Model thingRdf = JTD.toRDF(thing);
		Model shapeRdf = ModelFactory.createDefaultModel();
		shapeRdf.read(new ByteArrayInputStream(shape.getBytes()), null);
		return shaclShapeValidation(thingId, thingRdf, shapeRdf);
	}
	
//	/**
//	 * This method validates an instance a {@link Thing} object.
//	 * @param thing an instance of {@link Thing}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(Thing thing) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<Thing>> violations = validator.validate(thing);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new ThingValidationException(builder.toString());
//		}
//		// TODO: throw nested expcetions
//		
//		// TODO: validate form restrictions
//				// if PropertyAffordance readproperty and writeproperty
//				// if ActionAffordance invokeaction
//				// if EventAffordance subscribeevent
//	}
//	
//	
//	/**
//	 * This method validates an instance a {@link SecurityScheme} object.
//	 * @param securityScheme an instance of {@link SecurityScheme}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(SecurityScheme securityScheme) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<SecurityScheme>> violations = validator.validate(securityScheme);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new SecuritySchemeValidationException(builder.toString());
//		}
//
//	}
//	
//	/**
//	 * This method validates an instance of {@link PropertyAffordance}.
//	 * @param propertyAffordance an instance of {@link PropertyAffordance}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(PropertyAffordance propertyAffordance) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<PropertyAffordance>> violations = validator.validate(propertyAffordance);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new PropertyAffordanceValidationException(builder.toString());
//		}
//		if(propertyAffordance.getForms()!=null) {
//			for(Form form:propertyAffordance.getForms()) {
//				validate(form);
//			}
//		}
//		if(propertyAffordance.getUriVariables()!=null) {
//			List<DataSchema> schemas = new ArrayList<>(propertyAffordance.getUriVariables().values());
//			for(int index=0; index < schemas.size(); index++) {
//				DataSchema.validate(schemas.get(index));
//			}
//		}
//	}
//	
//	/**
//	 * This method validates an instance of {@link InteractionAffordance}.
//	 * @param interactionAffordance an instance of {@link InteractionAffordance}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(InteractionAffordance interactionAffordance) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<InteractionAffordance>> violations = validator.validate(interactionAffordance);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new InteractionAffordanceValidationException(builder.toString());
//		}
//		if(interactionAffordance.getForms()!=null) {
//			for(Form form: interactionAffordance.getForms()) {
//				validate(form);
//			}
//		}
//		if(interactionAffordance.getUriVariables()!=null) {
//			List<DataSchema> schemas = new ArrayList<>(interactionAffordance.getUriVariables().values());
//			for(int index=0; index < schemas.size(); index++) {
//				DataSchema.validate(schemas.get(index));
//			}
//		}
//	}
//	
//	/**
//	 * This method validates an instance of {@link EventAffordance}.
//	 * @param eventAffordance an instance of {@link EventAffordance}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(EventAffordance eventAffordance) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<EventAffordance>> violations = validator.validate(eventAffordance);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new EventAffordanceValidationException(builder.toString());
//		}
//		if(eventAffordance.getForms()!=null) {
//			for(Form form:eventAffordance.getForms()) {
//				validate(form);
//			}
//		}
//		if(eventAffordance.getUriVariables()!=null) {
//			List<DataSchema> schemas = new ArrayList<>(eventAffordance.getUriVariables().values());
//			for(int index=0; index < schemas.size(); index++) {
//				DataSchema.validate(schemas.get(index));
//			}
//		}
//		if(eventAffordance.getSubscription()!=null)
//			DataSchema.validate(eventAffordance.getSubscription());
//		if(eventAffordance.getData()!=null)
//			DataSchema.validate(eventAffordance.getData());
//		if(eventAffordance.getCancellation()!=null)
//			DataSchema.validate(eventAffordance.getCancellation());
//		
//	}
//	
//	/**
//	 * This method validates an instance of {@link ActionAffordance}.
//	 * @param actionAffordance an instance of {@link ActionAffordance}
//	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//	 */
//	public static void validate(ActionAffordance actionAffordance) throws SchemaValidationException {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<ActionAffordance>> violations = validator.validate(actionAffordance);
//		StringBuilder builder = new StringBuilder();
//		if(!violations.isEmpty()) {
//			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//			throw new ActionAffordanceValidationException(builder.toString());
//		}
//		if(actionAffordance.getForms()!=null) {
//			for(Form form: actionAffordance.getForms()) {
//				validate(form);
//			}
//		}
//		if(actionAffordance.getUriVariables()!=null) {
//			List<DataSchema> schemas = new ArrayList<>(actionAffordance.getUriVariables().values());
//			for(int index=0; index < schemas.size(); index++) {
//				DataSchema.validate(schemas.get(index));
//			}
//		}
//	}
//	
//		// -- Validation methods
//	
//		/**
//		 * This method validates an instance of {@link VersionInfo}.
//		 * @param versionInfo an instance of {@link VersionInfo}
//		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//		 */
//		public static void validate(VersionInfo versionInfo) throws SchemaValidationException {
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Validator validator = factory.getValidator();
//			Set<ConstraintViolation<VersionInfo>> violations = validator.validate(versionInfo);
//			if(!violations.isEmpty()) {
//				StringBuilder builder = new StringBuilder();
//				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//				throw new VersionInfoValidationException(builder.toString());
//			}
//		}
//		
//		/**
//		 * This method validates an instance of {@link Link}.
//		 * @param link an instance of {@link Link}
//		 * @throws SchemaValidationException  this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//		 */
//		public static void validate(Link link) throws SchemaValidationException {
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Validator validator = factory.getValidator();
//			Set<ConstraintViolation<Link>> violations = validator.validate(link);
//			if(!violations.isEmpty()) {
//				StringBuilder builder = new StringBuilder();
//				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//				throw new LinkValidationException(builder.toString());
//			}
//		}
//		
//		/**
//		 * This method validates an instance a {@link Form} object.
//		 * @param form an instance of {@link Form}
//		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//		 */
//		public static void validate(Form form) throws SchemaValidationException {
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Validator validator = factory.getValidator();
//			Set<ConstraintViolation<Form>> violations = validator.validate(form);
//			StringBuilder builder = new StringBuilder();
//			if(!violations.isEmpty()) {
//				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//				throw new FormValidationException(builder.toString());
//			}
//			// TODO: check that op is one of: readproperty, writeproperty, observeproperty, unobserveproperty, invokeaction, subscribeevent, unsubscribeevent, readallproperties, writeallproperties, readmultipleproperties, or writemultiplepropertie
//			// TODO: add to the documentation that OP can be only one of these values ==> transform op in enum?
//			// TODO: check that methodName is one REST operation, check if there are more
//			if(form.getResponse()!=null)
//				validate(form.getResponse());
//		}
//		
//
//		/**
//		 * This method validates an instance of {@link ExpectedResponse}.
//		 * @param expectedResponse an instance of {@link ExpectedResponse}
//		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//		 */
//		public static void validate(ExpectedResponse expectedResponse) throws SchemaValidationException {
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Validator validator = factory.getValidator();
//			Set<ConstraintViolation<ExpectedResponse>> violations = validator.validate(expectedResponse);
//			if(!violations.isEmpty()) {
//				StringBuilder builder = new StringBuilder();
//				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//				throw new ExpectedResponseValidationException(builder.toString());
//			}
//		}
//		
//		/**
//		 * This method validates an instance of {@link DataSchema}.
//		 * @param dataSchema an instance of {@link DataSchema}
//		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
//		 */
//		public static void validate(DataSchema dataSchema) throws SchemaValidationException {
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Validator validator = factory.getValidator();
//			Set<ConstraintViolation<DataSchema>> violations = validator.validate(dataSchema);
//			StringBuilder builder = new StringBuilder();
//			if(!violations.isEmpty()) {
//				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
//				throw new DataSchemaValidationException(builder.toString());
//			}
//			// TODO: validate nested objects
//			// TODO: validate other restrictions
//		}
//		
}
