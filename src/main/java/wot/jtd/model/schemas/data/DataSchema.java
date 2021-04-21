package wot.jtd.model.schemas.data;

import java.util.Collection;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfObjectGroup;
import wot.jtd.Vocabulary;

/**
 * This class implements the object <a href=
 * "https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema</a> from
 * a Thing Description as specified in the Web of Things (WoT) documentation.
 * <p>
 * 
 * @see <a href=
 *      "https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema WoT
 *      documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSchema extends AbstractDataSchema {

	// -- Attributes
	
	// String, Null & Boolean
	
	// Array

	@RdfObject("https://www.w3.org/2019/wot/json-schema#items")
	@JsonProperty(Vocabulary.ITEMS)
	protected Collection<DataSchema> items; // compactable attribute

	@RdfDatatype("https://www.w3.org/2019/wot/json-schema#minItems")
	protected Integer minItems; // maxInclusive = 4294967295

	@RdfDatatype("https://www.w3.org/2019/wot/json-schema#maxItems")
	protected Integer maxItems; // maxInclusive = 4294967295

	// Integer & Number

	@RdfDatatype("https://www.w3.org/2019/wot/json-schema#minimum")
	protected Number minimum;

	@RdfDatatype("https://www.w3.org/2019/wot/json-schema#maximum")
	protected Number maximum;

	// Object

	@RdfObjectGroup(value = "https://www.w3.org/2019/wot/json-schema#properties", key = "https://www.w3.org/2019/wot/json-schema#propertyName", includeKey = true 
			/*aliases = {
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#ObjectSchema", value = "object"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#ArraySchema", value = "array"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#BooleanSchema", value = "boolean"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#StringSchema", value = "string"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#NumberSchema", value = "number"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#IntegerSchema", value = "integer"),
			@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#NullSchema", value = "null") }*/
	)
	protected Map<String, DataSchema> properties;

	@RdfDatatype("https://www.w3.org/2019/wot/json-schema#required")
	protected Collection<String> required;

	// -- Getters & Setters

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

	// -- HashCode & Equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = computeHashCode(result, prime);
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((maxItems == null) ? 0 : maxItems.hashCode());
		result = prime * result + ((minItems == null) ? 0 : minItems.hashCode());

		result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
		result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());

		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((required == null) ? 0 : required.hashCode());
		
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		DataSchema other = (DataSchema) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		sameClass &= sameMap(this.getTitles(), other.getTitles());
		sameClass &= sameAttribute(this.getTitle(), other.getTitle());
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());
		sameClass &= sameAttribute(this.getSchemaType(), other.getSchemaType());
		sameClass &= sameAttribute(this.getCons(), other.getCons());
		sameClass &= sameAttribute(this.getFormat(), other.getFormat());
		sameClass &= sameAttribute(this.getReadOnly(), other.getReadOnly());
		sameClass &= sameAttribute(this.getUnit(), other.getUnit());
		sameClass &= sameAttribute(this.getWriteOnly(), other.getWriteOnly());
		sameClass &= sameCollections(this.getOneOf(), other.getOneOf());
		sameClass &= sameCollections(this.getEnumTypes(), other.getEnumTypes());

		sameClass &= sameCollections(this.getItems(), other.getItems());
		sameClass &= sameAttribute(this.getMaxItems(), other.getMaxItems());
		sameClass &= sameAttribute(this.getMinItems(), other.getMinItems());

		sameClass &= sameAttribute(this.getMaximum(), other.getMaximum());
		sameClass &= sameAttribute(this.getMinimum(), other.getMinimum());
		
		sameClass &= sameMap(this.getProperties(), other.getProperties());
		sameClass &= sameCollections(this.getRequired(), other.getRequired());

		return sameClass;
	}

}
