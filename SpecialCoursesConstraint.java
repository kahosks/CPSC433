import java.util.Arrays;

public class SpecialCoursesConstraint extends Constraint{
	private int specialCourseIndex;	//813 or 913
	private int[] specialCoursesPartners; //Courses or labs attached to 813/913
	@Override
	public boolean testHard(int[] node) {

		int specialCourseTime = node[specialCourseIndex];
		//System.out.println(this.toString());
		//System.out.println("specialCourseTime: " + specialCourseTime +" specialCourseCannot: " + node[specialCoursesPartners[0]]);
		
		if(specialCourseTime == 0){return true;}	//If it is unassigned
		else if(specialCourseTime != 4200 ){return false;}	//If they are not at the required time
		
		for(int index: specialCoursesPartners){
			int partnerValue = node[index];
			if(partnerValue==4200){return false;}		//If the assigned special overlaps on any of the partners
			else if(partnerValue == 4100 || partnerValue == 4230){return false;}	//Courses start at different times
		}
		return true;
	}
	
	public String toString(){
		return "Special Course: " + specialCourseIndex + " Cannot overlap with" + Arrays.toString(specialCoursesPartners);
	}
	
	public SpecialCoursesConstraint(int specialIndexIn, int[] specialCoursesPartIn){
		this.specialCourseIndex = specialIndexIn;
		this.specialCoursesPartners = specialCoursesPartIn;
	}
}
