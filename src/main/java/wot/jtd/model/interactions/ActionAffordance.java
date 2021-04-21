package wot.jtd.model.interactions;

import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;
import wot.jtd.model.schemas.data.DataSchema;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#actionaffordance">ActionAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#actionaffordance">ActionAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionAffordance extends InteractionAffordance{

		// -- Attributes 
	
		@RdfObject("https://www.w3.org/2019/wot/td#hasInputSchema")
		protected DataSchema input;
		
		@RdfObject("https://www.w3.org/2019/wot/td#hasOutputSchema")
		protected DataSchema output;
		
		@RdfDatatype("https://www.w3.org/2019/wot/td#isSafe")
		protected Boolean safe; // has default value
		
		@RdfDatatype("https://www.w3.org/2019/wot/td#isIdempotent")
		protected Boolean idempotent; // has default value
			
		// -- Getters & Setters
		
		public DataSchema getInput() {
			return input;
		}

		public void setInput(DataSchema input) {
			this.input = input;
		}

		public DataSchema getOutput() {
			return output;
		}

		public void setOutput(DataSchema output) {
			this.output = output;
		}

		public Boolean getSafe() {
			return safe;
		}

		public void setSafe(Boolean safe) {
			this.safe = safe;
		}

		public Boolean getIdempotent() {
			return idempotent;
		}

		public void setIdempotent(Boolean idempotent) {
			this.idempotent = idempotent;
		}

		
		// -- HashCode & Equals
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = computeHashCode(result, prime);
			
			result = prime * result + ((input == null) ? 0 : input.hashCode());
			result = prime * result + ((output == null) ? 0 : output.hashCode());
			result = prime * result + ((safe == null) ? 0 : safe.hashCode());
			result = prime * result + ((idempotent == null) ? 0 : idempotent.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this.getClass() != obj.getClass())
				return false;
			
			ActionAffordance other = (ActionAffordance) obj;
			Boolean sameClass = sameSuperAttributes(other);
			sameClass &= sameCollections(this.getForms(), other.getForms());
			sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
			sameClass &= sameMap(this.getTitles(), other.getTitles());
			sameClass &= sameMap(this.getUriVariables(), other.getUriVariables());
			sameClass &= sameAttribute(this.getTitle(), other.getTitle());
			sameClass &= sameAttribute(this.getDescription(), other.getDescription());

			sameClass &= sameAttribute(this.getInput(), other.getInput());
			sameClass &= sameAttribute(this.getOutput(), other.getOutput());
			sameClass &= sameAttribute(this.getSafe(), other.getSafe());
			sameClass &= sameAttribute(this.getIdempotent(), other.getIdempotent());

			return sameClass;
		}


		
		
		
}
