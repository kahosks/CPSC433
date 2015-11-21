/* Class Parser that takes input from a file, parses it, and 
 * puts into vectors.  Super duper long and not necessarily the most
 * efficient thing, by all means please change it/modify it!
 */
/**
 * Parser Class that takes input from a file and parses it.
 * @author CPSC 433 Toshibe
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class Parser {
	private File file;
	private BufferedReader br;
	private String name;	//Name for name field.

	private Slot[] M = initializeDay("MO", 13);	//Monday courses and labs array
	private Slot[] TCourses = initializeTuesday();	//Tuesday courses array
	private Slot[] TLabs = initializeDay("TU", 13);	//Tuesday labs array
	private Slot[] F = initializeDay("FR", 13);	//Friday Labs array
	
	private Vector<Class> labsAndCourses = new Vector<Class>();		//hold labs and classes
	private Vector<PairedCourseClass> notCompatible = new Vector<PairedCourseClass>(); 	//hold not compatible classes; see around line 333
	private Vector<PairedCourseClass> pairs = new Vector<PairedCourseClass>();	//hold pairs; see around line 340
	private Vector<Preference> preferences = new Vector<Preference>();	//hold preferences
	private Vector<ParserClass> unwanted = new Vector<ParserClass>();	//hold unwanted
	private Vector<ParserClass> partassign = new Vector<ParserClass>();	//hold partassign	
	
	/**
	 * Constructor with String argument.
	 * @param filename	String of name of file to read from.
	 * @throws SchedulerException	Thrown if file not found.
	 */
	public Parser(String filename) throws SchedulerException {
		file = new File(filename);

		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}		
	//ignore.  This method runs through parts of file and obtains information.
	//The return type of Vector[] is just for use with Schedule.java demo.
	/**
	 * Method that parses the file.
	 * @throws SchedulerException	Thrown by called methods.
	 */
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
	/**
	 * Gets the name under the "Name:" header.
	 * @param s	String read from file.
	 * @throws SchedulerException	Thrown if there is an IOException
	 */
	private void checkName(String s) throws SchedulerException {
		try {
			while (!(s.contains("Name:"))) {
				//keep looping through file until "Name:"
				br.readLine();
			}
			name = br.readLine().trim();
		}
		catch (NullPointerException e) {
			throw new SchedulerException("End of file reached.  Unable to find \"Name\"");
		}
		catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Gets the course min/max values for a slot.
	 * @param s	Line of file read by buffered reader.
	 * @throws SchedulerException	Thrown if IOException
	 */
	private void initiateCourseSlot(String s) throws SchedulerException {
		if (s.contains("Course slots:")) {
			String line;
			try {
				//Go until reach new line.  Not sure if this is correct implementation.
				while (!((line = br.readLine()).equals(""))) {
					//split into day, time, coursemin, coursemax
					String[] lineArr = line.split(",");
					//gets index of slot based on hour class begins
					int index = getSlotIndex(true, lineArr[1]);
					//add days to proper array.  Ignore this.
					addCourseToDay(lineArr[0], index, Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
				}	
			} 
			catch (IOException e) {
				throw new SchedulerException("Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Gets the lab min/max values for a slot.
	 * @param s	Line of file read by buffered reader.
	 * @throws SchedulerException	Thrown if IOException
	 */
	private void initiateLabSlot(String s) throws SchedulerException {
			if (s.contains("Lab slots:")) {
				String line;
				try {
					while (!((line = br.readLine()).equals(""))) {
					//split into day, time, coursemin, coursemax
					String[] lineArr = line.split(",");
					int index = getSlotIndex(false, lineArr[1].trim());
					addLabToDay(lineArr[0], index, Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
				}	
			}
				catch (IOException e) {	
					throw new SchedulerException("Error: " + e.getMessage());
				}
		}
	}
	
	/**
	 * Gets the courses from the file and adds them to the labsAndCourses vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
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
	/**
	 * Gets the labs from the file and adds them to the labsAndCourses vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
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
	/**
	 * Gets the unwanted slot times from the file and adds them to the unwanted vector.	
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
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
	/**
	 * Gets the preferences from the file and adds them to the preferences vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
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
	/**
	 * Gets the not compatible pairs and adds them as a tuple to the notCompatible vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */

	private void initiateNC(String s) throws SchedulerException {
		if (s.contains("Not compatible:")) {
			//parse and add the pairs to notCompatible vector		
			addTupleToVector(s,notCompatible);
		}			
	}
	
	/**
	 * Gets the pairs from the file and adds them to the pairs vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiatePair(String s) throws SchedulerException {
		if (s.contains("Pair:")) {
			addTupleToVector(s, pairs);
		}
	}
	
	/**
	 * Gets partassign values from file and stores in partassign vector.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
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
	
	/**
	 * Adds a tuple to vector.  This is used for classes PairCourseClass (dealing with pairs and 
	 * not compatible).
	 * @param s	Line read from file.
	 * @param vector	Vector you want to add the tuple to
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void addTupleToVector(String s, Vector<PairedCourseClass> vector) throws SchedulerException{
		try {
			String line;
			//booleans to check which classes are courses/labs
			boolean isCourseA = false;
			boolean isCourseB = false;
			while (!((line = br.readLine()).equals(""))) {
				//Split and get rid of extra whitespace.
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

	/**
	 * Adds course information to specific day and slot.
	 * @param day	Day to add information.
	 * @param index	Index in Slot array
	 * @param coursemax		int of maximum courses
	 * @param coursemin		int of minimum courses
	 * @throws SchedulerException	Thrown if unable to add information to day.
	 */
	private void addCourseToDay(String day, int index, int coursemax, int coursemin) throws SchedulerException {
		switch(day) {
		case "MO":
			M[index].addCourseInfo(coursemin, coursemax);
			break;
		case "TU":
			//need to fix for this
			TCourses[index].addCourseInfo(coursemin, coursemax); 
			break;	
		case "FR":
			F[index].addCourseInfo(coursemin, coursemax);
			break;		
		default:
			throw new SchedulerException("Error: Course info not added to day. ");
		}
	}
	
	/**
	 * Adds lab information to specific day and slot.
	 * @param day	Day to add slot information.
	 * @param index	Index in Slot array.
	 * @param labmax	int of maximum labs.
	 * @param labmin	int of minimum labs.
	 * @throws SchedulerException	Thrown if unable to add information to day.
	 */
	private void addLabToDay(String day, int index, int labmax, int labmin) throws SchedulerException {
		switch(day) {
		case "MO":
			M[index].addLabInfo(labmin,labmax);
			break;
		case "TU":
			TLabs[index].addLabInfo(labmin,labmax);
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
	private int getSlotIndex(boolean isTuesCourse, String time) throws SchedulerException {
		if (isTuesCourse) {
			return getTuesSlotIndex(time);
		}
		String[] hourMin = time.split(":");
		int hour = Integer.parseInt(hourMin[0].trim());
		return hour-8;
	}
	//Returns an array of Slot based on day and time.  NOTE:  This does not account for Tuesday's 
	// course times.  For that, can make a different array, or we can modify Tuesday to work with this
	// array and have flags for overlaps and such.
	public static Slot[] initializeTuesday() {
		int size = 8;	// 13 because 21-8= 13 (end time - start time)
		Slot[] Slot = new Slot[size];	//array of smpty Slot
		int startHour = 8;	//8 because earliest class time is 8 am
		for (int i = 0; i < size; i++) {
			String timeString;
			if (i % 2 == 0) {
				System.out.println("true");
				timeString = startHour + ":00";
			}
			else {
				System.out.println("false");
				timeString = startHour + ":30";
				startHour++;
			}
			Slot[i] = new Slot("TU", timeString);	//add information to slot
			startHour++;
			
		}
		return Slot;
	}
	//Gets index of slot based on hour.  NOTE: Use this function when using an array/list of Slot
	// based on days (ex. 3 arrays, one for MO, one for TU, one for FR, with array of Slot initialized
	// based on time of day.
	public int getTuesSlotIndex(String time) throws SchedulerException {
		String[] hourMin = time.split(":");
		int hour = Integer.parseInt(hourMin[0].trim());
		switch(hour) {
			case 8:
			case 9:
				return hour-8;
			case 11:
			case 12:
				return hour-9;
			case 14:
			case 15:
				return hour -10;
			case 17:
			case 18:
				return hour-11;
			default: throw new SchedulerException("Unable to get Tuesday slot index.");
		}
	}

	//Return methods for respective vectors
	public String getName() {
		return name;
	}
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
	public Slot[] getTCourses () {
		return TCourses;
	}
	public Slot[] getTLabs() {
		return TLabs;
	}
	public Slot[] getFLabs () {
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
		public Class getOtherClass(Class c) {
			if (a.toString().equals(c.toString())) {
				return b;
			}
			else if (b.toString().equals(c.toString())) {
				return a;
			}
			else {
				return null;
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
