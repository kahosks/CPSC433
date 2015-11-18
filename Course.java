

//Method Course that extends Class.
public class Course extends Class {

	public Course(String fullString) {
		super(fullString);
	}
	public Course(String name, String ID, String lec, String lecNum) {
		super(name, ID, lec, lecNum);
	}
	@Override
	public boolean isCourse() {
		//Returns true because is a course.
		return true;
	}

	@Override
	public String toString() {
		//Essentially reforming what was broken up.
		//ex. CPSC 433 LEC 01
		return String.format("%s %s %s %s", name, ID, lec, lecNum);
	}

}
