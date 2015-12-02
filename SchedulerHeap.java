import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 * Class SchedulerHeap that contains all the potential prob solutions.
 * This is just a really really rough idea, we don't need to use it, so definitely change.
 * @author CPSC 433 Toshibe
 *
 */
public class SchedulerHeap {
	private PriorityQueue<int[]> pq;
	private ScheduleComparator sc;
	private ArrayList<Class> labsAndCourses;
	private int[] bestSolution;
	private int size = 100;
	private int PROBLEM_LENGTH;
	private int BAD_PROBLEM_SCORE = Integer.MAX_VALUE;
	
	/* Constructor with no arguments. */
	public SchedulerHeap() {
		//Default initial value.  Very likely want greater size.
		pq = new PriorityQueue<int[]>(size, new ScheduleComparator());
	}
	/**
	 * Constructor that takes an int array of the initial problem as an argument
	 * @param prob	int array of the initial problem
	 */
	public SchedulerHeap(int[] prob, ArrayList<Class> labsAndCourses) {

		pq = new PriorityQueue<int[]>(prob.length, new ScheduleComparator());
		pq.add(prob);
		this.labsAndCourses = labsAndCourses;
		PROBLEM_LENGTH = prob.length;
		bestSolution = new int[PROBLEM_LENGTH];
		bestSolution[1] = BAD_PROBLEM_SCORE;
	}
	/*
	
	Makes the schedule, contains the scheduler heap main loop
	
	We might want to return an instance of Prob with this method OR have some way of getting the best
	solution
	*/
	public void makeSchedule() throws SchedulerException{
	
	if (pq.size() == 0) {
		throw new SchedulerException("No starting problem."); //Can't make the scheduler with nothing in the queue
	}
	
	SearchModel sm = new SearchModel(labsAndCourses);
	int[][] newProblems;
	
	while (!pq.isEmpty()) {
		newProblems = sm.div(pq.poll());
		for(int[] pr: newProblems) {
			/* if the current problem has a depth greater than the length
			 * of the array it is done, if it also has an eval greater than
			 * the best it is the new best
			 */
			if ((PROBLEM_LENGTH < pr[0]) && (bestSolution[1] < pr[1]))
				bestSolution = pr;			
			pq.add(pr);
		}
	}
	
	
	
	//We might want a checker here that basically goes through potentialSolutions and
	//removes any solutions that don't have all the classes in the schedule
	
	
//	boolean status = true;
//	for (int i = 0; i < potentialSolutions.length; i++) {
//	
//		int[] pr = potentialSolutions[i];
//		
//		//May cause issues compiling if it does then just assume that 
//		//Every one passes hard constraints till we can actually check hard constraints
//		
//		if (checkConstraints(pr)){
//			pq.add(pr);
//		} 
//					
//		
//	}
	//The loop below is probably not the right loop..
	//However it may have some uses somewhere
	/*
	while(status) {
	
	
		
		
		//I may have messed up the ordering of this
		Prob p = pq.peek();
		
		// if p is done and passes hard constraints
		//DIV(p) might make sense here
		
		
		/*
		START OF ITERATION THROUGH the DIV(p)
		
		
		
		
		
		//Pass p into the hard constraints here and if it fails a hard constraint delete p
		//otherwise keep p in the queue
		int softConstraintEval;
		
		if (HardConstraints.checkConstraints(p)) {
			
			//TODO add a check for the soft constraints
			
			//set the softConstraintEval to the prob soft constraint eval value
			
		} else {
			pq.poll(); //Basically removes the top element of the queue
		}
		
		if (pq.size() == 0) {
			status == false;	//Basically if pq is empty we can break out the loop
		}
		
		
		
		//Have some kind of check here that if the prob instance is better than our current best 
		//change the best to the current instance
		//Otherwise it is safe to assume that we have either a valid solution here that is not 
		//better than the best or we have the new best solution
		
		/*
		
			if (softConstraintEval > evalSoftConstaints(bestSolution) && solution passes hard constaints) {
				bestSolution = p;
			} else {
				pq.poll(); //Solution is not better than the optimal and is valid therefore we can remove it
			}
		
		
		
		//here we shall generate new solutions on prob if it is not a complete solution
		
			
	}
	*/
	
	}
	/**
	 * Gets the next Prob element from the queue, i.e., the top element.
	 * @return	Prob element.
	 */
	public int[] getNextProb() {
		//Gets the top element.
		int[] pr = pq.element();
		
		return pr;
		
	}
	/**
	 * Adds a Prob to the heap.
	 * Note: This is just the add() method in heap, so probably don't need 
	 * this function.
	 * @param pr	Prob to be added to heap.
	 */
	public void addToHeap(int[] pr) {
		/*
		 * TODO: Need to check that pr is a valid solution (this is where HardConstraints comes in.
		 * 		if pr is valid then add to queue, otherwise throw it away
		 */
		pq.add(pr);
	}
	/**
	 * Adds a ArrayList of Probs to the heap.
	 * @param pr	ArrayList of probs to be added.
	 */
	public void addArrayListToHeap(ArrayList<int[]> pr) {
		pq.addAll(pr);
	}
	/**
	 * Deleted a Prob from heap. 
	 * Note: We probably don't need this, because essentially it is just
	 * the remove() method for heap.
	 * @param pr	Prob to be deleted.
	 */
	public void deleteFromHeap(Prob pr) {
		pq.remove(pr);
	}
	/**
	 * Prints out all elements in the queue, plus their eval values and eval inverse values.  
	 * Used for testing, can delete.
	 */
	public void printQueue() {
		for (Object o:pq.toArray()) {
			Prob p = (Prob) o;
			System.out.println("P eval: " + p.getEvalValue() + " P inverse: " + p.getEvalInverse());
		}
	}
//	/**
//	 * Class ScheduleComparator which is used with the priority queue to sort objects.
//	 * @author CPSC 433 Toshibe
//	 *
//	 */
//	public class ScheduleComparator implements Comparator<Object> {
//		//Returns 1 if p1's eval value is bigger than p2's.  Returns 0 if equal.  Returns -1 otherwise.
//		
//		/**
//		 * Compares inverse eval-values of Probs.  Returns 1 is p1 > p2, 0 if equal, and -1 if p1 <p2.
//		 */
//		public int compare(Object p1, Object p2) {
//			if (((Prob) p1).getEvalInverse() > ((Prob)p2).getEvalInverse()) {
//				return 1;
//			}
//			else if (((Prob) p1).getEvalInverse() == ((Prob) p2).getEvalInverse()) {
//				return 0;
//			}
//			else {
//				return -1;
//			}
//		}		
//	}
	
	/**
	 * Class ScheduleComparator which is used with the priority queue to sort objects.
	 *
	 */
	public class ScheduleComparator implements Comparator<int[]> {
		/*	Returns 1 if p1's depth value is greater than p2's.  
		 * 	Returns -1 if p1's depth value is less than p2's.
		 * 	
		 * 	If p1 and p2 have equal Depth values then it will compare their eval values
		 * 	Returns 1 if p1's eval is greater than p2's
		 * 	Returns -1 if p1's eval is less than p2's
		 * 	Returns 0 if p1 and p2 have the same eval
		*/
		public int compare(int[] p1, int[] p2) {
			if (p1[0] > p2[0]) {
				return 1;
			}
			else if (p1[0] < p2[0]) {
				return -1;
			}
			else if ((p1[0] == p2[0]) && ((p1[1] > p2[1]))) {
				return 1;
			}
			else if ((p1[0] == p2[0]) && ((p1[1] < p2[1]))) {
				return -1;
			}
			else {
				return 0;
			}
		}		
	}
	
//	/**
//	 * Main function used to test what elements are in prob, how the queue sorts the elements,
//	 * and which Prob is returned off the top of the queue.
//	 * 
//	 * @param args	Command line arguments.
//	 */
//	public static void main (String[] args) {
//		System.out.println("Schedular Heap");
//		//Create elements to add to queue.
//		Prob pr1 = new Prob(new Slot[5], new Slot[5], new Slot[5],new Slot[5]);
//		pr1.addEvalValue(10);
//		System.out.println("Added eval: " + pr1.getEvalValue() + " Inverse: " +pr1.getEvalInverse());
//		
//		Prob pr2 = new Prob(null, null, null,null);
//		pr2.addEvalValue(5);
//		System.out.println("Added eval: " + pr2.getEvalValue() + " Inverse: " +pr2.getEvalInverse());
//
//		Prob pr3 = new Prob(null, null, null,null);
//		pr3.addEvalValue(7);
//		System.out.println("Added eval: " + pr3.getEvalValue() + " Inverse: " +pr3.getEvalInverse());
//
//		Prob pr4 = new Prob(null, null, null,null);
//		pr4.addEvalValue(-4);
//		System.out.println("Added eval: " + pr4.getEvalValue() + " Inverse: " +pr4.getEvalInverse());
//		System.out.println();
//
//		//Create a ArrayList to pass to heap.
//		ArrayList<Prob> probs = new ArrayList<Prob>();
//		probs.add(pr1);
//		probs.add(pr2);
//		probs.add(pr3);
//		probs.add(pr4);
//		
//		SchedulerHeap sc = new SchedulerHeap(probs);
//		
//		//Print the queue to see contents.
//		sc.printQueue();
//		//Check which prob is at top of queue.
//		Prob ans = sc.getNextProb();
//		System.out.println("\nEval: " + ans.getEvalValue() + " Inverse: " + ans.getEvalInverse());
//		
//		Prob pr5 = new Prob(null, null,null, null);
//		pr5.addEvalValue(10);
//		System.out.println("Added eval: " + pr5.getEvalValue() + " Inverse: " +pr5.getEvalInverse());
//		Prob pr6 = new Prob(null, null,null, null);
//		pr6.addEvalValue(8);
//		System.out.println("Added eval: " + pr6.getEvalValue() + " Inverse: " +pr6.getEvalInverse());
//		Prob pr7 = new Prob(null, null, null,null);
//		pr7.addEvalValue(2);
//		System.out.println("Added eval: " + pr7.getEvalValue() + " Inverse: " +pr7.getEvalInverse());
//		ArrayList<Prob> p = new ArrayList<Prob>();
//		p.add(pr6);
//		p.add(pr7);
//		sc.addToHeap(pr5);
//		sc.addArrayListToHeap(p);
//		sc.printQueue();
//		ans = sc.getNextProb();
//		System.out.println("\nEval: " + ans.getEvalValue() + " Inverse: " + ans.getEvalInverse());
//		
//	}
}
