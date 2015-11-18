/* Class Parser that takes input from a file, parses it, and 
 * puts into vectors.  Super duper long and not necessarily the most
 * efficient thing, by all means please change it/modify it!
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class Parser {
	File file;
	BufferedReader br;
	//flags for checking when not to use a method.  Probably not need.
	String name;

	Slot[] M = initializeDay("MO", 13);
	Slot[] T = initializeDay("TU", 13);	//will have to put labs in Slot, possible in two Slot
	Slot[] F = initializeDay("FR", 13);	//will only be for labs because all lectures will be in M
	
	Vector<Class> labsAndCourses = new Vector<Class>();		//hold labs and classes
	Vector<PairedCourseClass> notCompatible = new Vector<PairedCourseClass>(); 	//hold not compatible classes; see around line 333
	Vector<PairedCourseClass> pairs = new Vector<PairedCourseClass>();	//hold pairs; see around line 340
	Vector<Preference> preferences = new Vector<Preference>();	//hold preferences
	Vector<ParserClass> unwanted = new Vector<ParserClass>();	//hold unwanted
	Vector<ParserClass> partassign = new Vector<ParserClass>();	//hold partassign
	Vector<Slot[]> days = new Vector<Slot[]>();	//hold M,T,F when returning parse()
	
	
	//constructor.  Add more things potentially.
	public Parser(String filename) throws SchedulerException {
		file = new File(filename);
		if (!file.exists()) {
			throw new SchedulerException("Error, file doesn't exist. ");
		}
			try {
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
	}		
	//ignore.  This method runs through parts of file and obtains information.
	//The return type of Vector[] is just for use with Schedule.java demo.
	public void parse() throws SchedulerException {
		try {
			String s;
				if ((s = br.readLine()) != null) {
					checkName(s);
				}
				//go through methods until reach end of file.
			//while ((s = br.readLine()) != null) {
				br.readLine();
				initiateCourseSlot(br.readLine());
				initiateLabSlot(br.readLine());
				initiateCourses(br.readLine());
				initiateLabs(br.readLine());
				initiateNC(br.readLine());
				initiateUnwanted(br.readLine());
				initiatePreferences(br.readLine());
				initiatePair(br.readLine());
				initiatePartassign(br.readLine());
			//}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerException("Error with parsing file." + e.getMessage()); 
		}
	}
	//Gets name of file 
	private void checkName(String s) throws SchedulerException {
		try {
			while (!(s.contains("Name:"))) {
				br.readLine();
			}
			name = br.readLine().trim();
		}
		catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}

	//Gets courses 
	private void initiateCourseSlot(String s) throws SchedulerException {
		if (s.contains("Course slots:")) {
			String line;
			try {
				//Go until reach new line.  Not sure if this is correct implementation.
				while (!((line = br.readLine()).equals(""))) {
					//split into day, time, coursemin, coursemax
					String[] lineArr = line.split(",");
					//gets index of slot based on hour class begins
					int index = getSlotIndex(lineArr[1]);
					//add days to proper array.  Ignore this.
					addCourseToDay(lineArr[0], index, Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
				}	
			} 
			catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
		}
	}
	
	//basically same as getting the class slot
	private void initiateLabSlot(String s) throws SchedulerException {
			if (s.contains("Lab slots:")) {
				String line;
				try {
					while (!((line = br.readLine()).equals(""))) {
					//split into day, time, coursemin, coursemax
					String[] lineArr = line.split(",");
					int index = getSlotIndex(lineArr[1].trim());
					addLabToDay(lineArr[0], index, Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
				}	
			}
				catch (IOException e) {	
					throw new SchedulerException("Error: " + e.getMessage());
				}
		}
	}
	//gets the courses and adds them to the labsAndCourses vector.
	private void initiateCourses(String s) throws SchedulerException {
		if(s.contains("Courses:")) {
			try {
				String line;
				while (!((line = br.readLine()).equals(""))) {
					labsAndCourses.add(new Course(line));
				}
			}
			catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
		}
	}	
	//same as getCourses
	private void initiateLabs(String s) throws SchedulerException {
			if(s.contains("Labs:")) {
				try {
					String line;
					while (!((line = br.readLine()).equals(""))) {
						labsAndCourses.add(new Lab(line));
					}
				}
				catch (IOException e) {
					throw new SchedulerException("Error: " + e.getMessage());
				}
			}
	}
		
	private void initiateUnwanted(String s) throws SchedulerException {
		if (s.contains("Unwanted:")) {
			try {
				String line;
				while (!((line = br.readLine()).equals(""))) {
					String[] input = line.split(",");
					unwanted.add(new ParserClass(input[0], input[1], input[2]));
				}
			} catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
		}			
	}
	private void initiatePreferences(String s) throws SchedulerException {
		if (s.contains("Preferences:")) {
			try {
				String line;
				while (!((line = br.readLine()).equals(""))) {
					String[] input = line.split(",");
					preferences.add(new Preference(input[0], input[1], input[2], input[3]));		
				}
			} catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
		}
	}	
	
	//Gets the not compatible pairs.  This can be cleaned up so that duplication of this code and getPair 
	//code can be reduced.
	private void initiateNC(String s) throws SchedulerException {
		if (s.contains("Not compatible:")) {
			//parse and add the pairs to notCompatible vector		
			addTupleToVector(s,notCompatible);
		}			
	}
	
	//get pair method.  This is a duplication of the code for getNC, so basically just need to 
	//make a method out of the comparing parts to add to the appropriate places (move the try/catch
	//block into its own method that both getNC and getPair can call.  Have the vectors (notCompatible or 
	//pairs, passed as an argument.
	private void initiatePair(String s) throws SchedulerException {
		if (s.contains("Pair:")) {
			addTupleToVector(s, pairs);
		}
	}
	
	//Method that adds a tuple to vector.  Used for Pair and NotCompatible (PairCourseClass)
	private void addTupleToVector(String s, Vector<PairedCourseClass> vector) throws SchedulerException{
		try {
			String line;
			//booleans to check which classes are courses/labs
			boolean isCourseA = false;
			boolean isCourseB = false;
			while (!((line = br.readLine()).equals(""))) {
				String[] input = line.split("[ ]*,[ ]*");
				//check if first class is not a lab
				if (!input[0].contains("LAB") && !input[0].contains("TUT")) {
					isCourseA = true;
				}
				//check if second class is not a lab
				if (!input[1].contains("LAB") && !input[1].contains("TUT")) {
					isCourseB = true;
				}
				//add to vector when both are courses
				if (isCourseA && isCourseB) {
					vector.add(new PairedCourseClass(new Course(input[0].trim()), new Course(input[1].trim())));
				}
				//second is a lab
				else if (isCourseA && !isCourseB) {
					vector.add(new PairedCourseClass(new Course(input[0].trim()), new Lab(input[1].trim())));
				}
				//first is a lab
				else if (!isCourseA && isCourseB) {
					vector.add(new PairedCourseClass(new Lab(input[0].trim()), new Course(input[1].trim())));
				}
				//both are labs
				else {
					vector.add(new PairedCourseClass(new Lab(input[0].trim()), new Lab(input[1].trim())));
				}
				//set booleans back to false
				isCourseA = false;
				isCourseB = false;
			}
		} catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}
	//method that gets partassign and stores in array.
	private void initiatePartassign(String s) throws SchedulerException {

		if (s.contains("Partial assignments:")) {	
		String line;
			try {
				while ((line = br.readLine()) != null && !(line.equals(""))) {
						String[] input = line.split(",");
						partassign.add(new ParserClass(input[0], input[1], input[2]));		
					}
			} catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}		
		}
	}
	
	//adds course to specific day.  Adds the lab min and max values.
	private void addCourseToDay(String day, int index, int coursemax, int coursemin) throws SchedulerException {
		switch(day) {
		case "MO":
			M[index].addCourseInfo(coursemin, coursemax);
			break;
		case "TU":
			//need to fix for this
			T[index].addCourseInfo(coursemin, coursemax); 
			break;	
		case "FR":
			F[index].addCourseInfo(coursemin, coursemax);
			break;		
		default:
			throw new SchedulerException("Error: Course info not added to day. ");
		}
	}
	
	//Add labs to day.  Adds the lab min and max values.
	private void addLabToDay(String day, int index, int labmax, int labmin) throws SchedulerException {
		switch(day) {
		case "MO":
			M[index].addLabInfo(labmin,labmax);
			break;
		case "TU":
			T[index].addLabInfo(labmin,labmax);
			break;
			
		case "FR":
			F[index].addLabInfo(labmin, labmax);
			break;
			
		default:
			throw new SchedulerException("Error: lab info not added to day.");
		}
	}
	//Initializes array of days in terms of slots.  We don't need to use this.
	private Slot[] initializeDay(String day, int size) {
		//new slot array.  This is assuming slots are one hour long.
		//Will need separate implementation for courses on Tuesday.
		Slot[] Slot = new Slot[size];
		//Classes begin at 8 am 
		int startHour = 8;
		for (int i = 0; i < size; i++) {
			//make time as a string
			String timeString = startHour + ":00";
			//add info to the slot.
			Slot[i] = new Slot(day, timeString);
			startHour++;
		}
		return Slot;
	}
	//Get index of slot in array.
	private int getSlotIndex(String time) {
		String[] hourMin = time.split(":");
		int hour = Integer.parseInt(hourMin[0].trim());
		return hour-8;
	}

	//Return methods for respective vectors
	public Vector<Class> getLabsAndCourses() {
		return labsAndCourses;
	}
	public Vector<PairedCourseClass> getNC() {
		return notCompatible;
	}
	public Vector<PairedCourseClass> getPairs() {
		return pairs;
	}
	public Vector<Preference> getPreferences() {
		return preferences;
	}
	public Vector<ParserClass> getUnwanted() {
		return unwanted;
	}
	public Vector<ParserClass> getPartassign() {
		return partassign;
	}
	//methods that return arrays for M, T, or F.
	public Slot[] getMO () {
		return M;
	}
	public Slot[] getTU () {
		return T;
	}
	public Slot[] getFR () {
		return F;
	}
	
	//Generic class for Pair and Unwanted
	public class PairedCourseClass {
		Class a;
		Class b;
		public PairedCourseClass(Class a, Class b) {
			this.a = a;
			this.b = b;			
		}
		//Gets the class pair c is a pair with.
		public Class getOtherClass(Class c) throws SchedulerException {
			if (a.toString().equals(c.toString())) {
				return b;
			}
			else if (b.toString().equals(c.toString())) {
				return a;
			}
			else {
				throw new SchedulerException("Error: null other class value in class Pair.");
			}
		}
		public String toString() {
			return a.toString()+", " +b.toString();
		}
	}
	
	//Generic class for PartAssign and Unwanted
	public class ParserClass {
		String identifier;
		String day;
		String time;
		
		//Constructor
		public ParserClass(String identifier, String day, String time) {
			this.identifier = identifier.trim();
			this.day = day.trim();
			this.time = time.trim();
		}
		//returns day
		public String getDay() {
			return day;
		}
		//returns time
		public String getTime() {
			return time;
		}
		//returns course/lab identifier
		public String getIdentifier() {
			return identifier;
		}
		public String toString() {
			return identifier +", " +day + ", " + time;
		}
	}

	//Class Preference that extends ParserClass
	public class Preference extends ParserClass{
		int prefValue;
		
		//Constructor.  
		public Preference(String day, String time, String identifier, String prefValue) {
			super(day, time, identifier);
			this.prefValue = Integer.parseInt(prefValue.trim());	//parse string to an integer
		}

		//Gets the preference value
		public int getPrefValue() {
			return prefValue;
		}
		public String toString() {
			return identifier +", " +day + ", " + time + ", " + prefValue;
		}
	}
}
