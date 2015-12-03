/**
 * CourseCourseConstraint makes sure that two classes/lectures cannot take place at the same time
 * it takes two indexes for the courses that cannot overlap
 * @author cdneave
 *
 */
public class CourseCourseConstraint extends Constraint{
	int Slot1;
	int Slot2;
	
	@Override
	public boolean testHard(int[] prob){
		if(prob[Slot1]==prob[Slot2]){
			return false;
		}
		return true;
	}

	public CourseCourseConstraint(int indexA, int indexB){
		this.Slot1=indexA;
		this.Slot2=indexB;
		//The two indexes of the courses that cannot align
	}
	
	public String toString(){
		return "CourseCourseConstraint "+Slot1+" NOT " + Slot2;
	}
}
