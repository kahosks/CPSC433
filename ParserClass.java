
/**
 * ParserClass used for PartAssign and Unwanted.
 * @author CPSC 433 Toshibe
 *
 */
public class ParserClass {
	String identifier;
	String day;
	String time;
	
	/**
	 * Constructor.
	 * @param identifier		Full course name as string.
	 * @param day				Day of course as string.
	 * @param time				Time of course as string.
	 */
	public ParserClass(String identifier, String day, String time) {
		this.identifier = identifier.trim();
		this.day = day.trim();
		this.time = time.trim();
	}
	/**
	 * Gets the day of the class.
	 * @return	String representation of class's day.
	 */
	public String getDay() {
		return day;
	}
	/**
	 * Gets the class's time.
	 * @return	String representation of class time.
	 */
	public String getTime() {
		return time;
	}
	/**
	 * Gets the time as an integer value.
	 * @return		Integer value of time; ex. 8:00 -> 800
	 */
	public int getTimeInt() {
		int iTime;
		String[] hourMin = time.split(":");
		String tempTime = hourMin[0] + hourMin[1];
		iTime = Integer.parseInt(tempTime);
		return iTime;
	}
	/**
	 * Gets the class identifier (name).
	 * @return	String representation of the class name.
	 */
	public String getIdentifier() {
		return identifier;
	}
	public String toString() {
		return identifier +", " +day + ", " + time;
	}
}
