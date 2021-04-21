package kehio.models.test;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfId;

public class ORM4RDF1DTTest {
		
	@RdfId
	public String id;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Integer attrInteger;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public String attrString;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Boolean attrBool;
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ID: ").append(id).append("\n");
		builder.append("AttrInt: ").append(attrInteger).append("\n");
		builder.append("AttrStr: ").append(attrString).append("\n");
		builder.append("AttrBool: ").append(attrBool).append("\n");
		return builder.toString();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getAttrInteger() {
		return attrInteger;
	}


	public void setAttrInteger(Integer attrInteger) {
		this.attrInteger = attrInteger;
	}


	public String getAttrString() {
		return attrString;
	}


	public void setAttrString(String attrString) {
		this.attrString = attrString;
	}


	public Boolean getAttrBool() {
		return attrBool;
	}


	public void setAttrBool(Boolean attrBool) {
		this.attrBool = attrBool;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrBool == null) ? 0 : attrBool.hashCode());
		result = prime * result + ((attrInteger == null) ? 0 : attrInteger.hashCode());
		result = prime * result + ((attrString == null) ? 0 : attrString.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ORM4RDF1DTTest))
			return false;
		ORM4RDF1DTTest other = (ORM4RDF1DTTest) obj;
		if (attrBool == null) {
			if (other.attrBool != null)
				return false;
		} else if (!attrBool.equals(other.attrBool))
			return false;
		if (attrInteger == null) {
			if (other.attrInteger != null)
				return false;
		} else if (!attrInteger.equals(other.attrInteger))
			return false;
		if (attrString == null) {
			if (other.attrString != null)
				return false;
		} else if (!attrString.equals(other.attrString))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
}
