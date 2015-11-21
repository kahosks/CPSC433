/**
 * Class PenCourseMin which accounts for the soft constraint that 
 * penalizes solutions in which slots have less classes than the course min 
 * specifies.
 * 
 * @author CPSC 433 Toshibe
 *
 */
public class PenCourseMin extends Eval{

	/**
	 * Constructor that takes a slot array and the coursemin penalty as 
	 * arguments.
	 * @param slots	Slot array to be evaluated.
	 * @param pen_coursemin	Penalty value to be applied to violating slots.
	 */
	public PenCourseMin(Slot[] slots, int pen_coursemin) {
		super(slots);
		this.pen_coursemin = pen_coursemin;
	}
	/**
	 * For every slot in s, checks that amount of courses exceeds coursemin.
	 * If not, penalty value applied.  
	 * @return	Integer of total sum of the penalties for slots.
	 */
	public int evaluate() {
		int penalty =0;
		for (Slot s: slots) {
			if (s.getCourses().length < s.getCoursemin()) {
				penalty += pen_coursemin;
			}
		}
		return penalty;
	}
}
