public abstract class HardConstraints {
	public static final int EVENING = 17;	//arbitrary evening number
	boolean statusFlag;
	
	public HardConstraints() {
		statusFlag = true;	
	}
	
	public abstract boolean checkConstraint(Slot[] s);
		//return statusFlag;

	
	public boolean getStatusFlag() {
		return statusFlag;
	
	}
	
	public boolean assign(Slot s, Class a) {
		if (a.isCourse()) {
			for (Class l:s.getLabs()) {
				if (l.getIdentifier().contains(a.getIdentifier())) {
					return false;
				}
			}
		}
		else {
			for (Class l:s.getCourses()) {
				if (l.getIdentifier().contains(a.getIdentifier())) {
					return false;
				}
			}
		}
		return true;
	}
	//Just in case we want to set the status flag internally
	//should not be public and may not even be needed
	private void setStatusFlag(boolean status) {
		statusFlag = status;
	}
	
	/**
	 * Checks all the hard constraints.  Returns true if Prob passes all constraints,
	 * returns false otherwise.
	 * @param p	Prob to be passed into method.
	 * @return	Boolean true/false.
	 */
	 //not complete
	public boolean checkAllHardConstraints(Prob p) {
		for (Slot[] d:p.getDays()) {
			if (!(new CourseMax().checkConstraint(d)) && new LabsMax().checkConstraint(d)
					&& new HandleFiveHundredLevelClasses().checkConstraint(d) 
					&& new NoClassesAtTimeSlot().checkConstraint(d)  
					&& new HandleAssign().checkConstraint(d) 
					&& new EveningLectureConstr().checkConstraint(d)) {
				return false;
			}
		}
		return true;
	}
	
	public abstract String toString();
	//	return HardConstraints.class.getName();
	//}


	public static void main(String[] args) {
	
	//Test to make sure that both will print out their names
	System.out.println(new CourseMax());
	//System.out.println(new HardConstraints());
	
	}
}
class CourseMax extends HardConstraints {

	public CourseMax() {}
	
	public boolean checkConstraint(Slot[] sl) {
		//If one slot over course max, then return false.
		for (Slot s:sl) {
			if (s.getCourses().length > s.getCoursemax()) {
				return false;
			}
		}
		//Went through loop with no problems, then return true.
		return true;
	}
	
	/*This method is commented out because I don't have access to the slot class but this should be the 
	signature of the method
	*/
	
	public String toString() {
		return CourseMax.class.getName();
	}
}

class LabsMax extends HardConstraints {
	
	
	public LabsMax() {
	
	}

	public boolean checkConstraint( Slot[] sl) {
		//If one slot over course max, then return false.
		for (Slot s:sl) {
			if (s.getLabs().length > s.getLabmax()) {
				return false;
			}
		}
		//Went through loop with no problems, then return true.
		return true;
	}
	/*
	This method is commented out because I don't have access to the slot class but this should be the 
	signature of the method
	*/
	
	public String toString() {
		return LabsMax.class.getName();
	}

}


//Intended to make sure that no 500 level classes are in the same time
//slots
class HandleFiveHundredLevelClasses extends HardConstraints {

	//construtctor
	//basic constructor that doesn't take argument or set anything
	public HandleFiveHundredLevelClasses() {}
	
	//check contraints method
	//basic method stub
	public boolean checkConstraint(Slot[] sl) {
		int count = 0;
		//If one slot over course max, then return false.
		for (Slot s:sl) {
			for (Class c: s.getCourses()) {
				if (c.getID().charAt(0) == '5') {
					count++;
				}
				if (count > 1) {
					return false;
				}
			}
		}
		//Went through loop with no problems, then return true.
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return HandleFiveHundredLevelClasses.class.getName();
	}

}

//Class is used to check if there is a class at a certain time slot
/**
 * Check that there are no courses scheduled for 11-12:30 on Tuesday.
 * @author CPSC433 Toshibe
 *
 */
class NoClassesAtTimeSlot extends HardConstraints {
	
	//TODO: add stuff to constructor
	public NoClassesAtTimeSlot() {
		
	}
	/**
	 * Checks that all slots fulfill constraint.  Returns false if does not fulfill.
	 * @param	sl	Array of Slot objects.
	 * @return Boolean true/false.
	 */
	public boolean checkConstraint(Slot[] sl) {
		for (Slot s: sl) {
			if (s.getDay() == "TU") {
				if (s.getTime().equals("11:00")) {
					if (s.getCourses().length >0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Creates a string representation of the class.
	 * @return	String representation of class.
	 */
	public String toString() {
		return NoClassesAtTimeSlot.class.getName();
	}

}

class HandleNC extends HardConstraints {
	public boolean checkConstraint(Slot[] s) {
		return true;
	}
	public String toString() {
		return null;
	}
}
class HandleUnwanted extends HardConstraints {
	public boolean checkConstraint(Slot[] s) {
		return true;
	}
	public String toString() {
		return null;
	}
}
/**
 *Intended to handle the hard constraints that use assign and then return whether the constraints passed or failed
 *
 */
class HandleAssign extends HardConstraints {
	//basic constructor
	public HandleAssign() {
		
	}
	//basic method stub that doesn't do anything but return true.
	public boolean checkConstraint(Slot[] s) {
		return true;
	}
	public String toString() {
		return HandleAssign.class.getName();
	}


}
class SpecialCourseConstr extends HardConstraints {
	
	public boolean checkConstraint(Slot[] s) {
		return true;
	}
	public String toString() {
		return null;
	}
}
class EveningLectureConstr extends HardConstraints {

	@Override
	public boolean checkConstraint(Slot[] s) {
		for (Slot sl:s) {
			for (Class c:sl.getLabsAndClasses()) {
				if ((c.getLectureNum().charAt(0) == '9') && (Integer.parseInt(sl.getHour()) < EVENING)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
