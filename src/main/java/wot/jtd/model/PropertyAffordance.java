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

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeContainer;
import kehio.annotations.done.RdfObjectCollection;
import wot.jtd.JTD;
import wot.jtd.exception.PropertyAffordanceValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#propertyaffordance">PropertyAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#propertyaffordance">PropertyAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyAffordance extends DataSchema {

	// -- attributes
	@RdfObjectCollection("https://www.w3.org/2019/wot/td#hasForm")
	@NotEmpty(message="'forms' in any InteractionAffordance (PropertyAffordance, ActionAffordance, or EventAffordance) must not be empty")
	protected Collection<Form> forms;
	@RdfDatatype("http://purl.org/dc/terms/title")
	private String title;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/title", byLang=true)
	private Map<String,String> titles;
	@RdfDatatype("http://purl.org/dc/terms/description")
	private String description;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/description", byLang=true)
	private Map<String,String> descriptions;
	
	private Map<String,DataSchema> uriVariables;
	@RdfDatatype("https://www.w3.org/2019/wot/td#isObservable")
	private Boolean observable;
	
	// -- static constructors and validation method
	
	/**
	 * This method creates a validated instance of {@link PropertyAffordance}.
	 * @param forms am array with valid {@link Form}
	 * @return an instantiated and validated  {@link PropertyAffordance}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static PropertyAffordance create(Collection<Form> forms) throws SchemaValidationException {
		// Create form
		PropertyAffordance propertyAffordance = new PropertyAffordance();
		propertyAffordance.setForms(forms);
		// Validate created form
		validate(propertyAffordance);
		return propertyAffordance;
	}
	
	/**
	 * This method validates an instance of {@link PropertyAffordance}.
	 * @param propertyAffordance an instance of {@link PropertyAffordance}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static void validate(PropertyAffordance propertyAffordance) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PropertyAffordance>> violations = validator.validate(propertyAffordance);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new PropertyAffordanceValidationException(builder.toString());
		}
		if(propertyAffordance.getForms()!=null) {
			for(Form form:propertyAffordance.getForms()) {
				Form.validate(form);
			}
		}
		if(propertyAffordance.getUriVariables()!=null) {
			List<DataSchema> schemas = new ArrayList<>(propertyAffordance.getUriVariables().values());
			for(int index=0; index < schemas.size(); index++) {
				DataSchema.validate(schemas.get(index));
			}
		}
	}
	
	// -- serialization and deserialization
	
	/**
	 * This method transforms the current {@link PropertyAffordance} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	 */
	@Override
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link PropertyAffordance} object from a {@link JsonObject}.
	 * @param json a PropertyAffordance expressed as a {@link JsonObject}
	 * @return a valid {@link PropertyAffordance}
	 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	*/
	public static PropertyAffordance fromJson(JsonObject json) throws SchemaValidationException, IOException {
		PropertyAffordance propertyAffordance = (PropertyAffordance) JTD.instantiateFromJson(json, PropertyAffordance.class);
		validate(propertyAffordance);
		return propertyAffordance;
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
	

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Map<String, String> getTitles() {
		return titles;
	}

	@Override
	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Map<String, String> getDescriptions() {
		return descriptions;
	}

	@Override
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	public Map<String, DataSchema> getUriVariables() {
		return uriVariables;
	}

	public void setUriVariables(Map<String, DataSchema> uriVariables) {
		this.uriVariables = uriVariables;
	}

	public Boolean getObservable() {
		return observable;
	}

	public void setObservable(Boolean observable) {
		this.observable = observable;
	}

	// hasCode and equals 
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
		result = prime * result + ((observable == null) ? 0 : observable.hashCode());
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
		if (!(obj instanceof PropertyAffordance))
			return false;
		PropertyAffordance other = (PropertyAffordance) obj;
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
		if (observable == null) {
			if (other.observable != null)
				return false;
		} else if (!observable.equals(other.observable))
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
