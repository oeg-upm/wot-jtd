package wot.jtd.model;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfDatatypeGroup;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfObjectGroup;
import kehio.annotations.RdfUrlMap;
import wot.jtd.JTD;
import wot.jtd.Vocabulary;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.interactions.ActionAffordance;
import wot.jtd.model.interactions.EventAffordance;
import wot.jtd.model.interactions.PropertyAffordance;
import wot.jtd.model.schemas.security.SecurityScheme;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#thing">Thing</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#thing">Thing WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Thing extends AbstractRdfObject{
	
	// -- Attributes
	
	@JsonRawValue
	@NotEmpty(message="The value of @context must not be empty, it can be either a URI or an array cointatinig URIs or JSONs")
	@JsonProperty(Vocabulary.JSONLD_CONTEXT)
	protected Collection<String> context;
	
	@RdfDatatype("http://purl.org/dc/terms/title")
	@NotNull(message="the title of a td:Thing must not be null")
	protected String title;
	
	@RdfDatatype("http://purl.org/dc/terms/description")
	protected String description;
	
	@RdfDatatypeGroup(value="http://purl.org/dc/terms/title", byLang=true)
	protected Map<String,String> titles;
	
	@RdfDatatypeGroup(value="http://purl.org/dc/terms/description", byLang=true)
	protected Map<String,String> descriptions;
	
	@RdfObject("https://www.w3.org/2019/wot/td#versionInfo")
	protected VersionInfo version;
	
	@RdfDatatype(value="http://purl.org/dc/terms/created", datatype="http://www.w3.org/2001/XMLSchema#dateTime") 
	protected String created;
	
	@RdfDatatype(value="http://purl.org/dc/terms/modified", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	protected String modified;
	
	@RdfObject("https://www.w3.org/2019/wot/td#supportContact")
	protected URI support;
	
	@RdfObject("https://www.w3.org/2019/wot/td#base")
	protected URI base;
	
	@RdfObject("https://www.w3.org/2019/wot/td#hasLink")
	protected Collection<Link> links;
	
	@RdfObject(value="https://www.w3.org/2019/wot/td#hasSecurityConfiguration") // add base URI 
	@NotEmpty(message="security must not be blank, choose one from the security definitions")
	protected Collection<String> security;
	
	@RdfObjectGroup(value="https://www.w3.org/2019/wot/td#securityDefinitions", key="https://www.w3.org/2019/wot/td#scheme", 
					includeKey=false, 
					aliases={
						@RdfUrlMap(key="nosec", value="nosec_sc"),
						@RdfUrlMap(key="basic", value="basic_sc"),
						@RdfUrlMap(key="digest", value="digest_sc"),
						@RdfUrlMap(key="bearer", value="bearer_sc"),
						@RdfUrlMap(key="psk", value="psk_sc"),
						@RdfUrlMap(key="oauth2", value="oauth2_sc"),
						@RdfUrlMap(key="apikey", value="apikey_sc")
	})
	@NotEmpty
	protected Map<String, SecurityScheme> securityDefinitions;
	
	@RdfObject("https://www.w3.org/2019/wot/td#hasForm")
	protected Collection<Form> forms;
	
	@RdfObjectGroup(value="https://www.w3.org/2019/wot/td#hasPropertyAffordance", key="https://www.w3.org/2019/wot/td#name")
	protected Map<String,PropertyAffordance> properties;
	
	@RdfObjectGroup(value="https://www.w3.org/2019/wot/td#hasActionAffordance", key="https://www.w3.org/2019/wot/td#name")
	protected Map<String,ActionAffordance> actions;
	
	@RdfObjectGroup(value="https://www.w3.org/2019/wot/td#hasEventAffordance", key="https://www.w3.org/2019/wot/td#name")
	protected Map<String,EventAffordance> events;
	
	
	// -- Getters & Setters 
	
	public Collection<String> getContext() {
		return context;
	}
	
	@JsonSetter(Vocabulary.JSONLD_CONTEXT)
	public void setContextValues(Collection<JsonNode> context) {
		this.context = context.stream().map(JsonNode::toString).collect(Collectors.toList());
	}
	
	public void setContext(Collection<String> context) {
		this.context = context;
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

	public VersionInfo getVersion() {
		return version;
	}

	public void setVersion(VersionInfo version) {
		this.version = version;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public URI getSupport() {
		return support;
	}

	public void setSupport(URI support) {
		this.support = support;
	}

	public URI getBase() {
		return base;
	}

	public void setBase(URI base) {
		this.base = base;
	}

	public Collection<Link> getLinks() {
		return links;
	}

	public void setLinks(Collection<Link> links) {
		this.links = links;
	}

	public Collection<String> getSecurity() {
		return security;
	}

	public void setSecurity(Collection<String> security) {
		this.security = security;
	}

	public Map<String, SecurityScheme> getSecurityDefinitions() {
		return securityDefinitions;
	}

	public void setSecurityDefinitions(Map<String, SecurityScheme> securityDefinitions) {
		this.securityDefinitions = securityDefinitions;
		if(JTD.NORMALIZE_SECURITY_DEFINITIONS_SCHEMES) {
			Set<String> keys = this.securityDefinitions.keySet();
			for(String key:keys) {
				if(!JTD.SECURITY_DEFINITION_SCHEMES_PATTERN.matcher(key).matches()) {
					String newKey = key.replace("SecurityScheme", "").toLowerCase();
					newKey = newKey.substring(newKey.indexOf(':')+1) + "_sc";
					SecurityScheme scheme = this.securityDefinitions.remove(key);
					this.securityDefinitions.put(newKey, scheme);
				}
			}
		}
		
	}

	public Collection<Form> getForms() {
		return forms;
	}

	public void setForms(Collection<Form> forms) {
		this.forms = forms;
	}

	public Map<String, PropertyAffordance> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, PropertyAffordance> properties) {
		this.properties = properties;
	}

	public Map<String, ActionAffordance> getActions() {
		return actions;
	}

	public void setActions(Map<String, ActionAffordance> actions) {
		this.actions = actions;
	}

	public Map<String, EventAffordance> getEvents() {
		return events;
	}

	public void setEvents(Map<String, EventAffordance> events) {
		this.events = events;
	}

	
	// -- HasCode and equals


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
		result = partialHash(prime, result);
		result = superHashcode(result, prime);
		return result;
	}
	
	private int partialHash(int prime, int result) {
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((security == null) ? 0 : security.hashCode());
		result = prime * result + ((securityDefinitions == null) ? 0 : securityDefinitions.hashCode());
		result = prime * result + ((support == null) ? 0 : support.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Thing other = (Thing) obj;
		Boolean sameClass = sameSuperAttributes(other);
		// Map-based attributes
		sameClass &= sameMap(this.getActions(), other.getActions());
		sameClass &= sameMap(this.getEvents(), other.getEvents());
		sameClass &= sameMap(this.getProperties(), other.getProperties());
		sameClass &= sameMap(this.getSecurityDefinitions(), other.getSecurityDefinitions());
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		sameClass &= sameMap(this.getTitles(), other.getTitles());
		// Collection attributes
		sameClass &= sameCollections(this.getContext(), other.getContext());
		sameClass &= sameCollections(this.getSecurity(), other.getSecurity());
		sameClass &= sameCollections(this.getLinks(), other.getLinks());
		sameClass &= sameCollections(this.getForms(), other.getForms());
		// Primitive or Object attributes
		sameClass &= sameAttribute(this.getBase(), other.getBase());
		sameClass &= sameAttribute(this.getCreated(), other.getCreated());
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());
		sameClass &= sameAttribute(this.getTitle(), other.getTitle());
		sameClass &= sameAttribute(this.getModified(), other.getModified());
		sameClass &= sameAttribute(this.getSupport(), other.getSupport());
		sameClass &= sameAttribute(this.getVersion(), other.getVersion());
		
		return sameClass;
	}
	
	
	// -- Serialization and Deserialization
	
		/**
		 * This method transforms the current {@link Thing} object into a {@link JsonObject}.
		 * @return a {@link JsonObject}
		 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
		 */
		public JsonObject toJson() throws JsonProcessingException {
			return JTD.toJson(this);
		}

		/**
		 * This method instantiates and validates a {@link Thing} object from a {@link JsonObject}.
		 * @param json a Thing expressed as a {@link JsonObject}
		 * @return a valid {@link SecurityScheme}
		 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
		 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
		*/
		public static Thing fromJson(JsonObject json) throws IOException {
			return (Thing) JTD.instantiateFromJson(json.deepCopy(), Thing.class);
		}
	

	
	
	
}
