import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Integer;
public class NoFiveHundredConflicts {


	boolean checkConstraint(int[] prob, String[] indexString) {
		ArrayList<Integer> arrayOf500 = new ArrayList<Integer>();
		
		for (int i = 2; i < prob[0]; i++) {
			String courseId = indexString[i];
			String[] args = courseId.split(" ");
			if (args[1].charAt(0) == '5')
				arrayOf500.add( new Integer(prob[i]));
			}
		}
		int[] intArrayOf500 = new int[arrayOf500.size()];
		for (int i = 0; i < intArrayOf500.length; i++) {
			intArrayOf500[i] = arrayOf500.get(i).intValue();
		}
		
		Arrays.sort(intArrayOf500);
		
		for (int i = 0; i < intArrayOf500.length - 1; i++ ){
			if (intArrayOf500[i] == intArrayOf500[i + 1]){
				return false;
			}
		}
		
		return true;
	
	}
	NoFiveHundredConflicts() {
		
	}
	
	public static void main(String[] args) {
		int[] probFail = { 6, 0, 2400, 2400, 2800, 1400};
		String[] stringFail = {"", "", "CPSC 500", "CPSC 502", "CPSC 235", "CPSC 501"};
		NoFiveHundredConflicts test = new NoFiveHundredConflicts();
		System.out.println(test.checkConstraint(probFail, stringFail));
		
		int[] probPass = {6, 0, 2400, 2500, 2600, 2700};
		String[] stringPass = {" ", " ", "CPSC 500" ,"CPSC 500" ,"CPSC 500" ,"CPSC 500"};
		System.out.println(test.checkConstraint(probPass, stringPass));
	}

}
