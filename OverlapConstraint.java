//WORK IN PROGRESS


public class OverlapConstraint extends Constraint{
	private int indexCourse;
	private int indexLab;
	@Override
	public boolean testHard(int[] node) {
		//System.out.println("Course time:" + node[indexCourse] + " Lab time: " + node[indexLab] );
		//If either slot is still unassigned
		if(node[indexCourse] == 0 || node[indexLab]==0){return true;}	//Return that they do not overlap
		
		//Takes care of M/W and Tuesday on the hour
		if(node[indexCourse]==node[indexLab]){return false;}
		
		if(node[indexLab]>2400){
			//Takes care of Tu/Th 
			if(node[indexCourse]+70==node[indexLab]){return false;}			//Labs starting on the hour after the half hours
			else if(node[indexCourse]-30 == node[indexLab]){return false;}	//Labs starting on the hour before the half hours
			else if(node[indexCourse]+100 == node[indexLab]){return false;}	//Labs starting on the hour after the even hours
		
			//Takes care of F
			int fridayConvert = node[indexLab]-4800;
			if(node[indexCourse]+100 == fridayConvert || node[indexCourse]== fridayConvert){return false;}		//Labs starting on the hour after the course on fridays
		}
		return true;
	}

	public OverlapConstraint(int indexCourseIn, int indexLabIn){
		this.indexCourse = indexCourseIn;
		this.indexLab = indexLabIn;
	}
	
	public String toString(){
		return "OverlapConstratint:" + indexCourse + "labIndex:"+ indexLab;
	}
}
