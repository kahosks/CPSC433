

//Method Lab that extends Class. 
public class Lab extends Class {
	String tutLab = "";
	String tutLabNum = "";
	
	public Lab(String fullString) {
		super(fullString);
		String[] input = fullString.split("[ ]+");
		if (input.length ==6) {
			tutLab = input[4];
			tutLabNum = input[5];
		}
	}
	//Constructor where does not have additional arguments.
	public Lab(String name, String ID, String lec, String lecNum) {
				super(name, ID, lec, lecNum);
	}

	//Constructor with additional arguments for tutorial lab and tutorial lab number.
	public Lab(String name, String ID, String lec, String lecNum, String tutLab, String tutLabNum) {
		super(name, ID, lec, lecNum);
		//tutLab and tutLabNum are not null values, then set them to this.tutLab and this.tutLabNum.
		if (tutLab != null && tutLabNum != null) {
			this.tutLab = tutLab;
			this.tutLabNum = tutLabNum;
		}
	}
	//Get tutLab.
	public String getTutLab() {
		return tutLab;
	}
	//Get tutLabNum.
	public String getTutLabNum() {
		return tutLabNum;
	}
	
	//Labs are not courses, so return false.
	@Override
	public boolean isCourse() {
		return false;
	}

	//Returns as full statement.  ex. CPSC 433 LEC 01 TUT 03
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s", name, ID, lec, lecNum, tutLab, tutLabNum);
	}

}
