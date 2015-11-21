/**
 * Class PenLabMin which accounts for the soft constraint that 
 * penalizes solutions in which slots have less labs than the lab min 
 * specifies.
 * 
 * @author CPSC 433 Toshibe
 *
 */
public class PenLabmin extends Eval{

	/**
	 * Constructor that takes a slot array and the labmin penalty as 
	 * arguments.
	 * @param slots	Slot array to be evaluated.
	 * @param pen_labmin	Penalty value to be applied to violating slots.
	 */
	public PenLabmin(Slot[] slots, int pen_labmin) {
		super(slots);
		this.pen_labmin = pen_labmin;
	}
	/**
	 * For every slot in s, checks that amount of labs exceeds labmin.
	 * If not, penalty value applied.  
	 * @return	Integer of total sum of the penalties for slots.
	 */
	public int evaluate() {
		int penalty =0;
		for (Slot s: slots) {
			if (s.getLabs().length < s.getLabmin()) {
				penalty += pen_labmin;
			}
		}
		return penalty;
	}
}
