package wot.jtd.model.schemas.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfDatatypeGroup;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfUrlMap;
import wot.jtd.Vocabulary;
import wot.jtd.model.AbstractRdfObject;

/**
 * This class implements the object <a href=
 * "https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema</a> from a Thing Description as specified in the Web of Things
 * (WoT) documentation.
 * <p>
 * 
 * @see <a href=
 *      "https://www.w3.org/TR/wot-thing-description/#dataschema">DataSchema WoT
 *      documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDataSchema extends AbstractRdfObject {

	// -- Attributes

		@RdfDatatype("http://purl.org/dc/terms/title")
		protected String title;

		@RdfDatatypeGroup(value = "http://purl.org/dc/terms/title", byLang = true)
		protected Map<String, String> titles;

		@RdfDatatype("http://purl.org/dc/terms/decription")
		protected String description;

		@RdfDatatypeGroup(value = "http://purl.org/dc/terms/decription", byLang = true)
		protected Map<String, String> descriptions;

		@RdfObject(value = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", strict = true, aliases = {
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#ObjectSchema", value = "object"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#ArraySchema", value = "array"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#BooleanSchema", value = "boolean"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#StringSchema", value = "string"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#NumberSchema", value = "number"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#IntegerSchema", value = "integer"),
				@RdfUrlMap(key = "https://www.w3.org/2019/wot/json-schema#NullSchema", value = "null") })
		@Pattern(regexp = "array|object|number|integer|string|boolean|null", flags = Pattern.Flag.CASE_INSENSITIVE)
		@JsonProperty(Vocabulary.JSONLD_TYPE_ALIAS)
		protected String schemaType;

		@RdfObject("http://schema.org/unitCode")
		protected String unit;

		@RdfObject("https://www.w3.org/2019/wot/json-schema#const")
		@JsonRawValue
		@JsonProperty(Vocabulary.CONS)
		protected String cons;

		@RdfObject("https://www.w3.org/2019/wot/json-schema#oneOf")
		protected Collection<DataSchema> oneOf;

		@RdfObject("https://www.w3.org/2019/wot/json-schema#enum")
		@JsonRawValue
		@JsonProperty(Vocabulary.ENUM)
		protected Collection<String> enumTypes;

		@RdfDatatype("https://www.w3.org/2019/wot/json-schema#readOnly")
		protected Boolean readOnly; // has default value

		@RdfDatatype("https://www.w3.org/2019/wot/json-schema#writeOnly")
		protected Boolean writeOnly; // has default value

		@RdfDatatype("https://www.w3.org/2019/wot/json-schema#format")
		protected String format;

		// -- Getters & Setters

		@Override
		public Collection<String> getType() {
			return type;
		}

		protected static final Map<String, String> JSON_SCHEMA_TYPES = new HashMap<>();
		static {
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#ObjectSchema", "object");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#ArraySchema", "array");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#BooleanSchema", "boolean");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#StringSchema", "string");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#NumberSchema", "number");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#IntegerSchema", "integer");
			JSON_SCHEMA_TYPES.put("https://www.w3.org/2019/wot/json-schema#NullSchema", "null");
		}

		@Override
		public void setType(Collection<String> type) {
			String removeType = null;
			Iterator<String> typeIterator = type.iterator();
			while (typeIterator.hasNext()) {
				String typeIterate = typeIterator.next();
				if (JSON_SCHEMA_TYPES.containsKey(typeIterate)) {
					removeType = typeIterate;
					break;
				}
			}
			type.remove(removeType);
			this.setSchemaType(JSON_SCHEMA_TYPES.get(removeType));
			if (!type.isEmpty())
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

		@JsonSetter(Vocabulary.CONS)
		public void setCons(JsonNode cons) {
			this.cons = cons.toString();
		}

		@JsonSetter(Vocabulary.ENUM)
		public void setEnumTypes(List<JsonNode> enumTypes) {
			this.enumTypes = enumTypes.stream().map(elem -> elem.toString()).collect(Collectors.toList());
		}

		// -- HashCode & Equals
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = computeHashCode(result, prime);
			result = superHashcode(result, prime);
			return result;
		}
		
		protected int computeHashCode(int resultInput, int prime) {
			int result = resultInput;
			result = prime * result + ((cons == null) ? 0 : cons.hashCode());
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
			result = prime * result + ((enumTypes == null) ? 0 : enumTypes.hashCode());
			result = prime * result + ((format == null) ? 0 : format.hashCode());
			result = prime * result + ((oneOf == null) ? 0 : oneOf.hashCode());
			result = prime * result + ((readOnly == null) ? 0 : readOnly.hashCode());
			result = prime * result + ((schemaType == null) ? 0 : schemaType.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			result = prime * result + ((titles == null) ? 0 : titles.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			result = prime * result + ((writeOnly == null) ? 0 : writeOnly.hashCode());
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

			return sameClass;
			
		}
	
}
