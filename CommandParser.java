
/*
 * Class CommandParser that gets Eval weights and filename.
 * 
 */
public class CommandParser {
	
	private String filename;
	private int minfilled =1;
	private int pref =1;
	private int pair =1;
	private int secdiff=1;
	
	/*
	 * use -f _____ -m ___, -pr ___, -pa ___, -s ___, (file, minfilled, preferences, pair, secdiff)
	 * where the blanks should be an integer value.  If not, return an error.  Can be in any order.
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
	/*
	 * Method that gets the filename from the command line.
	 */
	private String findFilename(String[] array, int index) throws SchedulerException{
		if (index <0) {
			throw new SchedulerException("Invalid index of file");
		}
		return array[index];
	}

	/*
	 * Method that returns the next index in the array of the String value argument.
	 */
	private int find(String[] array, String value) {
		int index = 0;
		for (String s: array) {
			if (s.equals(value) && index <array.length) {
				return index+1;
			}
			index++;
		}
		return -1;
	}
	/*
	 * Method that gets the weight of the Eval arguments from command line.
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
	/*
	 * Getter methods to get values.
	 */
	public String getFilename() {
		return filename;
	}
	public int getMinfilled() {
		return minfilled;
	}
	public int getPref() {
		return pref;
	}
	public int getPair() {
		return pair;
	}
	public int getSecdiff() {
	return secdiff;
	}
}
