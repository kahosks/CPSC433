/** LabMaxConstraint holds the maximum amount of labs that can take place at a specific slot
 * There is a static int array that holds all of the indexes of labs so that all of the slots can be
 * quickly checked
 * @author cdneave
 *
 */
public class LabMaxConstraint extends Constraint {
	private static int[] labIndexes;
	private int slot;
	private int slotMax;
	@Override
	public boolean testHard(int[] node) {
		int labsInSlot = 0;
		for(int a: labIndexes){
			if(node[a]== slot){
				labsInSlot++;
			}
		}
		if(labsInSlot>slotMax){return false;}
		return true;
	}

	public static void setLabIndexes(int[] indexesTo){
		labIndexes = indexesTo;
	}
	
	public LabMaxConstraint(Slot a){
		this.slot = a.getDayTimeInt(); //Time plus day offset
		this.slotMax = a.getCoursemax();
	}
	
	public String toString(){
		return "The Lab max slot is:"+slot+" The max is:" + slotMax;
	}
}
