package wot.jtd.model.interactions;

import java.util.Collection;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfDatatypeGroup;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfObjectGroup;
import wot.jtd.model.AbstractRdfObject;
import wot.jtd.model.Form;
import wot.jtd.model.schemas.data.DataSchema;

/**
 * This class implements the object <a href=
 * "https://www.w3.org/TR/wot-thing-description/#interactionaffordance">InteractionAffordance</a>
 * from a Thing Description as specified in the Web of Things (WoT)
 * documentation.
 * <p>
 * 
 * @see <a href=
 *      "https://www.w3.org/TR/wot-thing-description/#interactionaffordance">InteractionAffordance
 *      WoT documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractionAffordance extends AbstractRdfObject {

	// -- Attributes: from InteractionAffordance

	@RdfObject("https://www.w3.org/2019/wot/td#hasForm")
	@NotEmpty(message = "'forms' in any InteractionAffordance (PropertyAffordance, ActionAffordance, or EventAffordance) must not be empty")
	protected Collection<Form> forms;

	@RdfDatatype("http://purl.org/dc/terms/title")
	protected String title;

	@RdfDatatypeGroup(value = "http://purl.org/dc/terms/title", byLang = true)
	protected Map<String, String> titles;

	@RdfDatatype("http://purl.org/dc/terms/description")
	protected String description;

	@RdfDatatypeGroup(value = "http://purl.org/dc/terms/description", byLang = true)
	protected Map<String, String> descriptions;

	@RdfObjectGroup(value = "https://www.w3.org/2019/wot/td#hasUriTemplateSchema", key = "https://www.w3.org/2019/wot/td#name", includeKey = true)
	protected Map<String, DataSchema> uriVariables;

	// -- Getters & Setters

	public Collection<Form> getForms() {
		return forms;
	}

	public void setForms(Collection<Form> forms) {
		this.forms = forms;
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

	// -- HashCode and equals
	
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
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		InteractionAffordance other = (InteractionAffordance) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameCollections(this.getForms(), other.getForms());
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		sameClass &= sameMap(this.getTitles(), other.getTitles());
		sameClass &= sameMap(this.getUriVariables(), other.getUriVariables());
		sameClass &= sameAttribute(this.getTitle(), other.getTitle());
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());

		return sameClass;
	}
	

}
