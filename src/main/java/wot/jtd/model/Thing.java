package wot.jtd.model;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.jena.rdf.model.Model;

import com.apicatalog.jsonld.JsonLdError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeCollection;
import kehio.annotations.done.RdfDatatypeContainer;
import kehio.annotations.done.RdfObject;
import kehio.annotations.done.RdfObjectCollection;
import kehio.annotations.done.RdfObjectContainer;
import wot.jtd.JTD;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.exception.ThingValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#thing">Thing</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#thing">Thing WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Thing extends AbstractJTDObject{
	
	// -- attributes
	@JsonRawValue
	@NotEmpty(message="The value of @context must not be empty, it can be either a URI or an array cointatinig URIs or JSONs")
	@JsonProperty(Vocabulary.JSONLD_CONTEXT)
	private Collection<String> context;
	@RdfDatatype("http://purl.org/dc/terms/title")
	@NotNull(message="the title of a td:Thing must not be null")
	private String title;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/title", byLang=true)
	private Map<String,String> titles;
	@RdfDatatype("http://purl.org/dc/terms/decription")
	private String description;
	@RdfDatatypeContainer(value="http://purl.org/dc/terms/description", byLang=true)
	private Map<String,String> descriptions;
	@RdfObject("https://www.w3.org/2019/wot/td#versionInfo")
	private VersionInfo version;
	@RdfDatatype(value="http://purl.org/dc/terms/created", datatype="http://www.w3.org/2001/XMLSchema#dateTime") 
	private String created;
	@RdfDatatype(value="http://purl.org/dc/terms/modified", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	private String modified;
	@RdfObject("https://www.w3.org/2019/wot/td#supportContact")
	private URI support;
	@RdfObject("https://www.w3.org/2019/wot/td#base")
	private URI base;
	@RdfObjectCollection("https://www.w3.org/2019/wot/td#hasLink")
	private Collection<Link> links;
	
	// TODO: elements in this array must be keys from the securityDefinitions
	@RdfObjectCollection("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")
	@NotEmpty(message="security must not be blank, choose one from the security definitions")
	private Collection<String> security;
	@RdfObjectContainer(value="https://www.w3.org/2019/wot/td#securityDefinitions", key="https://www.w3.org/2019/wot/td#scheme")
	@NotEmpty
	private Map<String, SecurityScheme> securityDefinitions;
	
	// TODO: validate these restrictions
		// if PropertyAffordance readproperty and writeproperty
		// if ActionAffordance invokeaction
		// if EventAffordance subscribeevent
	@RdfObjectCollection("https://www.w3.org/2019/wot/td#hasForm")
	private Collection<Form> forms;
	
	@RdfObjectContainer(value="https://www.w3.org/2019/wot/td#hasPropertyAffordance", key="https://www.w3.org/2019/wot/json-schema#propertyName")
	private Map<String,PropertyAffordance> properties;
	@RdfObjectContainer(value="https://www.w3.org/2019/wot/td#hasActionAffordance", key="https://www.w3.org/2019/wot/json-schema#propertyName")
	private Map<String,ActionAffordance> actions;
	@RdfObjectContainer(value="https://www.w3.org/2019/wot/td#hasEventAffordance", key="https://www.w3.org/2019/wot/json-schema#propertyName")
	private Map<String,EventAffordance> events;
	
	// -- static constructors and validation method
	
	// TODO: create another static contructor to change the context
	/**
	 * This method creates a validated instance of {@link Thing}.
	 * @param title a {@link String} title for the Thing
	 * @param security a {@link Collection} of security schemes that are keys in the map of 'securityDefinitions' parameter
	 * @param securityDefinitions a {@link Map} between security names and {@link SecurityScheme}
	 * @return an instantiated and validated  {@link Thing}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static Thing create(String title, Collection<String> security, Map<String, SecurityScheme> securityDefinitions) throws SchemaValidationException {
		// Create Thing
		Thing thing = new Thing();
		thing.setTitle(title);
		thing.setSecurity(security);
		thing.setSecurityDefinitions(securityDefinitions);
		// validate Thing
		validate(thing);
		return thing;
	}
	
	/**
	 * This method validates an instance a {@link Thing} object.
	 * @param thing an instance of {@link Thing}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static void validate(Thing thing) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Thing>> violations = validator.validate(thing);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new ThingValidationException(builder.toString());
		}
		// TODO: throw nested expcetions
	}

	
	// -- serialization and deserialization
	
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
	public static Thing fromJson(JsonObject json) throws IOException, SchemaValidationException {
		Thing thing = (Thing) JTD.instantiateFromJson(json.deepCopy(), Thing.class);
		validate(thing);
		return thing;
	}
	
	// -- getters and setters 
	
	public Collection<String> getContext() {
		return context;
	}
	
	@JsonSetter(Vocabulary.JSONLD_CONTEXT)
	public void setContext(Collection<JsonNode> context) {
		this.context = context.stream().map(elem -> elem.toString()).collect(Collectors.toList());
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
	
	@JsonIgnore
	public boolean getHasExternalProperties() {
		return this.hasExternalProperties;
	}
	

	
	/**
	 * This method translates the TDs into RDF and then compares their isomorphism to check if they are equivalent
	 * @param td a {@link JsonObject} TD
	 * @return a boolean value indicating if the provided Thing Description is equivalent (isomorphic) with the {@link Thing}
	 * @throws JsonLdError this exception is thrown if the {@link JsonObject} is not a correct Json-LD 1.1 semantics according to the WoT Thing Description
	 * @throws JsonProcessingException  this exception is thrown if the {@link JsonObject} is not a correct syntaxt Json-LD 1.1
	 */
	public boolean isEquivalent(JsonObject td) throws JsonProcessingException, JsonLdError {
		Model model = JTD.toRDF(this.toJson());
		Model tdModel = JTD.toRDF(td);
		return model.isIsomorphicWith(tdModel);
	}
	
	// -- hascode and equals

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
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
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Thing))
			return false;
		Thing other = (Thing) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
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
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (forms == null) {
			if (other.forms != null)
				return false;
		} else if (!forms.equals(other.forms))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (security == null) {
			if (other.security != null)
				return false;
		} else if (!security.equals(other.security))
			return false;
		if (securityDefinitions == null) {
			if (other.securityDefinitions != null)
				return false;
		} else if (!securityDefinitions.equals(other.securityDefinitions))
			return false;
		if (support == null) {
			if (other.support != null)
				return false;
		} else if (!support.equals(other.support))
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
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}


	

	
	
	
}
