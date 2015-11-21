/**
 * Class PenSecdiff that evaluates penalties for the soft constraint that 
 * different sections of the same class should be at different times.
 * @author CPSC 433 Toshibe
 *
 */
public class PenSecdiff extends Eval{

	/**
	 * Constructor that takes a slot array and a weighted penalty value as arguments.
	 * @param slots	Slot array to be evaluated.
	 * @param pen_secdiff	Integer penalty for violating constraint.
	 */
	public PenSecdiff(Slot[] slots, int pen_secdiff) {
		super(slots);
		this.pen_secdiff = pen_secdiff;
	}
	/**
	 * Evaluates all slots and penalizes slots that violate constraint.
	 * @return	Integer sum of penalities applied.
	 */
	//make sure the lectures of the same class are scheduled at different times.
	//TODO: Implement.
	public int evaluate() {
		int penalty =0;
		
		return penalty;
	}
}
