

import java.util.Vector;

//Abstract method Class that holds information about the Class added.
public abstract class Class {
	String name; //ex. CPSC
	String ID;	//ex. 433
	String lec;	//ie. LEC
	String lecNum;	//ex. 01
	
	//Have to set time and day manually, ie., when assigning them to slots.
	String time = "0:00";
	String day = "";
	public Class(String fullString) {
		String[] input = fullString.split("[ ]+");
		this.name = input[0];
		this.ID = input[1];
		this.lec = input[2];
		this.lecNum = input[3];
	}
	public Class(String name, String ID, String lec, String lecNum) {
		this.name = name;
		this.ID = ID;
		this.lec = lec;
		this.lecNum = lecNum;
	}
	//Get name: ex. CPSC
	public String getName() {
		return name;
	}
	//Get ID: ex. 433
	public String getID() {
		return ID;		
	}
	//Get lecture: ex. LEC/TUT/LAB
	public String getLecture() {
		return lec;
	}
	//Get lecture/tut/lab number: ex. 01
	public String getLectureNum() {
		return lecNum;
	}
	//Sets the time as a string.
	public void setTime(String time) {
		this.time = time;
	}
	
	//Gets the time in string form.
	public String getTime() {
		return time;
	}
	//Gets hour.
	public String getHour() {
		String[] sp = time.split(":");
		return sp[0];
	}
	//Gets minutes.
	public String getMinutes() {
		String[] sp = time.split(":");
		return sp[1];
	}
	//Set the day class is on.
	public void setDay(String day) {
		this.day = day;
	}
	//Gets day that class is on.
	public String getDay() {
		return day;
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
	
	/* Creates a previous or upcoming time, depending on addition argument.  This method 
	 * method is used in the overlap method.
	 */
	private String createTime(int addition) {
		//Parses the hour of time as a string, adds the addition to the hour integer,
		// and then concatenate this new hour with :00 to create a new time.
			return (Integer.parseInt(getHour()) + addition) +":00";
	}
	
	/*
	 * Method that checks for overlaps on Tuesday.
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
	
	/*
	 * Method that checks for overlap on Friday.
	 * @param s		where s is type Course
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

	//Abstract methods to be overridden in children classes.
	public abstract boolean isCourse();
	public abstract String toString();
}

