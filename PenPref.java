/**
 * Class PenPref that evaluates the penalty for the soft constraint of class
 * time preferences.
 * @author CPSC 433 Toshibe
 *
 */
import java.util.Vector;
public class PenPref extends Eval  {
	Vector<Parser.Preference> pref;
	
	/**
	 * Constructor that takes a slot array and the pref penalty value as constructors.
	 * @param slots	Array of slots to be evaluated.
	 * @param pen_pref	Weighted penalty for violating constraint.
	 */
	//Might not need this constructor.
	public PenPref(Slot[] slots, int pen_pref) {
		super(slots);
		this.pen_pref = pen_pref;
	}
	
	/**
	 * Constructor that takes a slot array, the penalty pref value, and the vector
	 * of preferences as arguments.
	 * 
	 * @param slots	Array of slots to be evaluated.
	 * @param pen_pref	Penalty for violating constraint.
	 * @param pref	Vector of preference slots/classes.
	 */
	public PenPref(Slot[] slots, int pen_pref, Vector<Parser.Preference> pref) {
		super(slots);
		this.pen_pref = pen_pref;
		this.pref = pref;
	}
	
	/**
	 * Evaluates the slots and sums up the total amount of penalties.  
	 * @return  Integer sum of penalty values for violating constraint.
	 */
	public int evaluate() {
		int penalty=0;
		for (Slot s: slots) {
			for (Class c: s.getLabsAndClasses()) {
				for (Parser.Preference p: pref) {
					if (p.getIdentifier().equals(c.getIdentifier())) {
						penalty += pen_pref;
					}
				}
			}
		}
		return penalty;
	}
}
