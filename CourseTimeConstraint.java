/**
 * Time constraint is a version of Constraint that makes it so a specific course or lab cannot
 * take place at a certain time
 * method: testHard(int[] prob) takes a node of the tree and checks if a specific instance of the constraint is met
 * @author cdneave
 *
 */
public class CourseTimeConstraint extends Constraint{
	private int index;	//Index that has the constraint (lines up to course)
	private int	slot;	//The constraint for the slot
	
	@Override
	public boolean testHard(int[] node) {
		if(node[index] == slot){
			return false;
		}
		return true;
	}

	//Made for every Unwanted in the input
	public CourseTimeConstraint(int indexOfCourse, int cannotUse){
		this.index=indexOfCourse;
		this.slot= cannotUse; //This is the hour+day offset of the slot that cannot be used
	}
}
