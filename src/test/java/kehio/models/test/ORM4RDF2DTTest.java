package kehio.models.test;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfId;

public class ORM4RDF2DTTest {
		
	@RdfId
	public String id;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Integer attrInteger;
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="http://www.w3.org/2001/XMLSchema#int")
	public String attrString1;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public String attrString2;
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ID: ").append(id).append("\n");
		builder.append("AttrInt: ").append(attrInteger).append("\n");
		builder.append("AttrStr: ").append(attrString1).append("\n");
		builder.append("AttrStr: ").append(attrString2).append("\n");
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


	public String getAttrString1() {
		return attrString1;
	}


	public void setAttrString1(String attrString1) {
		this.attrString1 = attrString1;
	}


	public String getAttrString2() {
		return attrString2;
	}


	public void setAttrString2(String attrString2) {
		this.attrString2 = attrString2;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrInteger == null) ? 0 : attrInteger.hashCode());
		result = prime * result + ((attrString1 == null) ? 0 : attrString1.hashCode());
		result = prime * result + ((attrString2 == null) ? 0 : attrString2.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ORM4RDF2DTTest))
			return false;
		ORM4RDF2DTTest other = (ORM4RDF2DTTest) obj;
		if (attrInteger == null) {
			if (other.attrInteger != null)
				return false;
		} else if (!attrInteger.equals(other.attrInteger))
			return false;
		if (attrString1 == null) {
			if (other.attrString1 != null)
				return false;
		} else if (!attrString1.equals(other.attrString1))
			return false;
		if (attrString2 == null) {
			if (other.attrString2 != null)
				return false;
		} else if (!attrString2.equals(other.attrString2))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	
}
