package wot.jtd.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeContainer;
import kehio.annotations.done.RdfObjectCollection;
import wot.jtd.JTD;
import wot.jtd.exception.InteractionAffordanceValidationException;
import wot.jtd.exception.SchemaValidationException;


/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#interactionaffordance">InteractionAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#interactionaffordance">InteractionAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractionAffordance extends AbstractJTDObject{

	// -- attributes
	@RdfObjectCollection("https://www.w3.org/2019/wot/td#hasForm")
	@NotEmpty(message="'forms' in any InteractionAffordance (InteractionAffordance, ActionAffordance, or EventAffordance) must not be empty")
	protected Collection<Form> forms;
	@RdfDatatype("http://purl.org/dc/terms/title")
	protected String title;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/title", byLang=true)
	protected Map<String,String> titles;
	@RdfDatatype("http://purl.org/dc/terms/description")
	protected String description;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/description", byLang=true)
	protected Map<String,String> descriptions;
	// TODO:
	protected Map<String,DataSchema> uriVariables;
	
	// -- static constructors and validation method
	
		/**
		 * This method creates a validated instance of {@link InteractionAffordance}.
		 * @param forms am array with valid {@link Form}
		 * @return an instantiated and validated  {@link InteractionAffordance}
		 * @throws SchemaValidationException  this exception is thrown when the syntax of the Thing Description as ORM is incorrect
		 */
		public static InteractionAffordance create(Collection<Form> forms) throws SchemaValidationException {
			// Create form
			InteractionAffordance interactionAffordance = new InteractionAffordance();
			interactionAffordance.setForms(forms);
			// Validate created form
			validate(interactionAffordance);
			return interactionAffordance;
		}
		
		/**
		 * This method validates an instance of {@link InteractionAffordance}.
		 * @param interactionAffordance an instance of {@link InteractionAffordance}
		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
		 */
		public static void validate(InteractionAffordance interactionAffordance) throws SchemaValidationException {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<InteractionAffordance>> violations = validator.validate(interactionAffordance);
			StringBuilder builder = new StringBuilder();
			if(!violations.isEmpty()) {
				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
				throw new InteractionAffordanceValidationException(builder.toString());
			}
			if(interactionAffordance.getForms()!=null) {
				for(Form form: interactionAffordance.getForms()) {
					Form.validate(form);
				}
			}
			if(interactionAffordance.getUriVariables()!=null) {
				List<DataSchema> schemas = new ArrayList<>(interactionAffordance.getUriVariables().values());
				for(int index=0; index < schemas.size(); index++) {
					DataSchema.validate(schemas.get(index));
				}
			}
		}
		
		// -- serialization and deserialization
		
		/**
		 * This method transforms the current {@link InteractionAffordance} object into a {@link JsonObject}.
		 * @return a {@link JsonObject}
		 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
		 */
		public JsonObject toJson() throws JsonProcessingException {
			return JTD.toJson(this);
		}
		
		/**
		 * This method instantiates and validates a {@link InteractionAffordance} object from a {@link JsonObject}.
		 * @param json a InteractionAffordance expressed as a {@link JsonObject}
		 * @return a valid {@link InteractionAffordance}
		 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
		 */
		public static InteractionAffordance fromJson(JsonObject json) throws IOException, SchemaValidationException {
			InteractionAffordance interactionAffordance = (InteractionAffordance) JTD.instantiateFromJson(json, InteractionAffordance.class);
			validate(interactionAffordance);
			return interactionAffordance;
		}
		
	
	// -- getters and setters
	
	public Collection<Form> getForms() {
		return forms;
	}

	public void setForms(Collection<Form> forms) {
		this.forms = forms;
	}

	@Override
	public Collection<String> getType() {
		return type;
	}

	@Override
	public void setType(Collection<String> type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getTitles() {
		return titles;
	}

	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	public Map<String, DataSchema> getUriVariables() {
		return uriVariables;
	}

	public void setUriVariables(Map<String, DataSchema> uriVariables) {
		this.uriVariables = uriVariables;
	}
	
	// -- hashcode and equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + ((uriVariables == null) ? 0 : uriVariables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof InteractionAffordance))
			return false;
		InteractionAffordance other = (InteractionAffordance) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (descriptions == null) {
			if (other.descriptions != null)
				return false;
		} else if (!descriptions.equals(other.descriptions))
			return false;
		if (forms == null) {
			if (other.forms != null)
				return false;
		} else if (!forms.equals(other.forms))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (titles == null) {
			if (other.titles != null)
				return false;
		} else if (!titles.equals(other.titles))
			return false;
		if (uriVariables == null) {
			if (other.uriVariables != null)
				return false;
		} else if (!uriVariables.equals(other.uriVariables))
			return false;
		return true;
	}
	


	
	




	
	
	

}
