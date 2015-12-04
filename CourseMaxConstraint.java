/**
 * CourseMaxConstraint holds the maximum amount of courses that can take place at a specific slot
 * There is a static int array that holds all of the indexes of labs so that all of the slots can be
 * quickly checked
 * @author cdneave
 *
 */
public class CourseMaxConstraint extends Constraint {
	private static int[] courseIndexes;	//
	private int slot;
	private int slotMax;
	@Override
	public boolean testHard(int[] prob) {
		int coursesInSlot = 0;
		for(int a: courseIndexes){
			if(prob[a]== slot){
				coursesInSlot++;
			}
		}
		if(coursesInSlot>slotMax){return false;}
		return true;
	}

	public static void setCourseIndexes(int[] indexesTo){
		courseIndexes = indexesTo;
	}
	
	public CourseMaxConstraint(Slot a){
		this.slot = a.getDayTimeInt();	//Time plus day offset
		this.slotMax = a.coursemax;
	}
}
