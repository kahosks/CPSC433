
/**
 * Class used for Pair and Unwanted to store information.
 * @author CPSC 433 Toshibe
 *
 */
public class PairedCourseClass {
	private Class a;
	private Class b;
	
	/**
	 * Constructor.
	 * @param a		First class of pair
	 * @param b		Second class of pair
	 */
	public PairedCourseClass(Class a, Class b) {
		this.a = a;
		this.b = b;
	}
	

	/**
	 * Gets a class c's pair.  Returns null if no class paired.
	 * @param c		Class whose pair you want.
	 * @return		Class c's pair if found, null otherwise.
	 */
	public Class getOtherClass(Class c) {
		if (a.toString().equals(c.toString())) {
			return b;
		}
		else if (b.toString().equals(c.toString())) {
			return a;
		}
		else {
			return null;
		}
	}
	
	public Class getFirstPair() {
		return a;
	}
	public Class getSecondPair() {
		return b;
	}
	/**
	 * String representation of the class.
	 * @return	String representation of the clas.
	 */
	public String toString() {
		return a.toString()+", " +b.toString();
	}
}
