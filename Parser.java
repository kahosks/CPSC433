
import java.io.*;
public class Parser2 {
	
	String[] courses;
	String[] labs;
	String[] slots;
	
	class Slot {
		public String name; // Could be a lab/lecture
		public int course_min;
		public int course_max;
		public int lab_min;
		public int lab_max;
	}
		
	
	//Basically used as a struct to put in information from the file
	// FieldA and FieldB can be either a course or a lab
	//We can tell whether they are a course or a lab because of the identifiers
	//EG TUT for lab and LEC for lecture
	class NotCompatible {
		public String fieldA;
		public String fieldB;
		
	}
	
	class Unwanted {
		public String identifier;
		public String slotDay;
		public String slotTime; 
		
	}
	
	class Preferences {
		public String slotDay;
		public String slotTime;
		public String courseIdentifier;
		public int preferrenceValue;
	}
	
	// FieldA and FieldB can be either a course or a lab
	//We can tell whether they are a course or a lab because of the identifiers
	//EG TUT for lab and LEC for lecture
	class Pair {
		public String fieldA;
		public String fieldB;
	}
	
	class PartialAssign {
		public String courseIdentifier;
		public String slotDay;
		public String slotTime;
	}
	
	//Start of the parsing algorithm
	//The boolean result will be used to tell if the file is legit
	boolean doParsing(File file) {
		
		BufferedReader br = new BufferedReader( new FileReader(file));
		
		//create a loop to read the file
		
		//Parse!
		
		//Could use a switch statement below to make it easier to use
		//or some other parse technique
		
		//read in Course slots:
		
		//read in Lab slots:
		
		//read in courses:
		
		//read in Labs:
		
		//read in Not compatible:
		
		//read in Unwanted:
		
		//read in Preferences:
		
		//read in pair:
		
		//read in partial assignment: 
		
		
	}
	
	
	//used to output all the information
	public void toString() {
		
	}
	
	
	public static void main(String[] args) {
	
		// Add support to add the file in the args
		
		
		//Ask the user if they want to modify the eval function
		
		
	}
	
	
}
