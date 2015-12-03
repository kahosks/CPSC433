import java.util.*;
public abstract class SoftConstraints
{
	int courseMin;
	int labMin;
	String[] course;
	String[] index;
	String[] a, b; //This one is ambiguous, how will the pair(a,b) be passed?
	int[] slots;
	
	
	/*
	* Constructor
	*/
	public SoftConstraints(String[] index)
	{
		this.index = Arrays.copyOf(index, index.size());
	}
	
	/*Get minimum course/lab penalty
	* For now, input is the prob array,
	* the time slot to be checked, courseMin and labMin requirements of that time slot
	*/
	public int getMinFilled(int[] time, int slot, int courseMin, int labMin)
	{
		int penalty = 0;
		int cNum = 0;
		int lNum = 0;
		for (int x = 0; x < time[0]; x++)
		{
			if(x != 0 && x!= 1)
			{
				if(time[x] == slot)
				{
					if(index[x].substring(9,11).equals("LEC"))
					{
						cNum++;
					}
					if(index[x].substring(9,11).equals("TUT") || index[x].substring(9,11).equals("LAB"))
					{
						lNum++;
					}
					if (index[x].length > 14)
					{
						if(index[x].substring(16,18).equals("TUT") || index[x].substring(16,18).equals("LAB"))
						{
							lNum++;
						}
					}
				}
			}
		}
		if (cNum < courseMin)
		{
			penalty += (courseMin - cNum);
		}
		if (lNum < labMin)
		{
			penalty += (labMin - lNum);
		}
		
		return penalty;
	}
	
	/*Get professor preference penalty
	* For now, input is the prob array,
	* as well as the array of the course half of the pair, and array of the time half of the pair.
	* Indexes are assumed to be synchronous.
	* Will most likely need to be changed depending on the format proffessor preferences is given
	*/
	public int getPref(int[] time, String[] course, int[] slots)
	{
		int penalty = 0;
		for (int x = 0; x < time[0]; x++)
		{
			if(x != 0 && x!= 1)
			{
				for(int y = 0; y < slot.size(); y++);
				{
					if(index[x].equals(course[y]))
					{
						if(time[x] != slots[y])
						{
							penalty++;
						}
					}
				}
			}
		}	
		return penalty;
	}
	
	/*Get not paired penalty
	* For now, input is the prob array,
	* as well as the names of the two courses/labs as they may appear in the index array
	* Depending on format input, this would need to change
	*/
	public int getPair(int[] time , String a, String b)
	{
		int penalty = 0;
		boolean afound, bfound = false;
		
		for (int x = 0; x < time[0]; x++)
		{
			if(x != 0 && x!= 1)
			{
				if(index[x].substring(0,7).equals(a.substring(0,7)))
				{
					int aIndex = x;
					afound = true;
				}
				if(index[x].substring(0,7).equals(b.substring(0,7)))
				{
					int bIndex = x;
					bfound = true;
				}
				if(afound && bfound)
				{
					for (int y = x; y < time[0]; y++)
					{
						if(index[y].substring(0,7).equals(a.substring(0,7)))
						{
							if(time[y] != time[bIndex])
							{
								penalty++;
							}	
						}
						if(index[x].substring(0,7).equals(b.substring(0,7)))
						{
							if(time[y] != time[aIndex])
							{
								penalty++;
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
						secNum = Integer.parseint(index[x].substring(13,14));
						for(int y = x+1; y < time[0]; y++)
						{
							if (course.equals(index[y].substring(0,7))
							{
								if(secNum != Integer.parseint(index[x].substring(13,14)))
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
