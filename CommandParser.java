
/**
 * Class CommandParser that gets Eval weights and filename.
 * @author CPSC 433 Toshibe
 * 
 */
public class CommandParser {
	
	private String filename;
	/* Set initial default values. */
	private int minfilled =1;
	private int pref =1;
	private int pair =1;
	private int secdiff=1;
	
	/**
	 * Constructor that takes a string of arguments and gets the filename,
	 * and eval values.
	 * use -f _____ -m ___, -pr ___, -pa ___, -s ___, (file, minfilled, preferences, pair, secdiff)
	 * where the blanks should be an integer value.  If not, return an error.  Can be in any order.
	 * @param args	Command line arguments that are to be parsed.
	 */
	public CommandParser(String[] args) throws SchedulerException {
	//if no arguments, just default weight of 1
		if (args.length ==1 || args.length ==10) {
			filename = findFilename(args, find(args,"-f"));
			minfilled = getWeight(args, find(args,"-m"));
			pref = getWeight(args, find(args,"-pr"));
			pair = getWeight(args, find(args,"-pa"));
			secdiff = getWeight(args, find(args,"-s"));
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
	 * @return value	Integer index that corresponds to where the value argument is in the 
	 * 			string array argument.
	 */
	private int find(String[] array, String value) {
		int index = 0;
		for (String s: array) {
			//Check that index is not at the end of the array (otherwise no
			//argument is after it).
			if (s.equals(value) && index <array.length) {
				return index+1;
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
	 * Gets the preferences value from CommandParser.
	 * @return	Integer preferences from parsed arguments
	 */ 
	public int getPref() {
		return pref;
	}
	/**
	 * Gets the pair value from CommandParser.
	 * @return	Integer pair from parsed arguments
	 */ 
	public int getPair() {
		return pair;
	}
	/**
	 * Gets the section difference value from CommandParser.
	 * @return	Integer section difference from parsed arguments
	 */ 
	public int getSecdiff() {
	return secdiff;
	}
}
