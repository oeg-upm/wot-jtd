package wot.jtd.model;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import wot.jtd.JTD;
import wot.jtd.exception.DataSchemaValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema</a> (and its sub-clases) from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSchema extends AbstractJTDObject{

	// -- attributes
	
	@JsonProperty(Vocabulary.JSONLD_TYPE)
	private Collection<String> type;
	
	private String title;
	private Map<String,String> titles;
	private String description;
	private Map<String,String> descriptions;
	@Pattern(regexp = "array|object|number|integer|string|boolean|null", flags = Pattern.Flag.CASE_INSENSITIVE)
	@JsonProperty(Vocabulary.TYPE)
	private String schemaType;
	private String unit;
	private Collection<DataSchema> oneOf;
	
	@JsonRawValue
	@JsonProperty(Vocabulary.ENUM)
	private Collection<String> enumTypes;
	
	@JsonRawValue
	@JsonProperty(Vocabulary.CONS)
	private String cons; 
	
	private Boolean readOnly; // has default value
	private Boolean writeOnly; // has default value
	private String format;
	
	// array
	@JsonProperty(Vocabulary.ITEMS)
	private Collection<DataSchema> items; //compactable attribute
	private Integer minItems; // maxInclusive = 4294967295
	private Integer maxItems; // maxInclusive = 4294967295
	// boolean + string
	// number + integer
	private Number minimum;
	private Number maximum;
	// object
	private Map<String,DataSchema> properties;
	private Collection<String> required;

	
	
	// -- constructors and validation methods (static)
	/**
	 * This method creates a validated instance of {@link DataSchema}, if default values are enabled in {@link JTD} the {@link DataSchema} will have the attributes 'readOnly' and 'writeOnly' as false
	 * @return an instantiated and validated  {@link DataSchema}
	 * @throws SchemaValidationException 
	 */
	public static DataSchema create() throws SchemaValidationException {
		DataSchema dataSchema = new DataSchema();
		if(JTD.getDefaultValues()) {
			dataSchema.setReadOnly(false);
			dataSchema.setWriteOnly(false);
		}
		validate(dataSchema);
		return dataSchema;
	}
	
	/**
	 * This method validates an instance of {@link DataSchema}.
	 * @param actionAffordance an instance of {@link DataSchema}
	 * @throws SchemaValidationException
	 */
	public static void validate(DataSchema dataSchema) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<DataSchema>> violations = validator.validate(dataSchema);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new DataSchemaValidationException(builder.toString());
		}
		// TODO: validate nested objects
		// TODO: validate other restrictions
	}
	
	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link DataSchema} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link DataSchema} object from a {@link JsonObject}.
	 * @param json a ExpectedResponse expressed as a {@link JsonObject}
	 * @return a valid {@link DataSchema}
	 * @throws IOException
	 * @throws SchemaValidationException 
	 */
	public static DataSchema fromJson(JsonObject json) throws IOException, SchemaValidationException {
		DataSchema dataSchema = (DataSchema) JTD.instantiateFromJson(json, DataSchema.class);
		validate(dataSchema);
		return dataSchema;
	}
	
	
	// -- getters and setters

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
	public String getSchemaType() {
		return schemaType;
	}
	public void setSchemaType(String schemaType) {
		this.schemaType = schemaType;
	}
	public Object getCons() {
		return cons;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Collection<DataSchema> getOneOf() {
		return oneOf;
	}
	public void setOneOf(Collection<DataSchema> oneOf) {
		this.oneOf = oneOf;
	}
	
	public Collection<String> getEnumTypes() {
		return enumTypes;
	}
	
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public Boolean getWriteOnly() {
		return writeOnly;
	}
	public void setWriteOnly(Boolean writeOnly) {
		this.writeOnly = writeOnly;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Collection<DataSchema> getItems() {
		return items;
	}
	public void setItems(Collection<DataSchema> items) {
		this.items = items;
	}
	public Integer getMinItems() {
		return minItems;
	}
	public void setMinItems(Integer minItems) {
		this.minItems = minItems;
	}
	public Integer getMaxItems() {
		return maxItems;
	}
	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}
	public Number getMinimum() {
		return minimum;
	}
	public void setMinimum(Number minimum) {
		this.minimum = minimum;
	}

	public Number getMaximum() {
		return maximum;
	}

	public void setMaximum(Number maximum) {
		this.maximum = maximum;
	}

	@JsonSetter(Vocabulary.CONS)
	public void setCons(JsonNode cons) {
		this.cons = cons.toString();
	}
	@JsonSetter(Vocabulary.ENUM)
	public void setEnumTypes(List<JsonNode> enumTypes) {
		this.enumTypes = enumTypes.stream().map(elem -> elem.toString()).collect(Collectors.toList());
	}

	public Map<String, DataSchema> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, DataSchema> properties) {
		this.properties = properties;
	}
	public Collection<String> getRequired() {
		return required;
	}
	public void setRequired(Collection<String> required) {
		this.required = required;
	}

	// -- hashcode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cons == null) ? 0 : cons.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((enumTypes == null) ? 0 : enumTypes.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((maxItems == null) ? 0 : maxItems.hashCode());
		result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
		result = prime * result + ((minItems == null) ? 0 : minItems.hashCode());
		result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());
		result = prime * result + ((oneOf == null) ? 0 : oneOf.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((readOnly == null) ? 0 : readOnly.hashCode());
		result = prime * result + ((required == null) ? 0 : required.hashCode());
		result = prime * result + ((schemaType == null) ? 0 : schemaType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((writeOnly == null) ? 0 : writeOnly.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof DataSchema))
			return false;
		DataSchema other = (DataSchema) obj;
		if (cons == null) {
			if (other.cons != null)
				return false;
		} else if (!cons.equals(other.cons))
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
		if (enumTypes == null) {
			if (other.enumTypes != null)
				return false;
		} else if (!enumTypes.equals(other.enumTypes))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (maxItems == null) {
			if (other.maxItems != null)
				return false;
		} else if (!maxItems.equals(other.maxItems))
			return false;
		if (maximum == null) {
			if (other.maximum != null)
				return false;
		} else if (!maximum.equals(other.maximum))
			return false;
		if (minItems == null) {
			if (other.minItems != null)
				return false;
		} else if (!minItems.equals(other.minItems))
			return false;
		if (minimum == null) {
			if (other.minimum != null)
				return false;
		} else if (!minimum.equals(other.minimum))
			return false;
		if (oneOf == null) {
			if (other.oneOf != null)
				return false;
		} else if (!oneOf.equals(other.oneOf))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (readOnly == null) {
			if (other.readOnly != null)
				return false;
		} else if (!readOnly.equals(other.readOnly))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		if (schemaType == null) {
			if (other.schemaType != null)
				return false;
		} else if (!schemaType.equals(other.schemaType))
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
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (writeOnly == null) {
			if (other.writeOnly != null)
				return false;
		} else if (!writeOnly.equals(other.writeOnly))
			return false;
		return true;
	}
	



	
	
	
	
}
