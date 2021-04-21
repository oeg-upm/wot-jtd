package wot.jtd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import wot.jtd.exception.ActionAffordanceValidationException;
import wot.jtd.exception.DataSchemaValidationException;
import wot.jtd.exception.EventAffordanceValidationException;
import wot.jtd.exception.ExpectedResponseValidationException;
import wot.jtd.exception.FormValidationException;
import wot.jtd.exception.InteractionAffordanceValidationException;
import wot.jtd.exception.LinkValidationException;
import wot.jtd.exception.PropertyAffordanceValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.exception.SecuritySchemeValidationException;
import wot.jtd.exception.ThingValidationException;
import wot.jtd.exception.VersionInfoValidationException;
import wot.jtd.model.ExpectedResponse;
import wot.jtd.model.Form;
import wot.jtd.model.Link;
import wot.jtd.model.Thing;
import wot.jtd.model.VersionInfo;
import wot.jtd.model.interactions.ActionAffordance;
import wot.jtd.model.interactions.EventAffordance;
import wot.jtd.model.interactions.InteractionAffordance;
import wot.jtd.model.interactions.PropertyAffordance;
import wot.jtd.model.schemas.data.DataSchema;
import wot.jtd.model.schemas.security.SecurityScheme;

public class Validation {

	
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
