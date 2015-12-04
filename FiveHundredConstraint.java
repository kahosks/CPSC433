/**
 * A constraint to test if every 500 level course has a unique time slot
 */

import java.util.ArrayList;
import java.util.Arrays;


public class FiveHundredConstraint extends Constraint{
	private static int[] fiveHundIndexes;

	@Override
	public boolean testHard(int[] node) {
		
		ArrayList<Integer> containsList = new ArrayList<Integer>();
		for(int index: fiveHundIndexes){
			if(containsList.contains(node[index])&& node[index]!=0){
				return false;
			}
			
			containsList.add(node[index]);
		}
		return true;
	}

	public static void setFiveHundIndexes(int[] indexes){
		fiveHundIndexes=indexes;
	}
}
