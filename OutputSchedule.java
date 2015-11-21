/**
 * Class that can be used as a formatter to output the solution.
 * NOTE: Lots need to be implemented, like alphabetical ordering.. 
 * @author CPSC 433 Toshibe
 *
 */

public class OutputSchedule {
	public OutputSchedule() {
	}
	public void output(Slot[] args) {
		//String returnString = String.format("Eval-value: %d\n", eval);

		for (Slot s:args) {
			for (Class c:s.getLabsAndClasses()) {
				System.out.printf("%-30s : %-2s, %5l\n", c.toString(), c.getDay(), c.getTime());	
			}
		
		}
	}
}
