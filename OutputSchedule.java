import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class that can be used as a formatter to output the solution.
 * NOTE: Lots need to be implemented, like alphabetical ordering.. 
 * @author CPSC 433 Toshibe
 *
 */

/*
 *  alphabetize the courses, print out the eval value
 */
public class OutputSchedule {
	private String[] courseNames;
	private int[] courseTimes; 
	private int eval;
	private final int NAME = 0;
	private final int EVAL_VAL = 1;
	private final int DAY = 1;
	private final int TIME = 2;
	
	/**
	 * Constructor that takes String array and index array.
	 * @param courseNames		String array of course names.
	 * @param courseTimes		Int array of time scheduled corresponding to index in courseNames.
	 */
	public OutputSchedule(String[] courseNames, int[] courseTimes) {
		this.courseNames = courseNames;
		this.courseTimes = courseTimes;
		eval = courseTimes[EVAL_VAL];		//Get eval value from array
	}

	/**
	 * Prints out the alphabetized schedule.
	 */
	public void output() {
		//Prints out the eval value for solution.
		System.out.printf("Eval-value: %d\n", eval);

		//Create array list to temporarily store classes to print out.
		ArrayList<String[]> classes = new ArrayList<String[]>();
		//Skip over depth and eval and add course name and corresponding time and day to array.  Store info
		//as String array.
		for (int i =2; i< courseNames.length;i++) {
			String[] addClass = {courseNames[i], convertDay(courseTimes[i]), convertHour(courseTimes[i])};
			//add class to array list.
			classes.add(addClass);
		}
		//Create a collection and comparator to sort the elements in the array list.
		Collections.sort(classes, new Comparator<String[]>() {
			public int compare(String[] s1, String[] s2) {
				return (s1[NAME].compareTo(s2[NAME]));
			}
		});
		//print out classes in alphabetical order.
		for (String[] s:classes) {
			System.out.printf("%-30s : %-2s, %5s\n", s[NAME], s[DAY], s[TIME]);	
		}
	}

	/**
	 * Converts a time in array to a day string.
	 * @param hour	Time that will be converted to a day.
	 * @return		String representation of day ("MO", "TU", or "FR")
	 */
	public String convertDay(int hour) {
		//Check if less than 2400 (Monday).
		if (hour <2400) {
			return "MO";
		}
		//If not, check if less than 4800 (Tuesday). 
		else if (hour < 4800) {
			return "TU";
		}
		//If not, must be Friday.
		else {
			return "FR";
		}	
	}
	
	/**
	 * Converts a time integer to an output format "hour:minutes".
	 * @param hour	Time to convert to output format.
	 * @return		String representation of time in "hour:minutes" format.
	 */
	public String convertHour(int hour) {
		String strHour;
		//Goal: To get to 24 hour representation.
		//If hour greater than 4800 (Friday), subtract 2400 from it.
		if (hour >4800) {
			hour -= 2400;
		}
		//If hour still above 2400, subtract another 2400
		if (hour > 2400) {
			//if hour
			hour -= 2400;
		}
		strHour = "" + hour;
		//If hour less than 1000, then will have hour as first index and other
		//indices containing minutes.
		if (hour == 0) {
			return ("0:00");
		}
		else if (hour < 1000) {
			return (strHour.charAt(0) + ":" + strHour.substring(1));
		}
		//If >1000, have to get substring of two hour digits.
		else {
			return (strHour.substring(0, 2) + ":" + strHour.substring(2));
		}
	}
	/**
	 * Main method that just prints out given classes.
	 */
	public static void main(String[] args) {
			int[] indexArray1 = {20, 20, 900, 1800, 2700, 3600, 5600};
			String[] stringArray1 = {"", "", "CPSC 433 LEC 01", "SENG 301 LEC 020", "CPSC 433 LEC 01 TUT 01", "CPSC 332 LEC 02", "CPSC 413 LEC 01"};
			OutputSchedule os = new OutputSchedule(stringArray1, indexArray1);
			os.output();
		
	}
}
