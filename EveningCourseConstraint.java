/**
 * EveningConstraint is made for when a lecture can only take place in the evenings
 * @author cdneave
 *
 */
public class EveningCourseConstraint extends Constraint{
	private int index;	//Index of the evening course
	
	@Override
	public boolean testHard(int[] node) {
		if(node[index]%2400 >= 1800 || node[index] == 0){return true;}//If the class starts after 6pm (1800) (or is unscheduled){return true;}//If the class starts after 6pm (1800)
		return false;
	}

	public EveningCourseConstraint(int indexOfCourse){
		this.index = indexOfCourse;
	}
}
