
public class HardConstraints {

	boolean statusFlag;
	

	
	
	public HardConstraints() {
		statusFlag = true;	
	}
	
	public static boolean checkConstraint(Prob p){
	
		return true;
	
	}
	
	public boolean getStatusFlag() {
		return statusFlag;
	
	}
	//Just in case we want to set the status flag internally
	//should not be public and may not even be needed
	private void setStatusFlag(boolean status) {
		statusFlag = status;
	}
	
	public String toString() {
	
		return HardConstraints.class.getName();
	}
	
	public static void main(String[] args) {
	
	//Test to make sure that both will print out their names
	System.out.println(new CourseMax());
	System.out.println(new HardConstraints());
	
	}

}

class CourseMax extends HardConstraints {

	int courseMax;

	//@author Bernie Mayer
	
	public boolean checkConstraint(Prob p) {
	
	
	
	}
	

	
	public String toString() {
		return CourseMax.class.getName();
	}

}

class LabsMax extends HardConstraints {
	
	int labsMax;
	
	public LabsMax(int labsMax) {
	
	}

	/*
	public boolean checkConstraint( Slot s) {
	
	}
	*
	This method is commented out because I don't have access to the slot class but this should be the 
	signature of the method
	*/
	
	public String toString() {
		return CourseMax.class.getName();
	}

}


//Intended to make sure that no 500 level classes are in the same time
//slots
class HandleFiveHundredLevelClasses {

	//construtctor
	
	
	
	
	//check contraints method


}

//Class is used to check if there is a class at a certain time slot

class NoClassesAtTimeSlot {


}

//Intended to handle the hard constaints that use assign and then return whether the constraints passed or failed

class HandleAssign {


}


