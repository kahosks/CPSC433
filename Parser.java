/* Class Parser that takes input from a file, parses it, and 
 * puts into ArrayLists.  Super duper long and not necessarily the most
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
import java.util.ArrayList;


public class Parser {
	private File file;
	private BufferedReader top;
	private String name;	//Name for name field.

	private Slot[] M = initializeDay("MO", 13);	//Monday courses and labs array
	private Slot[] TCourses = initializeTuesday();	//Tuesday courses array
	private Slot[] TLabs = initializeDay("TU", 13);	//Tuesday labs array
	private Slot[] F = initializeDay("FR", 13);	//Friday Labs array
	
	private ArrayList<Class> labsAndCourses = new ArrayList<Class>();		//hold labs and classes
	private ArrayList<PairedCourseClass> notCompatible = new ArrayList<PairedCourseClass>(); 	//hold not compatible classes; see around line 333
	private ArrayList<PairedCourseClass> pairs = new ArrayList<PairedCourseClass>();	//hold pairs; see around line 340
	private ArrayList<Preference> preferences = new ArrayList<Preference>();	//hold preferences
	private ArrayList<ParserClass> unwanted = new ArrayList<ParserClass>();	//hold unwanted
	private ArrayList<ParserClass> partassign = new ArrayList<ParserClass>();	//hold partassign	
	
	/**
	 * Constructor with String argument.
	 * @param filename	String of name of file to read from.
	 * @throws SchedulerException	Thrown if file not found.
	 */
	public Parser(String filename) throws SchedulerException {
		file = new File(filename);

		try {
			top = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}		
	//ignore.  This method runs through parts of file and obtains information.
	//The return type of ArrayList[] is just for use with Schedule.java demo.
	/**
	 * Method that parses the file.
	 * @throws SchedulerException	Thrown by called methods.
	 */
	public void parse() throws SchedulerException {
		try {
			boolean name = false, courseSlot = false, labSlot = false, courses = false, labs = false, notCompat = false, unwanted = false, preferences = false, pair = false, partAssign = false;
			BufferedReader br = top;
			String line = br.readLine();
			while(!(name && courseSlot && labSlot && courses && labs && notCompat && unwanted && preferences && pair && partAssign)) {
				if (line.equals("Name:")) {
					name = true;
				} else if (line.equals("Course slots:")){
					courseSlot = true;
					initiateCourseSlot(br);
				} else if (line.equals("Lab slots:")) {
					labSlot = true;
					initiateLabSlot(br);
				} else if (line.equals("Courses:")) {
					courses = true;
					initiateCourses(br);
				} else if (line.equals("Labs:")) {
					labs = true;
					initiateLabs(br);
				} else if (line.equals("Not compatible:")) {
					notCompat = true;
					addTupleToArrayList(notCompatible, br);
				} else if (line.equals("Unwanted:")) {
					unwanted = true;
					initiateUnwanted(br);
				} else if (line.equals("Preferences:")) {
					preferences = true;
					initiatePreferences(br);
				} else if (line.equals("Pair:")) {
					pair = true;
					addTupleToArrayList(pairs, br);
				} else if (line.equals("Partial assignments:")) {
					partAssign = true;
					initiatePartassign(br);
				}
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerException("Error with parsing file." + e.getMessage()); 
		}
	}
	
	/*private BufferedReader prepBufferedReader(BufferedReader br, String s) throws SchedulerException {
		try {
			while (!br.readLine().equals(s)) {}
		}catch (NullPointerException e) {
			throw new SchedulerException("End of file reached.  Unable to find \"" + s + "\"");
		} catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
		return br;
	}*/
	
	
	/**
	 * Gets the name under the "Name:" header.
	 * @param s	String read from file.
	 * @throws SchedulerException	Thrown if there is an IOException
	 */
	/*private void checkName(BufferedReader br) throws SchedulerException {
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
	}*/
	
	/**
	 * Gets the course min/max values for a slot.
	 * @param s	Line of file read by buffered reader.
	 * @throws SchedulerException	Thrown if IOException
	 */
	private void initiateCourseSlot(BufferedReader br) throws SchedulerException {
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
	
	/**
	 * Gets the lab min/max values for a slot.
	 * @param s	Line of file read by buffered reader.
	 * @throws SchedulerException	Thrown if IOException
	 */
	private void initiateLabSlot(BufferedReader br) throws SchedulerException {
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
	

	
	/**
	 * Gets the courses from the file and adds them to the labsAndCourses ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiateCourses(BufferedReader br) throws SchedulerException {
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
	/**
	 * Gets the labs from the file and adds them to the labsAndCourses ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiateLabs(BufferedReader br) throws SchedulerException {
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
	/**
	 * Gets the unwanted slot times from the file and adds them to the unwanted ArrayList.	
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiateUnwanted(BufferedReader br) throws SchedulerException {
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
	/**
	 * Gets the preferences from the file and adds them to the preferences ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiatePreferences(BufferedReader br) throws SchedulerException {
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
	/**
	 * Gets the not compatible pairs and adds them as a tuple to the notCompatible ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 *//*

	private void initiateNC(BufferedReader br) throws SchedulerException {
		//parse and add the pairs to notCompatible ArrayList	
		try {
			String line;
			while (!((line = br.readLine()).equals(""))) {
				
			}
		} catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}
	
	*//**
	 * Gets the pairs from the file and adds them to the pairs ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 *//*
	private void initiatePair(BufferedReader br) throws SchedulerException {
		try {
			String line;
			while (!((line = br.readLine()).equals(""))) {
				
			}
		} catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	}*/
	
	/**
	 * Gets partassign values from file and stores in partassign ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiatePartassign(BufferedReader br) throws SchedulerException {
	
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
	
	/**
	 * Adds a tuple to ArrayList.  This is used for classes PairCourseClass (dealing with pairs and 
	 * not compatible).
	 * @param s	Line read from file.
	 * @param vector	ArrayList you want to add the tuple to
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void addTupleToArrayList(ArrayList<PairedCourseClass> ArrayList, BufferedReader br) throws SchedulerException{
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
				//add to ArrayList when both are courses
				if (isCourseA && isCourseB) {
					ArrayList.add(new PairedCourseClass(new Course(input[0].trim()), new Course(input[1].trim())));
				}
				//second is a lab
				else if (isCourseA && !isCourseB) {
					ArrayList.add(new PairedCourseClass(new Course(input[0].trim()), new Lab(input[1].trim())));
				}
				//first is a lab
				else if (!isCourseA && isCourseB) {
					ArrayList.add(new PairedCourseClass(new Lab(input[0].trim()), new Course(input[1].trim())));
				}
				//both are labs
				else {
					ArrayList.add(new PairedCourseClass(new Lab(input[0].trim()), new Lab(input[1].trim())));
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
			default: throw new SchedulerException("Unable to get Tuesday slot index." + time);
		}
	}

	//Return methods for respective ArrayLists
	public String getName() {
		return name;
	}
	public ArrayList<Class> getLabsAndCourses() {
		return labsAndCourses;
	}
	public ArrayList<PairedCourseClass> getNC() {
		return notCompatible;
	}
	public ArrayList<PairedCourseClass> getPairs() {
		return pairs;
	}
	public ArrayList<Preference> getPreferences() {
		return preferences;
	}
	public ArrayList<ParserClass> getUnwanted() {
		return unwanted;
	}
	public ArrayList<ParserClass> getPartassign() {
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
