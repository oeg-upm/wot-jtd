package wot.jtd.model.interactions;

import java.util.Collection;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfObjectGroup;
import wot.jtd.model.Form;
import wot.jtd.model.schemas.data.DataSchema;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#propertyaffordance">PropertyAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#propertyaffordance">PropertyAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyAffordance extends DataSchema {

	// -- Attributes: from InteractionAffordance

	@RdfObject("https://www.w3.org/2019/wot/td#hasForm")
	@NotEmpty(message="'forms' in any InteractionAffordance (PropertyAffordance, ActionAffordance, or EventAffordance) must not be empty")
	protected Collection<Form> forms;
	
	@RdfObjectGroup(value="https://www.w3.org/2019/wot/td#hasUriTemplateSchema", key="https://www.w3.org/2019/wot/td#name", includeKey=true)
	protected Map<String,DataSchema> uriVariables;
	
	@RdfDatatype("https://www.w3.org/2019/wot/td#isObservable")
	protected Boolean observable; // extended attribute from InteractionAffordance
	
	// -- Getters & Setters
	
	public Collection<Form> getForms() {
		return forms;
	}

	public void setForms(Collection<Form> forms) {
		this.forms = forms;
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

	// HasCode and Equals 
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = computeHashCode(result, prime);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + ((uriVariables == null) ? 0 : uriVariables.hashCode());
		result = prime * result + ((observable == null) ? 0 : observable.hashCode());

		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		PropertyAffordance other = (PropertyAffordance) obj;
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
		sameClass &= sameCollections(this.getForms(), other.getForms());
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		sameClass &= sameMap(this.getTitles(), other.getTitles());
		sameClass &= sameMap(this.getUriVariables(), other.getUriVariables());
		sameClass &= sameAttribute(this.getTitle(), other.getTitle());
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());
		sameClass &= sameAttribute(this.getObservable(), other.getObservable());

		return sameClass;
	}

	
}
