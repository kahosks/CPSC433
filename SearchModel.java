import java.util.*;
public class SearchModel {
	ArrayList<Class> labsAndClasses;
	
	Prob[] div(Prob p, int fBound) {
		//loop structure
		
		//Figure out what classes are already in prob
		ArrayList<Prob> divList;
		labsAndClasses.removeAll(p.getclassesScheduled());
		
		Slot[][] slots = prob.getDays();
		
		//Sorry about the weird naming...
		for (Slot[] s_1:slots){
			 for (Slot s_actual:s_1) {
				 copyProb = new Prob(p);
				 
				 //copyProb.add(); Fix this up once Prob has been finalized, use 
				 //s_actual
				 
				 
				 
				 divList.add(copyProb);
			 }
		}
		
		
		
		
		return divList.toArray();
	}
	
	private int eval(CommandParser commPar, Prob p) {
		int eval = 0;
		
		for (Slot[] s_1:slots) {
			for (Slot s_actual:s_1) {
				
			}
		}
		//Check coursmin(s)
		//Check labsmin(s)
		
		//Check proffessor preferences
		
		// 
		
		return 0;	
	}
	
	
	SearchModel(ArrayList<Class> labsAndCourses) {
		this.labsAndClasses = labsAndClasses;
	}
	
	

}
