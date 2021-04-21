package kehio.models.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;

public class TestPerson2 {
	@RdfDatatype("http://xmlns.com/foaf/0.1/name")
	private String name;
	@RdfDatatype("http://xmlns.com/foaf/0.1/surnames")
	private Set<String> surnames;
	@RdfObject("http://xmlns.com/foaf/0.1/phone")
	private List<TestTelephone> phones;
	
	public TestPerson2() {
		
	}
	public TestPerson2(boolean init){
		if(init) {
			name = "John";
			surnames = new HashSet<>();
			surnames.add("Doe");
			surnames.add("Fold");
			TestTelephone phone1 = new TestTelephone(true);
			TestTelephone phone2 = new TestTelephone(true);
			this.phones = new ArrayList<>();
			this.phones.add(phone1);
			this.phones.add(phone2);
		}
		
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ").append(name).append("\n");
		builder.append("Surname: ").append(surnames).append("\n");
		builder.append("Phones: ").append(phones).append("\n");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phones == null) ? 0 : phones.hashCode());
		result = prime * result + ((surnames == null) ? 0 : surnames.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TestPerson2))
			return false;
		TestPerson2 other = (TestPerson2) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phones == null) {
			if (other.phones != null)
				return false;
		} else if (!phones.equals(other.phones))
			return false;
		if (surnames == null) {
			if (other.surnames != null)
				return false;
		} else if (!surnames.equals(other.surnames))
			return false;
		return true;
	}
	
	
}
