/**
 * 
 * @author CPSC433 Toshibe
 * Public abstract class Class that holds the name, ID, lecture identifier, and lecture number of 
 * an object.
 */
public abstract class Class {
	String name; //ex. CPSC
	String ID;	//ex. 433
	String lec;	//ie. LEC
	String lecNum;	//ex. 01
	String identifier;
	
	//Have to set time and day manually, ie., when assigning them to slots.
	String time = "0:00";
	String day = "";
	/**
	 * Constructor with fullString argument.
	 * @param fullString	String of information that class will hold.
	 */
	public Class(String fullString) {
		this.identifier = fullString;
		String[] input = fullString.split("[ ]+");
		this.name = input[0];
		this.ID = input[1];
		this.lec = input[2];
		this.lecNum = input[3];
	}
	
	/**
	 * 
	 * @param name	Name of class
	 * @param ID	Class number
	 * @param lec	Whether it is a lecture or lab
	 * @param lecNum	Number of lecture out of all lectures for course.
	 */
	public Class(String name, String ID, String lec, String lecNum) {
		this.name = name;
		this.ID = ID;
		this.lec = lec;
		this.lecNum = lecNum;
	}
	/**
	 * Gets the name of the class.
	 * @return	String name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Gets the ID number of the course.
	 * @return	String of course number.
	 */
	public String getID() {
		return ID;		
	}
	/**
	 * Gets whether the class is a LEC/TUT/LAB
	 * @return	String of lecture identifier
	 */
	public String getLecture() {
		return lec;
	}
	/**
	 * Gets the lecture number for the class.
	 * @return	String of the lecture number.
	 */
	public String getLectureNum() {
		return lecNum;
	}
	/**
	 * Sets the time of the class.
	 * @param time	String time you want to set the class to.
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * Gets a string representation of the class's time.
	 * @return	String time.
	 */
	public String getTime() {
		return time;
	}
	/**
	 * Gets a string representation of the hour of time.
	 * @return	String hour.
	 */
	public String getHour() {
		String[] sp = time.split(":");
		return sp[0];
	}
	/**
	 * Gets a string representation of class's minutes.
	 * @return	String minutes.
	 */
	public String getMinutes() {
		String[] sp = time.split(":");
		return sp[1];
	}
	/**
	 * Sets the day that the class is on.
	 * @param day	String representation of the day.
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * Gets the day that the class is on.
	 * @return	String day.
	 */
	public String getDay() {
		return day;
	}
	/**
	 * Gets the full string representation of the class's information.
	 * @return	String representation of the full class information.
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * 
	 * @param s	 Class that you are checking for overlap with.
	 * @return	boolean value true/false.
	 */
	public boolean overlaps(Class s) {
		
		//Check for the same day.
		if (s.getDay().equals(day)) {
			
			//Check that are the same time.
			if (s.getTime().equals(time)) {
				return true;
			}
			
			/*Check for specific Tuesday and Friday cases: Tuesday for overlap between labs and classes, and 
			Friday between labs and classes. */
			switch (s.getDay()) {
				case "TU":
					
					//if s is a course...
					if (s.isCourse()) {
						return isTuesOverlap(s);
					}
					
					//Otherwise, s is a lab and have to do reverse of above.
					else {
						return s.isTuesOverlap(this);				
					}
					
					//Will either return true or false, does not need break statement.
				case "FR":
					
					//Need to check if it is a course, that it is not starting in the middle of a lab.
					//Need to check if it is a lab, that it does not run into a course.
					if (s.isCourse() && !isCourse()) {
						
						//If Class we're comparing to isn't a lab, then it was already compared and thus not 
						//overlapping.  But if it is a lab, then have to check.
						return isFriOverlap(s);	
					}
					else {
						//Need to compare to another course, already handled lab.compare(lab) with if statement
						//before switch clause.
						if (isCourse() && !s.isCourse()) {
							return s.isFriOverlap(this);
						}
						//Is a lab, already checked that before switch statement.  Thus, false.
						return false;
					}
				default: //is Monday, but would have returned true from first if statement if there was an overlap.
					return false;
			}
		}
		
		//Not the same day, so return false.
		return false;
	}
	
	/**
	 * Creates a previous or upcoming time, depending on the addition argument.  This 
	 * method is applied in the overlap method.
	 * 
	 * @param addition	Integer that you want to add/subtract time.
	 * @return	String representation of time.
	 */
	private String createTime(int addition) {
		//Parses the hour of time as a string, adds the addition to the hour integer,
		// and then concatenate this new hour with :00 to create a new time.
			return (Integer.parseInt(getHour()) + addition) +":00";
	}
	
	/**
	 * Method that checks for overlaps on Tuesday.  This is used in the overlap
	 * method.
	 * @param s	Class that you are checking.
	 * @return	Boolean true/false.
	 */
	private boolean isTuesOverlap(Class s) {
		if (s.getMinutes().equals("00")) {
			//Check next hour for start of lab, since class is 1.5 hours.
			//It will not overlap a class, because we have already checked for that.
			if ((s.createTime(1)).equals(time)) { 
				return true;
			}
			return false;	//No overlap.
		}
		//Otherwise, starts at (xx:30)
		else {
			//Have to check current hour for overlap.
			if (time.equals(s.getHour() + ":00")) {
				return true;
			}
			//Also, check next hour for overlap.
			else if (time.equals(s.createTime(1))) {
				return true;
			}
			else {
				return false;	//No overlap.
			}
		}
	}
	
	/**
	 * Method that checks for overlap on Friday.  This is used in the overlap method.
	 * @param s		Class s.
	 * @return		Boolean true/false depending on if there is overlap or not.
	 */
	private boolean isFriOverlap(Class s) {

		//"time" is a lab time, so we check s's time
		//an hour ahead because labs on Fridays are 2 hours long.
		if (time.equals(s.createTime(-1))) {
			return true;	//Lab starts an hour before, thus overlap.
		}
		else {
			return false;	//No overlap.
		}	
	}

	/**
	 * Method that returns true if the Class is an instance of course.
	 * @return	Boolean true/false.
	 */
	public abstract boolean isCourse();
	/**
	 * Method that creates a toString representation of the data.
	 * @return	String of data to represent.
	 */
	public abstract String toString();
	public static void main(String[] args) {
		Class a = new Course("CPSC 433 LEC 01");
		System.out.println(a.getIdentifier());
	}
}

