package wot.jtd.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import kehio.annotations.RdfContainer;
import kehio.annotations.RdfId;
import kehio.annotations.RdfObject;
import kehio.annotations.RdfUrlMap;
import wot.jtd.Vocabulary;


public class AbstractRdfObject {

	
	@JsonProperty(Vocabulary.JSONLD_TYPE)
	@RdfObject(value="http://www.w3.org/1999/02/22-rdf-syntax-ns#type", base="https://www.w3.org/2019/wot/td#")
	protected Collection<String> type;
	
	@RdfId
	@JsonProperty("id")
	@JsonAlias(Vocabulary.JSONLD_ID)
	protected String id;
	
	@RdfContainer(ignore= {"https://www.w3.org/2019/wot/td#name", "https://www.w3.org/2019/wot/json-schema#propertyName"}, 
			prefixes= {@RdfUrlMap(key="htv", value="http://www.w3.org/2011/http#"),
					   @RdfUrlMap(key="", value="https://oeg-upm.github.io/wot-jtd/")
				},
			aliases={ //@RdfUrlMap(key="type", value="http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
					  @RdfUrlMap(key="@type", value="http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
			},
			identifiers={ "id"}
	)
	@JsonIgnore
	protected Map<String,Object> externalProperties = new HashMap<>();
	
	// -- Getters & Setters
	
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
	
	
	// ---
		
	@JsonAnySetter
	public void setExternalProperties(String name, Object value) {
		externalProperties.put(name, value);
	}
	
	@JsonAnyGetter
	public Map<String, Object> getExternalProperties() {
	    return externalProperties;
	}
	
	// -- Hash code & Equals
	
	// -- hash code sub-methods
	
	protected int superHashcode(int subResult, int prime) {
		int result = subResult;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((externalProperties == null) ? 0 : externalProperties.hashCode());
		return result;
	}
	
	// -- equals sub-methods
	
	protected boolean sameSuperAttributes(AbstractRdfObject abstractRdfObject) {
		Boolean sameAttributes = sameCollections(abstractRdfObject.getType(), abstractRdfObject.getType());
		sameAttributes &= sameAttribute(this.id, abstractRdfObject.getId());
		sameAttributes &= sameMap(this.externalProperties, abstractRdfObject.getExternalProperties());
		return sameAttributes;
	}
	
	protected boolean sameCollections(Collection<?> collection1, Collection<?> collection2) {
		Boolean sameCollections = collection1!=null && collection2!=null;
		if(sameCollections)
			sameCollections &= collection1.containsAll(collection2) && collection2.containsAll(collection1);
		sameCollections |= collection1==null && collection2==null;
		return sameCollections;
	}
	
	protected boolean sameMap(Map<?,?> map1, Map<?,?> map2) {
		Boolean sameMaps = map1!=null && map2!=null && map1.size() == map2.size() ;
		if(sameMaps) {
			Set<?> entries1 = map1.entrySet();
			Set<?> entries2 = map2.entrySet();
			sameMaps = sameCollections(entries1, entries2);
		}
		sameMaps |= map1==null && map2==null;
		return sameMaps;
	}
	
	protected boolean sameAttribute(Object attribute1, Object attribute2) {
		return (attribute1!=null && attribute2!=null && attribute1.equals(attribute2)) || (attribute1==null && attribute2==null);
	}
	
	
}
