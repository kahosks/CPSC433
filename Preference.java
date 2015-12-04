
/**
	 * Class Preference that extends ParserClass.
	 * @author CPSC 433 Toshibe
	 *
	 */
public class Preference extends ParserClass{
	int prefValue;
	
	/**
	 * 
	 * @param day			Day of class as a string.
	 * @param time			Time of class as a string.
	 * @param identifier	String representation of class name.
	 * @param prefValue		Preference value of class as a string.
	 */
	public Preference(String day, String time, String identifier, String prefValue) {
		super(day, time, identifier);
		this.prefValue = Integer.parseInt(prefValue.trim());	//parse string to an integer
	}

	/**
	 * Gets the preference value for the preference.
	 * @return	Integer value preference.
	 */
	public int getPrefValue() {
		return prefValue;
	}
	/**
	 * String representation of the class.
	 * @return	String identifier of class.
	 */
	public String toString() {
		return identifier +", " +day + ", " + time + ", " + prefValue;
	}
}
