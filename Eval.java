import java.util.Vector;

/**
 * Abstract class Eval.  Used to make child classes for other Eval functions.
 * Eval is used for evaluating soft constraints and getting an eval value to use
 * when choosing best solution. 
 * Feel free to modify this, definitely needs work!
 * @author CPSC 433 Toshibe
 *
 */
public abstract class Eval {
	protected int pen_labmin =0;
	protected int pen_coursemin=0;
	protected int pen_pref=0;
	protected int pen_notpaired=0;
	protected int pen_secdiff=0;
	Slot[] slots;
	Vector<Eval> eval = new Vector<Eval>();
	
	/**
	 * Empty constructor.  Not sure if we need this.
	 */
	public Eval() {}
	/**
	 * Constructor that takes a Slot array as argument.
	 * @param slots	Array of Slots.
	 */
	public Eval(Slot[] slots) {
		this.slots = slots;
	}
	/**
	 * Constructor that takes labmin, coursemin, preferences, notpaired, and sectiondifference
	 * penalty arguments.
	 * 
	 * @param pen_labmin	Penalty for not fulfilling labmin constraint.
	 * @param pen_coursemin	Penalty for not fulfilling coursemin constraint.
	 * @param pen_pref	Penalty for not fulfilling preferences constraint.
	 * @param pen_notpaired	Penalty for not fulfilling notpaired constraint.
	 * @param pen_secdiff	Penalty for not fulfilling section difference constraint.
	 */
	public Eval(int pen_labmin, int pen_coursemin, int pen_pref, int pen_notpaired, int pen_secdiff) {
		this.pen_labmin = pen_labmin;
		this.pen_coursemin = pen_coursemin;
		this.pen_pref = pen_pref;
		this.pen_notpaired = pen_notpaired;
		this.pen_secdiff = pen_secdiff;
	}
	/**
	 * Evaluate method to implement in child classes.  
	 * @return	Integer that will be eval-value.
	 */
	public abstract int evaluate();
	
	/**
	 * Evaluates all Eval objects.  This method can be called from any Eval
	 * object instantiated.
	 * @return	Integer value which is the penalty for all soft constraints.
	 */
	public int evaluateAll() {
		int totalPenalty =0;
		for (Object o: eval.toArray()) {
			Eval e = (Eval) o;
			totalPenalty += e.evaluate();
		}
		return totalPenalty;
	}
	/**
	 * Adds Eval object to eval vector.
	 * @param fun	Eval object to be added to vector.
	 */
	public void addToEval(Eval fun) {
		eval.add(fun);
	}
}
