/**
 * Class PenNotpaired that evaluates slots for the soft constraint that paired classes should be 
 * scheduled at the same time.
 * @author CPSC 433 Toshibe
 */

import java.util.Vector;

public class PenNotpaired extends Eval{
	Vector<Parser.PairedCourseClass> pair;
	/**
	 * Constructor that takes a slot array, weighted penalty value, and Vector of pairs as arguments.
	 * @param slots	Array of slots to be evaluated.
	 * @param pen_notpaired	Penalty to be applied to violating slots.
	 * @param pair	Array of pairs parsed by parser.
	 */
	public PenNotpaired(Slot[] slots, int pen_notpaired, Vector<Parser.PairedCourseClass> pair) {
		super(slots);
		this.pen_notpaired = pen_notpaired;
		this.pair = pair;
	}
	/**
	 * Evaluates the slots to make sure that if they violate the constraint, a penalty is applied.  
	 * @return Sum of penalties applied.
	 */
	//check for pairs in pair vector and make sure that if the class/lab has a pair, that it is in the
	//same time slot as its pair.  If not, penalize.
	public int evaluate() {
		int penalty =0;
		for (Slot s:slots) {
			for (Class c: s.getLabsAndClasses()) {
				for (Parser.PairedCourseClass p: pair) {
					Class d;
					if ((d = p.getOtherClass(c)) != null) {
						if ((d.getTime() != c.getTime()) && (d.getDay() != c.getDay()))  {
							penalty += pen_notpaired;
						}
					}
				}
			}
		}
		return penalty;
	}
}
