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
import wot.jtd.exception.EventAffordanceValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;


/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#eventaffordance">EventAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#eventaffordance">EventAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventAffordance extends AbstractJTDObject{

	// -- attributes

	@NotEmpty(message = "'forms' in any InteractionAffordance (PropertyAffordance, ActionAffordance, or EventAffordance) must not be empty")
	private Collection<Form> forms;
	@JsonProperty(Vocabulary.JSONLD_TYPE)
	private Collection<String> type;
	private String title;
	private Map<String, String> titles;
	private String description;
	private Map<String, String> descriptions;
	private Map<String, DataSchema> uriVariables;

	// specific attributes of EvenAffordance
	private DataSchema subscription;
	private DataSchema data;
	private DataSchema cancellation;
	
	// -- static constructors and validation method
	
	/**
	 * This method creates a validated instance of {@link EventAffordance}.
	 * @param forms am array with valid {@link Forms}
	 * @return an instantiated and validated  {@link EventAffordance}
	 * @throws SchemaValidationException 
	 */
	public static EventAffordance create(Collection<Form> forms) throws SchemaValidationException {
		// create action affordance
		EventAffordance eventAffordance = new EventAffordance();
		eventAffordance.setForms(forms);
		// validate
		validate(eventAffordance);
		return eventAffordance;
	}
	
	/**
	 * This method validates an instance of {@link EventAffordance}.
	 * @param eventAffordance an instance of {@link EventAffordance}
	 * @throws SchemaValidationException
	 */
	public static void validate(EventAffordance eventAffordance) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<EventAffordance>> violations = validator.validate(eventAffordance);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new EventAffordanceValidationException(builder.toString());
		}
		if(eventAffordance.getForms()!=null) {
			for(Form form:eventAffordance.getForms()) {
				Form.validate(form);
			}
		}
		if(eventAffordance.getUriVariables()!=null) {
			List<DataSchema> schemas = new ArrayList<>(eventAffordance.getUriVariables().values());
			for(int index=0; index < schemas.size(); index++) {
				DataSchema.validate(schemas.get(index));
			}
		}
		if(eventAffordance.getSubscription()!=null)
			DataSchema.validate(eventAffordance.getSubscription());
		if(eventAffordance.getData()!=null)
			DataSchema.validate(eventAffordance.getData());
		if(eventAffordance.getCancellation()!=null)
			DataSchema.validate(eventAffordance.getCancellation());
		
	}
	
	
	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link EventAffordance} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link EventAffordance} object from a {@link JsonObject}.
	 * @param json a EventAffordance expressed as a {@link JsonObject}
	 * @return a valid {@link EventAffordance}
	 * @throws IOException
	 * @throws SchemaValidationException
	 */
	public static EventAffordance fromJson(JsonObject json) throws IOException, SchemaValidationException {
		EventAffordance eventAffordance = (EventAffordance) JTD.instantiateFromJson(json, EventAffordance.class);
		validate(eventAffordance);
		return eventAffordance;
	}
	
	
	// -- getters and setters

	public Collection<Form> getForms() {
		return forms;
	}

	public void setForms(Collection<Form> forms) {
		this.forms = forms;
	}

	public Collection<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
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

	public DataSchema getSubscription() {
		return subscription;
	}

	public void setSubscription(DataSchema subscription) {
		this.subscription = subscription;
	}

	public DataSchema getData() {
		return data;
	}

	public void setData(DataSchema data) {
		this.data = data;
	}

	public DataSchema getCancellation() {
		return cancellation;
	}

	public void setCancellation(DataSchema cancellation) {
		this.cancellation = cancellation;
	}

	// -- hashcode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cancellation == null) ? 0 : cancellation.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
		result = prime * result + ((subscription == null) ? 0 : subscription.hashCode());
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
		if (!(obj instanceof EventAffordance))
			return false;
		EventAffordance other = (EventAffordance) obj;
		if (cancellation == null) {
			if (other.cancellation != null)
				return false;
		} else if (!cancellation.equals(other.cancellation))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
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
		if (subscription == null) {
			if (other.subscription != null)
				return false;
		} else if (!subscription.equals(other.subscription))
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
