package wot.jtd.model.interactions;

import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfObject;
import wot.jtd.model.schemas.data.DataSchema;


/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#eventaffordance">EventAffordance</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#eventaffordance">EventAffordance WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventAffordance extends InteractionAffordance{

	// -- Attributes
	
	@RdfObject(value="https://www.w3.org/2019/wot/td#hasSubscriptionSchema")
	protected DataSchema subscription;
	
	@RdfObject(value="https://www.w3.org/2019/wot/td#hasNotificationSchema")
	protected DataSchema data;
	
	@RdfObject(value="https://www.w3.org/2019/wot/td#hasCancellationSchema")
	protected DataSchema cancellation;
	
	// -- Getters & Setters

	public DataSchema getSubscription() {
		return subscription;
	}

	public void setSubscription(DataSchema subscription) {
		this.subscription = subscription;
	}

	public DataSchema getData() {
		return data;
	}

	public void setData(DataSchema data) {
		this.data = data;
	}

	public DataSchema getCancellation() {
		return cancellation;
	}

	public void setCancellation(DataSchema cancellation) {
		this.cancellation = cancellation;
	}

	// -- HashCode & Equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = computeHashCode(result, prime);
		result = prime * result + ((subscription==null) ? 0: subscription.hashCode());
		result = prime * result + ((data==null) ? 0: data.hashCode());
		result = prime * result + ((cancellation==null) ? 0: cancellation.hashCode());
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		EventAffordance other = (EventAffordance) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameCollections(this.getForms(), other.getForms());
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		sameClass &= sameMap(this.getTitles(), other.getTitles());
		sameClass &= sameMap(this.getUriVariables(), other.getUriVariables());
		sameClass &= sameAttribute(this.getTitle(), other.getTitle());
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());

		sameClass &= sameAttribute(this.getSubscription(), other.getSubscription());
		sameClass &= sameAttribute(this.getData(), other.getData());
		sameClass &= sameAttribute(this.getCancellation(), other.getCancellation());

		return sameClass;
	}

}
