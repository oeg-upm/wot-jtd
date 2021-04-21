package kehio.models.test;

import java.util.HashSet;
import java.util.Set;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;

public class TestPerson1 {

	@RdfDatatype("http://xmlns.com/foaf/0.1/name")
	private String name;
	@RdfDatatype("http://xmlns.com/foaf/0.1/surnames")
	private Set<String> surnames;
	@RdfObject("http://xmlns.com/foaf/0.1/phone")
	private TestTelephone phone;
	
	public TestPerson1(){
		
	}
	
	public TestPerson1(boolean init){
		if(init) {
			name = "John";
			surnames = new HashSet<>();
			surnames.add("Doe");
			surnames.add("Fold");
			TestTelephone phone = new TestTelephone(true);
			this.phone = phone;
		}
		
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ").append(name).append("\n");
		builder.append("Surname: ").append(surnames).append("\n");
		builder.append("Phone: ").append(phone).append("\n");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((surnames == null) ? 0 : surnames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TestPerson1))
			return false;
		TestPerson1 other = (TestPerson1) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (surnames == null) {
			if (other.surnames != null)
				return false;
		} else if (!surnames.equals(other.surnames))
			return false;
		return true;
	}
	

	
	
}
