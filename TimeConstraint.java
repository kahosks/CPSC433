
public class TimeConstraint extends Constraint{
	int index;	//Index that has the constraint (lines up to course)
	int	slot;	//The constraint for the slot
	
	@Override
	public boolean testHard(int[] node) {
		if(node[index] == slot){
			return false;
		}
		return true;
	}

}
