
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
import java.util.Arrays;

//import HardConstraints.Constraint;
//import HardConstraints.CourseCourseConstraint;
//import HardConstraints.CourseMaxConstraint;
//import HardConstraints.CourseTimeConstraint;
//import HardConstraints.LabMaxConstraint;
//import junit.framework.Test;


public class Parser {
	private File file;
	private BufferedReader top;
	private String name;	//Name for name field.

	private ArrayList<Slot> M = new ArrayList<Slot>();	//Monday courses array
	private ArrayList<Slot> MLabs = new ArrayList<Slot>();	//Monday courses array
	private ArrayList<Slot> TCourses = new ArrayList<Slot>();	//Tuesday courses array
	private ArrayList<Slot> TLabs = new ArrayList<Slot>();	//Tuesday labs array
	private ArrayList<Slot> F = new ArrayList<Slot>();	//Friday Labs array
	
	private ArrayList<Class> labsAndCourses = new ArrayList<Class>();		//hold labs and classes
	private ArrayList<PairedCourseClass> notCompatible = new ArrayList<PairedCourseClass>(); 	//hold not compatible classes; see around line 333
	private ArrayList<PairedCourseClass> pairs = new ArrayList<PairedCourseClass>();	//hold pairs; see around line 340
	private ArrayList<Preference> preferences = new ArrayList<Preference>();	//hold preferences
	private ArrayList<ParserClass> unwanted = new ArrayList<ParserClass>();	//hold unwanted
	private ArrayList<ParserClass> partassign = new ArrayList<ParserClass>();	//hold partassign	
	
	private int[] initialProblem;
	private String[] indexArray;
	private Constraint[] hardConstraints;
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
			createInitialProbAndIndex();
			makeConstraints();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerException("Error with parsing file." + e.getMessage()); 
		}
	}
		


	private void makeConstraints(){
		ArrayList<Constraint> hcArrayList = new ArrayList<Constraint>();
		//For each of the non-compatible course pairs
		for(int i = 0; i<notCompatible.size();i++){
			String firstName=notCompatible.get(i).getFirstPair().toString().trim();		//SWITCH TO getFirstPair()
			String secondName=notCompatible.get(i).getFirstPair().trim();	//SWITCH TO getSecondPair()
			int indexA = 0;
			int indexB = 0;

			for (int j = 2;j< indexArray.length;j++){
				String indexString = indexArray[j];
				if(indexString.equalsIgnoreCase(firstName)){
					indexA=j;
				}
				else if(indexString.equalsIgnoreCase(secondName)){
					indexB=j;
				}
			}
			if(indexA>0 && indexB>0){
				hcArrayList.add(new CourseCourseConstraint(indexA, indexB));
			}
			else{
				System.out.println("Error making notCompatibleConstraint");
			}
		}

		//Now do all of the unwanted constraints
		for(int i = 0; i<unwanted.size();i++){
			ParserClass unwantedEx = unwanted.get(i);
			int dayOffset;
			if(unwantedEx.day.equalsIgnoreCase("MO")){
				dayOffset = 0;
			}
			else if(unwantedEx.day.equalsIgnoreCase("TU")){
				dayOffset = 2400;
			}
			else{
				dayOffset = 4800;
			}
			dayOffset +=unwantedEx.getTimeInt();
			for(int j=2; j< indexArray.length; j++){
				if(indexArray[j].equalsIgnoreCase(unwantedEx.getIdentifier().trim())){
					hcArrayList.add(new CourseTimeConstraint(j,dayOffset));
				}
			}
			
		}
		//Now do the Course max and lab max constraints
		//Need all of the lecture/lab indexes 
		ArrayList<Integer> courseIndexesList = new ArrayList<Integer>();
		ArrayList<Integer> labIndexesList = new ArrayList<Integer>();
		
		for(int i = 2; i < indexArray.length; i++){
			if(indexArray[i].contains("LAB")||indexArray[i].contains("TUT")){
				labIndexesList.add(i);
			}
			else{
				courseIndexesList.add(i);
			}
		}
		courseIndexes = new int[courseIndexesList.size()];
		for(int i = 0; i<courseIndexesList.size();i++){
			courseIndexes[i]=courseIndexesList.get(i);
		}
		labIndexes = new int[labIndexesList.size()];
		for(int i = 0; i<labIndexesList.size();i++){
			labIndexes[i]=labIndexesList.get(i);
		}
		CourseMaxConstraint.setCourseIndexes(courseIndexes);
		LabMaxConstraint.setLabIndexes(labIndexes);
		
		for(int i = 0; i<M.size(); i++){
			hcArrayList.add(new CourseMaxConstraint(M.get(i)));
		}
		for(int i = 0; i<TCourses.size(); i++){
			hcArrayList.add(new CourseMaxConstraint(TCourses.get(i)));
		}
		for(int i = 0; i<MLabs.size(); i++){
			hcArrayList.add(new LabMaxConstraint(MLabs.get(i)));
		}
		for(int i = 0; i<TLabs.size(); i++){
			hcArrayList.add(new LabMaxConstraint(TLabs.get(i)));
		}
		for(int i = 0; i<F.size(); i++){
			hcArrayList.add(new LabMaxConstraint(F.get(i)));
		}
		
		//Now make tutorial course overlap
		//Now make CPSC813/913
		//Now tu @11 no lectures
		for(int i=0; i<courseIndexes.length; i++){
			hcArrayList.add(new CourseTimeConstraint(courseIndexes[i], 3500)); //No courses at 11 on Tu/Th
		}
		//Evening courses
		for(int i=0;i<labsAndCourses.size();i++){
			Class posEvening = labsAndCourses.get(i);
			if(posEvening.getLectureNum().trim().charAt(0)=='9'){
				for(int j = 0; j<indexArray.length;j++){
					if(indexArray[j].equalsIgnoreCase(posEvening.toString().trim())){
						hcArrayList.add(new EveningCourseConstraint(j));
					}
				}
			}
		}
			
		
		//Now make 500 courses constraints
		ArrayList<Integer> fiveHundredCoursesList = new ArrayList<Integer>();
		
		for(int i=2;i<indexArray.length;i++){
			String courseId = indexArray[i];
			String[] components = courseId.split(" ");
			if((components[1].charAt(0) == '5' && !courseId.contains("TUT"))&& !courseId.contains("LAB")){
				
				fiveHundredCoursesList.add(i);
			}
		}
		fiveHundredIndexes = new int[fiveHundredCoursesList.size()];
		for(int i = 0; i<fiveHundredIndexes.length;i++){
			fiveHundredIndexes[i]=fiveHundredCoursesList.get(i);
		}
		
		FiveHundredConstraint.setFiveHundIndexes(fiveHundredIndexes);
		hcArrayList.add(new FiveHundredConstraint());
//TODO WORKING HERE::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::		
/*		//Make sure that labs and courses don't overlap
		for(int i = 0; i < labIndexes.length; i++){
			String labName = indexArray[labIndexes[i]];
			String[] labNameComponents = labName.split(regex)
			if(labName.contains("LEC")){
				for(int j = 0; j<courseIndexes.length; j++){
					String nameTest = indexArray[courseIndexes[j]];
				}
			}
		}*/
		hardConstraints = hcArrayList.toArray(new Constraint[hcArrayList.size()]);
		
	}
	public Constraint[] getHardConstraints(){
		
		System.out.println(Arrays.toString(hardConstraints));
		System.out.println(Arrays.toString(indexArray));
		System.out.println(Arrays.toString(initialProblem));
		return hardConstraints;
	}
	
	
	
	/**
	 * Gets the name under the "Name:" header.
	 * @param s	String read from file.
	 * @throws SchedulerException	Thrown if there is an IOException
	 */
	
	/**
	 * Creates the initialProblem and indexArray
	 */
	
	private void createInitialProbAndIndex() {
		int size = labsAndCourses.size() + 2;			// the +2 is for the Depth and Eval values
		initialProblem = new int[size];
		indexArray = new String[size];
		
		initialProblem[0] = partassign.size() + 2;		// first slot that needs to be set is after the partial assign and eval + depth
		initialProblem[1] = 0;							// setting eval to 0
		indexArray[0] = "";
		indexArray[1] = "";
		int i = 2;
		
		String day = "";
		
		// this loop puts the partial assign just after the depth/eval
		for(ParserClass pClass: partassign) {
			initialProblem[i] = pClass.getTimeInt();
			day = pClass.getDay();
			if(day.equals("TU")) {
				initialProblem[i] += 2400;
			} else if(day.equals("FR")) {
				initialProblem[i] += 4800;
			}
			indexArray[i] = pClass.getIdentifier();
			i++;
		}

		// creating a list of classes that are not already assigned
		ArrayList<Class> notAssigned = new ArrayList<Class>();
		notAssigned.addAll(labsAndCourses);
		for(Class labCourse: labsAndCourses) {
			for(String assigned: indexArray) {
				if(labCourse.getIdentifier().equals(assigned)) {
					notAssigned.remove(labCourse);
					break;
				}
			}
		}
		
		// add those not assigned classes after the assigned ones, init time to 0
		for(Class labCourse: notAssigned) {
			initialProblem[i] = 0;						// no time is assigned to these labs/courses yet
			indexArray[i] = labCourse.getIdentifier();
			i++;
		}
	}
	
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
				//System.out.println(line.charAt(line.length()-1));  // could use this to find : at end of line and stop on that instead of an empty line
				//split into day, time, coursemin, coursemax
				String[] lineArr = line.replaceAll("\\s+"," ").split(",");
				//gets index of slot based on hour class begins
				//int index = getSlotIndex(true, lineArr[0], lineArr[1].trim());
				//add days to proper array.  Ignore this.
				addCourseToDay(lineArr[0].trim(), lineArr[1].trim(), Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
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
				String[] lineArr = line.replaceAll("\\s+"," ").split(",");
				//int index = getSlotIndex(false, lineArr[0], lineArr[1].trim());
				addLabToDay(lineArr[0].trim(), lineArr[1].trim(), Integer.parseInt(lineArr[2].trim()), Integer.parseInt(lineArr[3].trim()));
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
				labsAndCourses.add(new Course(line.replaceAll("\\s+"," ")));
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
				labsAndCourses.add(new Lab(line.replaceAll("\\s+"," ")));
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
				String[] input = line.replaceAll("\\s+"," ").split(",");
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
				String[] input = line.replaceAll("\\s+"," ").split(",");
				preferences.add(new Preference(input[0], input[1], input[2], input[3]));		
			}
		} catch (IOException e) {
			throw new SchedulerException("Error: " + e.getMessage());
		}
	
	}	
	
	
	/**
	 * Gets partassign values from file and stores in partassign ArrayList.
	 * @param s	Line read from file.
	 * @throws SchedulerException	Thrown if IOException occurs.
	 */
	private void initiatePartassign(BufferedReader br) throws SchedulerException {
	
		String line;
		try {
			while ((line = br.readLine()) != null && !(line.equals(""))) {
					String[] input = line.replaceAll("\\s+"," ").split(",");
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
				String[] input = line.replaceAll("\\s+"," ").split("[ ]*,[ ]*");
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
	private void addCourseToDay(String day, String time, int coursemax, int coursemin) throws SchedulerException {
		Slot slot = new Slot(day, time);
		slot.addCourseInfo(coursemin, coursemax);
		switch(day) {
		case "MO":
			M.add(slot);
			break;
		case "TU":
			TCourses.add(slot); 
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
	private void addLabToDay(String day, String time, int labmax, int labmin) throws SchedulerException {
		Slot slot = new Slot(day, time);
		slot.addCourseInfo(labmin, labmax);	//TODO FIX THIS LINE
		slot.addLabInfo(labmin, labmax);
		switch(day) {
		case "MO":
			MLabs.add(slot);
			break;
		case "TU":
			TLabs.add(slot);
			break;
		case "FR":
			F.add(slot);
			break;
			
		default:
			throw new SchedulerException("Error: lab info not added to day.");
		}
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
		Slot[] temp = new Slot[M.size()];
		M.toArray(temp);
		return temp;
	}
	public Slot[] getMLabs () {
		Slot[] temp = new Slot[MLabs.size()];
		MLabs.toArray(temp);
		return temp;
	}
	public Slot[] getTCourses () {
		Slot[] temp = new Slot[TCourses.size()];
		TCourses.toArray(temp);
		return temp;
	}
	public Slot[] getTLabs() {
		Slot[] temp = new Slot[TLabs.size()];
		TLabs.toArray(temp);
		return temp;
	}
	public Slot[] getFLabs () {
		Slot[] temp = new Slot[F.size()];
		F.toArray(temp);
		return temp;
	}
	
	/**
	 * Returns the initial problem as an integer array
	 * 
	 * in the following format
	 * index 0: Depth		the index for the next class that needs to be scheduled it is
	 * 						initially set to 2 + any classes that have been assigned already
	 * 						from part assign
	 * 
	 * index 1: Eval		the value returned from eval
	 * 
	 * index i-j:			times for any classes that already been assigned a time from the part assign,
	 * 						if there are no classes in part assign then this section does not exist
	 * 
	 * index k-l:			all contain 0 as the time for these classes has yet to be assigned
	 * 
	 * Note: classes are in the same order that they appear in the original document except that part assign come before
	 * Note: Times for classes are for Monday if they are < 2400
	 * 								   Tuesday if they are > 2400 and < 4800
	 * 								   Friday if they are > 4800
	 * @return
	 */
	public int[] getInitialProblem() {
		return initialProblem;
	}
	/**
	 * Returns a string array that can be used as a class name index
	 *
	 * in the following format
	 * index 0: 			left as null
	 * 
	 * index 1: 			left as null
	 * 
	 * index i-j:			names for any classes that already been assigned from the part assign,
	 * 						if there are no classes in part assign then this section does not exist
	 * 
	 * index k-l:			then names of the remaining classes
	 * 
	 * Note: classes are in the same order that they appear in the original document except that part assign come before
	 * @return
	 */
	public String[] getIndexArray() {
		return indexArray;
	}
}
