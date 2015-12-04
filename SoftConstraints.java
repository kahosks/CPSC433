import java.util.ArrayList;
import java.util.Arrays;

public class SoftConstraints
{
	Slot[] slotArray; //Not sure if defined correctly; assumes array of Slots class
	//Preference[] prefArray; //Not sure if defined correctly; assumes array of Preference class
	//PairedCourseClass[] pairArray;//Not sure if defined correctly; assumes array of PairedCourseClass class
	ArrayList<Preference> prefArray;
	ArrayList<PairedCourseClass> pairArray;
	
	String[] index; //the index array
	
	/*
	* Constructor -Condensed-
	* May need to be changed depending how this will be instantiated
	*/
	public SoftConstraints(String[] index, Slot[] slotArray,
	 ArrayList<Preference> prefArray, ArrayList<PairedCourseClass> pairArray)
	{
		this.index = index;
		this.slotArray = slotArray;
		this.prefArray = prefArray;
		this.pairArray = pairArray;
	}
	
	/*
	*Seperate constructors, may not need anymore
	*Recieves the slot array
	*I have no idea how I'm to recieve this, someone check this out
	*public SoftConstraints(Slot[] array)
	*{
	*	slotArray = Arrays.copyOf(array, array.length);
	*}
	*
	*Recieves the prefences array
	*I have no idea how I'm to recieve this, someone check this out
	*public SoftConstraints(Preferences[] array)
	*{
	*	prefArray = Arrays.copyOf(array, array.size());
	*}
	*
	*Recieves the pairs array
	*I have no idea how I'm to recieve this, someone check this out
	*public SoftConstraints(Pairs[] array)
	*{
	*	pairArray = Arrays.copyOf(array, array.size());
	*}
	*/
	
	/*
	* Get minimum course/lab penalty
	* For now, input is the prob array
	*/
		public int getMinFilled(int[] time)
	{
		int penalty = 0;
		int cNum = 0;
		int lNum = 0;
		int courseMin = 0;
		int labMin = 0;
		int tim = 0;
		boolean commit = false;
		for (int y = 0; y < slotArray.length; y++)
		{
			if (slotArray[y] != null) {

				courseMin = slotArray[y].getCoursemin();
				labMin = slotArray[y].getLabmin();
				tim = slotArray[y].getTimeInt();
				for (int x = 2; x < time[0]; x++)
				{
					if (tim == time[x])
					{

						if(index[x].contains("TUT")|| index[x].contains("LAB"))
						{
							lNum++;
							commit = true;
						}
						else {				
							cNum++;
							commit = true;
						}
					}
				}
				if (commit == true)
				{
					if (cNum < courseMin)
					{
						penalty += (courseMin - cNum); //maybe penalty +=1.  I believe the penlty would be the difference between the slot's courseMin, and the number of courses actually in?
						cNum = 0;
					}
					if (lNum < labMin)
					{
						penalty += (labMin - lNum);  // maybe penalty += 1.    Same as above, but with labMin and number of Labs?
						lNum = 0;
					}
					commit = false;
				}
			}
		}
		return penalty;
	
	}
	
	/*
	* Get professor preference penalty
	* For now, input is the prob array,
	* list of pref(course/lab, time, pref value), assumes list of Preference class?
	*/
	public int getPref(int[] time)
	{
		
		int penalty = 0;
		for (int z = 0; z < prefArray.size(); z++)
		{
			for (int x = 2; x < time[0]; x++)
			{
				if(index[x].equals(prefArray.get(z).getIdentifier()))
				{
					if(time[x] != prefArray.get(z).getTimeInt())
					{
						penalty += prefArray.get(z).getPrefValue();
					}
				}
			}
		}
		return penalty;
	}
	
	/*
	*Get not paired penalty
	* For now, input is the prob array,
	* list of pair(a,b), assumes list of PairedCourseClass class?
	*/
	public int getPair(int[] time)
	{
		int penalty = 0;
		
		for (int z = 0; z < pairArray.size(); z++)
		{
			boolean afound = false, bfound = false;
			for (int x = 2; x < time[0]; x++)
			{
				int aTime = 0;
				int bTime = 0;
				if(index[x].equals(pairArray.get(z).getFirstPair().getIdentifier()))
				{
					aTime = time[x];
					afound = true;
				}
				if(index[x].equals(pairArray.get(z).getSecondPair().getIdentifier()))
				{
					bTime = time[x];
					bfound = true;
				}
				if((afound && bfound) && (aTime != bTime))
				{
					penalty++;
					x = time[0];
				}
			}
		}
		return penalty;
	}
	
	/*
	*Gets section same penalty
	* For now, input is simply the problem array
	*/
	public int getSecDiff(int[] time)
	{
		int penalty = 0;
		int secNum;
		int count = 0;
		String course;
		String[] checked = new String[time[0]];
		for (int x = 0; x < time[0]; x++)
		{
			if(x != 0 && x!= 1)
			{
				if(index[x].length() > 14)
				{
					if(!Arrays.asList(checked).contains(index[x].substring(0,7)))
					{
						checked[count] = index[x].substring(0,7);
						count++;
						course = index[x].substring(0,7);
						secNum = Integer.parseInt(index[x].substring(13,14));
						for(int y = x+1; y < time[0]; y++)
						{
							if (course.equals(index[y].substring(0,7)))
							{
								if(secNum != Integer.parseInt(index[x].substring(13,14)))
								{
									if(time[x] == time[y])
									{
										penalty ++;
									}
								}
							}
						}
					}
				}
			}
		}	
		return penalty;
	}
}
