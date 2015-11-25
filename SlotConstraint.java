
public class SlotConstraint extends Constraint{
	int Slot1;
	int Slot2;
	
	@Override
	public boolean testHard(int[] prob){
		if(prob[Slot1]==prob[Slot2]){
			return false;
		}
		return true;
	}

}
