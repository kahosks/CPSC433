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
	private final int DAY = 1;
	private final int TIME = 2;
	public OutputSchedule(String[] courseNames, int[] courseTimes) {
		this.courseNames = courseNames;
		this.courseTimes = courseTimes;
		eval = courseTimes[1];
	}

	/**
	 * Currently prints out the output.  Will do more.
	 */
	public void output() {
		//Prints out the eval value for solution.
		System.out.println("Eval Value: " + eval);

		//String returnString = String.format("Eval-value: %d\n", eval);
		ArrayList<String[]> classes = new ArrayList<String[]>();
		for (int i =2; i< courseNames.length;i++) {
			String[] addClass = {courseNames[i], convertDay(courseTimes[i]), convertHour(courseTimes[i])};
			//add class to array list.
			classes.add(addClass);
		}
		Collections.sort(classes, new Comparator<String[]>() {
			public int compare(String[] s1, String[] s2) {
				return (s1[NAME].compareTo(s2[NAME]));
			}
		});
		//print out classes in alphabetical order.
		for (String[] s:classes) {
			System.out.printf("%-30s : %-2s, %5l\n", s[NAME], s[DAY], s[TIME]);	
		}
	}
	
	/**
	 * Converts a time in array to a day string.
	 * @param hour	Time that will be converted to a day.
	 * @return		String representation of day ("MO", "TU", or "FR")
	 */
	public String convertDay(int hour) {
		if (hour <2400) {
			return "MO";
		}
		else if (hour < 4800) {
			return "TU";
		}
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
		//Want: need to check for if hour has one or two digits
		//Put all hours down to 24 hour time.
		if (hour >4800) {
			hour -= 2400;
		}
		if (hour > 2400) {
			//if hour
			hour -= 2400;
		}
		strHour = "" + hour;
		if (hour < 1000) {
			return (strHour.charAt(0) + ":" + strHour.substring(1));
		}
		else {
			return (strHour.substring(0, 2) + ":" + strHour.substring(2));
		}
	}
}
