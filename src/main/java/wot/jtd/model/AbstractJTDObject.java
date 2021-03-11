package wot.jtd.model;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.rdf.model.Model;
import com.apicatalog.jsonld.JsonLdError;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import kehio.annotations.RdfContainer;
import kehio.annotations.done.RdfId;
import kehio.annotations.done.RdfObject;
import kehio.annotations.done.RdfObjectCollection;
import kehio.mapper.Kehio;
import wot.jtd.JTD;
import wot.jtd.vocabulary.Vocabulary;


public abstract class AbstractJTDObject {

	protected static final Logger LOGGER = Logger.getLogger(AbstractJTDObject.class.getName());
	static {
		LOGGER.setLevel(Level.WARNING);
	}
	
	// Shared common attributes
	@JsonProperty(Vocabulary.JSONLD_TYPE)
	@RdfObjectCollection("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
	protected Collection<String> type;
	
	@RdfId
	protected String id;
	

	public Collection<String> getType() {
		return type;
	}
	public void setType(Collection<String> type) {
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	// Any other property outside the standard
	@RdfContainer
	protected Map<String,Object> unknownProperties = new HashMap<>();
	
	@JsonIgnore
	protected Boolean hasExternalProperties = false;
	
	@JsonAnySetter
	public void setExternal(String name, Object value) {
        unknownProperties.put(name, value);
        hasExternalProperties = true;
        if(JTD.getShowExternalValuesWarnings())
        		LOGGER.log( Level.WARNING, getWariningMessage());
	}
	
	private String getWariningMessage() {
		StringBuilder message = new StringBuilder();
		try {
			String clazzName = this.getClass().getSimpleName();
			ObjectMapper mapper = new ObjectMapper();
			String jsonUnknownProperties = mapper.writeValueAsString(unknownProperties);
			message.append("Json elements outside the standard found in '").append(clazzName).append("' : ").append(jsonUnknownProperties);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return message.toString();
	}
	
	@JsonAnyGetter
	public Map<String,Object> getExternal() {
	    return unknownProperties;
	}
	
	
	

	
	// -- Hash code and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unknownProperties == null) ? 0 : unknownProperties.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractJTDObject other = (AbstractJTDObject) obj;
		if (unknownProperties == null) {
			if (other.unknownProperties != null)
				return false;
		} else if (!unknownProperties.equals(other.unknownProperties))
			return false;
		return true;
	}
	
	
}
