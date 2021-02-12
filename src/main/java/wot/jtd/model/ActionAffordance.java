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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import wot.jtd.JTD;
import wot.jtd.exception.ActionAffordanceValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#actionaffordance">ActionAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#actionaffordance">ActionAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionAffordance extends AbstractJTDObject{

		// -- attributes
	
		@NotEmpty(message="'forms' in any InteractionAffordance (PropertyAffordance, ActionAffordance, or EventAffordance) must not be empty")
		private Collection<Form> forms;
		@JsonProperty(Vocabulary.JSONLD_TYPE)
		private Collection<String> type;
		private String title;
		private Map<String,String> titles;
		private String description;
		private Map<String,String> descriptions;
		private Map<String,DataSchema> uriVariables;
		
		// specific attributes of ActionAffordance
		private DataSchema input;
		private DataSchema output;
		private Boolean safe; // has default value
		private Boolean idempotent; // has default value
		
		// -- static constructors and validation method

		/**
		 * This method creates a validated instance of {@link ActionAffordance}, if default values are enabled in {@link JTD} the {@link ActionAffordance} will have as the attributes 'safe' and 'idempotent' as false.
		 * @param forms am array with valid {@link Forms}
		 * @return an instantiated and validated  {@link ActionAffordance}
		 * @throws SchemaValidationException 
		 */
		public static ActionAffordance create(Collection<Form> forms) throws SchemaValidationException {
			// create action affordance
			ActionAffordance actionAffordance = new ActionAffordance();
			actionAffordance.setForms(forms);
			if(JTD.getDefaultValues()) {
				actionAffordance.setSafe(false);
				actionAffordance.setIdempotent(false);
			}
			// validate
			validate(actionAffordance);
			return actionAffordance;
		}
		
		/**
		 * This method validates an instance of {@link ActionAffordance}.
		 * @param actionAffordance an instance of {@link ActionAffordance}
		 * @throws SchemaValidationException 
		 */
		public static void validate(ActionAffordance actionAffordance) throws SchemaValidationException {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<ActionAffordance>> violations = validator.validate(actionAffordance);
			StringBuilder builder = new StringBuilder();
			if(!violations.isEmpty()) {
				violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
				throw new ActionAffordanceValidationException(builder.toString());
			}
			if(actionAffordance.getForms()!=null) {
				for(Form form: actionAffordance.getForms()) {
					Form.validate(form);
				}
			}
			if(actionAffordance.getUriVariables()!=null) {
				List<DataSchema> schemas = new ArrayList<>(actionAffordance.getUriVariables().values());
				for(int index=0; index < schemas.size(); index++) {
					DataSchema.validate(schemas.get(index));
				}
			}
		}
		
		// -- serialization and deserialization
		/**
		 * This method transforms the current {@link ActionAffordance} object into a {@link JsonObject}.
		 * @return a {@link JsonObject}
		 * @throws JsonProcessingException
		 */
		public JsonObject toJson() throws JsonProcessingException {
			return JTD.toJson(this);
		}
		
		/**
		 * This method instantiates and validates a {@link ActionAffordance} object from a {@link JsonObject}.
		 * @param json a ActionAffordance expressed as a {@link JsonObject}
		 * @return a valid {@link ActionAffordance}
		 * @throws IOException
		 * @throws SchemaValidationException 
		 */
		public static ActionAffordance fromJson(JsonObject json) throws IOException, SchemaValidationException {
			ActionAffordance actionAffordance = (ActionAffordance) JTD.instantiateFromJson(json, ActionAffordance.class);
			validate(actionAffordance);
			return actionAffordance;
		}
		
		// -- getters and setters
		
		public Collection<Form> getForms() {
			return forms;
		}

		public DataSchema getInput() {
			return input;
		}

		public void setInput(DataSchema input) {
			this.input = input;
		}

		public DataSchema getOutput() {
			return output;
		}

		public void setOutput(DataSchema output) {
			this.output = output;
		}

		public Boolean getSafe() {
			return safe;
		}

		public void setSafe(Boolean safe) {
			this.safe = safe;
		}

		public Boolean getIdempotent() {
			return idempotent;
		}

		public void setIdempotent(Boolean idempotent) {
			this.idempotent = idempotent;
		}

		public void setForms(Collection<Form> forms) {
			this.forms = forms;
		}

		public Collection<String> getType() {
			return type;
		}

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
		
		// -- hashCode and equals

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
			result = prime * result + ((forms == null) ? 0 : forms.hashCode());
			result = prime * result + ((idempotent == null) ? 0 : idempotent.hashCode());
			result = prime * result + ((input == null) ? 0 : input.hashCode());
			result = prime * result + ((output == null) ? 0 : output.hashCode());
			result = prime * result + ((safe == null) ? 0 : safe.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			result = prime * result + ((titles == null) ? 0 : titles.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((uriVariables == null) ? 0 : uriVariables.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof ActionAffordance))
				return false;
			ActionAffordance other = (ActionAffordance) obj;
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
			if (idempotent == null) {
				if (other.idempotent != null)
					return false;
			} else if (!idempotent.equals(other.idempotent))
				return false;
			if (input == null) {
				if (other.input != null)
					return false;
			} else if (!input.equals(other.input))
				return false;
			if (output == null) {
				if (other.output != null)
					return false;
			} else if (!output.equals(other.output))
				return false;
			if (safe == null) {
				if (other.safe != null)
					return false;
			} else if (!safe.equals(other.safe))
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
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (uriVariables == null) {
				if (other.uriVariables != null)
					return false;
			} else if (!uriVariables.equals(other.uriVariables))
				return false;
			return true;
		}

		
		
		
		
		
	

		
		
		
}
