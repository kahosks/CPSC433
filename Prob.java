/**
 * Class Prob which is essentially a pr in Prob in an and-tree.  We might not need, could replace with
 * Slot[][], but Prob might just be nice for simplicity's sake.
 * @author Kiersten
 *
 */
public class Prob {
	public static final int MONDAY = 0;
	public static final int TUESDAY = 1;
	public static final int TUESLAB = 2;
	public static final int FRIDAY = 3;
	private int evalValue =0;
	private int slotsFilled = 0;
	private int coursesAdded = 0;
	private int totalCourses = 0;
	//Array of array of slots (3 indices, one for Monday, one for Tuesday, one for Friday).
	Slot[][] days = new Slot[4][];
	
	/**
	 * Constructor that takes three Slot arrays as arguments.
	 * @param mon	Slot array of Monday/Wed/Fri classes and labs.
	 * @param tues	Slot array of Tuesday/Thursday classes and labs.
	 * @param fri	Slot array of Friday labs.
	 */
	public Prob(Slot[] mon, Slot[] tues, Slot[] tueslab, Slot[] fri) {
		days[0] = mon;
		days[1] = tues;
		days[2] = tueslab;
		days[3] = fri;
		for (Slot[] d:days) {
			coursesAdded += calculateTotalCourses(d);
		}
		//coursesAdded = 4;
		//totalCourses = Scheduler.totalCourses;	//Use in implementation?
		totalCourses = 5;
	}
	
	public int calculateTotalCourses(Slot[] slots) {
		int count = 0;
		int labsAndClasses;
		for (Slot s: slots) {
			try {
				labsAndClasses = s.getLabsAndClasses().length;
				count += labsAndClasses;
			}
			catch (NullPointerException e) {
				System.out.println("Null;");
			}
		}
		return count;
	}
	/**
	 * Gets the eval value of the pr.
	 * @return	Integer evalValue for pr.
	 */
	public int getEvalValue() {
		return evalValue;
	}
	/**
	 * Gets the inverse value of the eval-value.
	 * Note: This returns a double, not an integer.
	 * @return	Double of eval-value inverse.
	 */
	//Note want inverse to be on number of classes, where inverse value is smaller as you
	//have more classes.  NOT for eval-value.
	public int getEvalInverse() {
		//return (double)1/(double)evalValue;
		//return getTotalSlots() - slotsFilled;
		return totalCourses - coursesAdded;
	}
	/**
	 * Adds a given value to current eval-value.
	 * @param value	Integer to add to current eval-value.
	 */
	public void addEvalValue(int value) {
		evalValue += value;
	}
	/**
	 * Sets the eval-value.
	 * @param value	Integer to set evalValue to.
	 */
	public void setEvalValue(int value) {
		evalValue = value;
	}
	/**
	 * Returns the total slots in the Prob.
	 * @return Integer value of all slot arrays in days.
	 */
	public int getTotalSlots() {
		return days[0].length + days[1].length + days[2].length + days[3].length;
	}
	/**
	 * Returns the integer value of slots that are filled.  The definition of filled
	 * means that at least one 
	 * @return
	 */
	public int getSlotsFilled() {
		return slotsFilled;
	}
	public int getCoursesAdded() {
		return coursesAdded;
	}
	public int getCoursesNotAdded() {
		return totalCourses;
	}
	public void setCoursesNotAdded(int coursenum) {
		totalCourses = coursenum;
	}
	public void addClass(Class c, int day, int time) {
		switch(day) {
		case MONDAY:
			//days[MONDAY][time];
		}
	}
	public Slot[][] getDays() {
		return days;
	}
	public static void main(String[] args) {
		Prob pr = new Prob(new Slot[2], new Slot[3], new Slot[4], new Slot[5]);
		int i=0;
		for (Slot[] s: pr.getDays()) {
			System.out.println("Number of slots in day "+i+": " +s.length);
			i++;
		}
		System.out.println("Length of Slot[][]: " +pr.getDays().length);
		System.out.println("Total slots: " + pr.getTotalSlots());
		System.out.println("Eval-inverse: " + pr.getEvalInverse());
	}
	
}
