import java.util.Arrays;

public abstract class SoftConstraints
{
	//The below are all needed inputs, though how do we get them?
	Slot[] slotArray;
	Preferences[] prefArray;
	Pairs[] pairArray;
	String[] course;
	String[] index;
	int[] slots;
	
	/*Constructors
	* May need to be changed depending how this will be instantiated
	*/
	public SoftConstraints(String[] index)
	{
		this.index = Arrays.copyOf(index, index.size());
	}
	
	//Recieves the slot array
	//I have no idea how I'm to recieve this, someone check this out
	public SoftConstraints(Slot[] array)
	{
		slotArray = Arrays.copyOf(array, array.size());
	}
	
	//Recieves the prefences array
	//I have no idea how I'm to recieve this, someone check this out
	public SoftConstraints(Preferences[] array)
	{
		prefArray = Arrays.copyOf(array, array.size());
	}
	
	//Recieves the pairs array
	//I have no idea how I'm to recieve this, someone check this out
	public SoftConstraints(Pairs[] array)
	{
		pairArray = Arrays.copyOf(array, array.size());
	}
	
	/*Get minimum course/lab penalty
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
		for (int y = 0; y < slotArray.size(); y++)
		{
			courseMin = slotArray(y).getCourseMin();
			labMin = slotArray(y).getLabMin();
			tim = slotArray(y).getTimeInt();
			for (int x = 2; x < time[0]; x++)
			{
				if (tim == time[x])
				{
					if(index[x].substring(9,11).equals("LEC"))
					{
						cNum++;
						commit = true;
					}
					if(index[x].substring(9,11).equals("TUT") || index[x].substring(9,11).equals("LAB"))
					{
						lNum++;
						commit = true;
					}
					if (index[x].length > 14)
					{
						if(index[x].substring(16,18).equals("TUT") || index[x].substring(16,18).equals("LAB"))
						{
							lNum++;
							commit = true;
						}
					}
				}
			}
			if (commit == true)
			{
				if (cNum < courseMin)
				{
					penalty += (courseMin - cNum); ///maybe penalty +=1
					cNum = 0;
				}
				if (lNum < labMin)
				{
					penalty += (labMin - lNum);  // maybe penalty += 1
					lNum = 0;
				}
				commit = false;
			}
		}
		return penalty;
	}
	
	/*Get professor preference penalty
	* For now, input is the prob array,
	* list of pref(course/lab, time), assumes (string, time)?
	* Will most likely need to be changed depending on the format proffessor preferences is given
	*/
	public int getPref(int[] time)
	{
		
		int penalty = 0;
		for (int z = 0; z < prefArray.size(); z++)
		{
			for (int x = 2; x < time[0]; x++)
			{
				if(index[x].equals(prefArray[z][0]))
				{
					if(time[x] != prefArray[z][1])
					{
						penalty++;// TODO make +=prefArray[z][2]
					}
				}
			}
		}
		return penalty;
	}
	
	/*Get not paired penalty
	* For now, input is the prob array,
	* list of pair(a,b), assumes (string, string)
	* Depending on format input, this would need to change
	*/
	public int getPair(int[] time)
	{
		int penalty = 0;
		boolean afound, bfound = false;
		
		for (int z = 0; z < pairArray(size); z++)
		{
			for (int x = 2; x < time[0]; x++)
			{
				if(index[x].substring(0,7).equals(pairArray[z][0].substring(0,7)))
				{
					int aIndex = x;
					afound = true;
				}
				if(index[x].substring(0,7).equals(pairArray[z][1].substring(0,7)))
				{
					int bIndex = x;
					bfound = true;
				}
				if(afound && bfound)
				{
					for (int y = x; y < time[0]; y++)
					{
						if(index[y].substring(0,7).equals(pairArray[z][0].substring(0,7)))
						{
							if(time[y] != time[bIndex])
							{
								penalty++;
								afound = false;
							}	
						}
						if(index[x].substring(0,7).equals(pairArray[z][1].substring(0,7)))
						{
							if(time[y] != time[aIndex])
							{
								penalty++;
								bfound = false;
							}	
						}
					}
					x = time[0];
				}
			}
		}
		return penalty;
	}
	
	/*Gets section same penalty
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
				if(index[x].length > 14)
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
