//Idea for project.  Feel free to modify or ignore. 
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Slot {
	int coursemin=0, coursemax=0, labmin=0, labmax=0, currentLabs =0, currentClasses =0;
	String day = "none";
	String time = "0:00";
	Vector<Class> labsAndClasses = new Vector<Class>();
	SimpleDateFormat format = new SimpleDateFormat("hh:mm");
	
	
	/*
	 * NOTE: There is no constructor that will have coursemin/coursemax/labmin/labmax because
	 * those pieces of information come at different times (and since they are integers, there is no
	 * way to determine which constructor to use without flags).  The information can be added 
	 * later without trouble.
	 * 
	 * NOTE2:  Various methods for different ways of implementation.
	 */
	
	//Empty constructor.
	public Slot() {
		
	}
	
	//Constructor with only time agrument (this constructor might not be used, possibly delete).
	public Slot(String time) {
		this.time = time;
	}
	
	//Constructor with day and time arguments.  Use this constructor if creating array/list of Slot 
	// based on day.  Then, you can add in slot information with other methods.
	public Slot(String day, String time) {
		this.day = day;
		this.time = time;
	}

	//Add day and time information to slot
	public void addInfo(String day, String time) {
		this.day = day;
		this.time = time;
	}
	
	//Adds coursemin and coursemax information
	public void addCourseInfo(int coursemin, int coursemax) {
		this.coursemin = coursemin;
		this.coursemax = coursemax;
	}
	
	//Adds labmin and labmax information to slot.
	public void addLabInfo(int labmin, int labmax) {
		this.labmin = labmin;
		this.labmax = labmax;
	}
	
	//Sets coursemin variable.
	public void setCoursemin(int coursemin) {
		this.coursemin = coursemin;
	}
	
	//Gets coursemin variable
	public int getCoursemin() {
		return coursemin;
	}
	
	//Sets coursemax variable
	public void setCoursemax(int coursemax) {
		this.coursemax = coursemax;
	}
	
	//Gets coursemax variable
	public int getCoursemax() {
		return coursemax;
	}
	
	//Set labmin variable
	public void setLabmin(int labmin) {
		this.labmin = labmin;
	}
	
	//Get labmin variable.
	public int getLabmin() {
		return labmin;
	}
	
	//Sets labmax variable.
	public void setLabmax(int labmax) {
		this.labmax = labmax;
	}
	
	//Gets labmax variable.
	public int getLabmax() {
		return labmax;
	}
	
	//Set time of slot in hour:minutes  (ex. 8:00)
	public void setTime(String time) {
		this.time = time;
	}
	
	//Get time of slot in form hour:minutes
	public String getTime() {
		return time;
	}
	
	//Get slot's hour.
	public String getHour() {
		String[] sp = time.split(":");
		return sp[0];
	}
	
	//Gets minutes of Slot.
	public String getMinutes() {
		String[] sp = time.split(":");
		return sp[1];
	}
	//Set day.
	public void setDay(String day) {
		this.day = day;
	}
	
	//Return day.
	public String getDay() {
		return day;
	}
	
	public void add(Class aClass) {
		//Temporary variables corresponding to amount of classes/labs in labsAndClasses vector.
		int courseCheck = currentClasses;
		int labCheck = currentLabs;
		//If aClass is a course, then increment temp variable courseCheck.
		if (aClass.isCourse()) {
			courseCheck++;
		}
		else{
			//Otherwise, increment temp variable labCheck.
			labCheck++;
		}
		//If temp variables over capacity, then can't add course/lab.
		if ((courseCheck > coursemax) || (labCheck > labmax)) {
			System.out.println("Cannot add course");
		}
		else {
			labsAndClasses.add(aClass);
			//Set the day and time of the class.
			aClass.setTime(getTime());
			aClass.setDay(getDay());
			
			//Update currentClasses/currentLabs.
			currentClasses = courseCheck;
			currentLabs = labCheck;
		}
	}

	//Returns array of labsAndClasses vector.
	public Class[] getLabsAndClasses() {
		return (Class[]) labsAndClasses.toArray();
	}
	
	//Returns an array of Slot based on day and time.  NOTE:  This does not account for Tuesday's 
	// course times.  For that, can make a different array, or we can modify Tuesday to work with this
	// array and have flags for overlaps and such.
	public Slot[] initializeDay(String day) {
		int size = 13;	// 13 because 21-8= 13 (end time - start time)
		Slot[] Slot = new Slot[size];	//array of smpty Slot
		int startHour = 8;	//8 because earliest class time is 8 am
		for (int i = 0; i < size; i++) {
			String timeString = startHour + ":00";
			Slot[i].addInfo(day, timeString);	//add information to slot
		}
		return Slot;
	}
	
	//Gets index of slot based on hour.  NOTE: Use this function when using an array/list of Slot
	// based on days (ex. 3 arrays, one for MO, one for TU, one for FR, with array of Slot initialized
	// based on time of day.
	public int getSlotIndex(String time) {
		String[] hourMin = time.split(":");
		int hour = Integer.parseInt(hourMin[0]);
		return hour-8;
	}
}
