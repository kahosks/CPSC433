
/**
 * Class CommandParser that gets Eval weights and filename.
 * @author CPSC 433 Toshibe
 * 
 */
public class CommandParser {
	
	private String filename;
	/* Set initial default values. */
	private int minfilled =1;
	private int pen_minfilled = 0;
	private int pref =1;
	//private int pen_pref = 0;
	private int pair =1;
	private int pen_pair = 0;
	private int secdiff=1;
	private int pen_secdiff = 0;
	
	/**
	 * Constructor that takes a string of arguments and gets the filename,
	 * and eval values.
	 * use -f _____, -m ____ ____, -pr ____, -pa ____ ____, -s ____ ____, (file, minfilled, preferences, pair, secdiff)
	 * where first blank is weight, and the second blank is penalty.
	 * where the blanks should be an integer value.  If not, return an error.  Can be in any order.
	 * @param args	Command line arguments that are to be parsed.
	 */
	public CommandParser(String[] args) throws SchedulerException {
	//if no arguments, just default weight of 1
		if (args.length ==2 || args.length ==14) {
			filename = findFilename(args, find(args,"-f",1));
			if (args.length == 14) {
				minfilled = getWeight(args, find(args,"-m",1));
				pen_minfilled = getWeight(args, find(args,"-m",2));
				pref = getWeight(args, find(args,"-pr",1));
				//pen_pref = getWeight(args, find(args,"-pr",2));
				pair = getWeight(args, find(args,"-pa",1));
				pen_pair = getWeight(args, find(args,"-pa",2));
				secdiff = getWeight(args, find(args,"-s",1));
				pen_secdiff = getWeight(args, find(args,"-s",2));
			}
		}
		else {
			System.out.println("Invalid command line argument.");
		}
	}
	/**
	 * Method that gets the filename from the command line.
	 * @param array		Array of strings from command line
	 * @param index		Index of the string that you are looking for
	 * @return 		String that is the filename.
	 */
	private String findFilename(String[] array, int index) throws SchedulerException{
		if (index <0) {
			throw new SchedulerException("Invalid index of file");
		}
		return array[index];
	}

	/**
	 * Method that returns the next index in the array of the String value argument.
	 * @param		String array of arguments
	 * @param array		String value that you are looking for in the string array
	 * @param indexAfter	Integer value of whether you want a weight (1) or a penalty value (2)
	 * @return value	Integer index that corresponds to where the value argument is in the 
	 * 			string array argument.
	 */
	private int find(String[] array, String value, int indexAfter) {
		int index = 0;
		for (String s: array) {
			//Check that index is not at the end of the array (otherwise no
			//argument is after it).
			if (s.equals(value) && index <array.length) {
				return index+indexAfter;
			}
			index++;
		}
		return -1;
	}
	/**
	 * Method that gets the weight of the Eval arguments from command line.
	 * @param array		Array of strings from the command line
	 * @param index		Index of the value that you are looking for
	 * @return		Integer weight from the string array
	 */
	private int getWeight(String[] array, int index) throws SchedulerException {
		//Fix this.  Just returns 1 if no argument is given
		if (index <0) {
			return 1;
		}
		try {
			return Integer.parseInt(array[index]);
		}
		catch (Exception e) {
			throw new SchedulerException("Could not get weight.");
		}
	}
	/**
	 * Gets the filename from CommandParser.
	 * @return 	String name of file from parsed arguments
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * Gets the minfilled value from CommandParser.
	 * @return	Integer minfilled from parsed arguments
	 */ 
	public int getMinfilled() {
		return minfilled;
	}
	/**
	 * Gets the pen_minfilled value from CommandParser.
	 * @return	Integer pen_minfilled from parsed arguments
	 */
	public int getPenMinfilled() {
		return pen_minfilled;
	}
	/**
	 * Gets the preferences value from CommandParser.
	 * @return	Integer preferences from parsed arguments
	 */ 
	public int getPref() {
		return pref;
	}
//	/**
//	 * Gets the penalty preferences value from CommandParser.
//	 * @return	Integer penalty preferences from parsed arguments
//	 */ 
//	public int getPenPref() {
//		return pen_pref;
//	}
	/**
	 * Gets the pair value from CommandParser.
	 * @return	Integer pair from parsed arguments
	 */ 
	public int getPair() {
		return pair;
	}
	/**
	 * Gets the pen_pair value from CommandParser.
	 * @return	Integer pen_pair from parsed arguments
	 */ 
	public int getPenPair() {
		return pen_pair;
	}
	/**
	 * Gets the section difference value from CommandParser.
	 * @return	Integer section difference from parsed arguments
	 */ 
	public int getSecdiff() {
	return secdiff;
	}
	/**
	 * Gets the penalty section difference value from CommandParser.
	 * @return	Integer penalty section difference from parsed arguments
	 */ 
	public int getPenSecdiff() {
		return pen_secdiff;
	}
}
