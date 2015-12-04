//WORK IN PROGRESS


public class OverlapConstraint extends Constraint{
	private int indexCourse;
	private int indexLab;
	@Override
	public boolean testHard(int[] node) {
		//Takes care of M/W and Tuesday on the hour
		if(node[indexCourse]==node[indexLab]){return false;}
		
		//Takes care of Tu/Th 
		if(node[indexCourse]+70==node[indexLab]){return false;}			//Labs starting on the hour after the half hours
		else if(node[indexCourse]-30 == node[indexLab]){return false;}	//Labs starting on the hour before the half hours
		else if(node[indexCourse]+100 == node[indexLab]){return false;}	//Labs starting on the hour after the even hours
		
		//Takes care of F
		int fridayConvert = node[indexLab]-4800;
		if(node[indexCourse]+100 == fridayConvert){return false;}		//Labs starting on the hour after the course on fridays
		return true;
	}

}
