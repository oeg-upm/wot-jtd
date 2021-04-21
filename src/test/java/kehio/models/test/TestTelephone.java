package kehio.models.test;

import kehio.annotations.RdfDatatype;

public class TestTelephone {
	@RdfDatatype("http://xmlns.com/foaf/0.1/number")
	private String number;
	
	public TestTelephone() {
		
	}
	
	public TestTelephone(boolean b) {
		this.number = "+34 54 65 23";
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("number: ").append(number).append("\n");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TestTelephone))
			return false;
		TestTelephone other = (TestTelephone) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}
	
	
	
}
