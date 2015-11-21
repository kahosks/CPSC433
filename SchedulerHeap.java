import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Class SchedulerHeap that contains all the potential prob solutions.
 * This is just a really really rough idea, we don't need to use it, so definitely change.
 * @author CPSC 433 Toshibe
 *
 */
public class SchedulerHeap {
	PriorityQueue<Prob> pq;
	ScheduleComparator sc;
	
	/**
	 * Constructor that takes a Vector of Probs as an argument
	 * @param probs	Vector of Probs
	 */
	public SchedulerHeap(Vector<Prob> probs) {
		//add all the prs to the heap
		pq = new PriorityQueue<Prob>(probs.size(), new ScheduleComparator()); 
		for (Object o:probs.toArray()) {
			Prob pr = (Prob) o;
			pq.add(pr);
		}
	}
	/**
	 * Gets the next Prob element from the queue, i.e., the top element.
	 * @return	Prob element.
	 */
	public Prob getNextProb() {
		//Gets the top element.
		Prob pr = pq.element();
		
		return pr;
		
	}
	/**
	 * Adds a Prob to the heap.
	 * Note: This is just the add() method in heap, so probably don't need 
	 * this function.
	 * @param pr	Prob to be added to heap.
	 */
	public void addToHeap(Prob pr) {
		/*
		 * TODO: Need to check that pr is a valid solution (this is where HardConstraints comes in.
		 * 		if pr is valid then add to queue, otherwise throw it away
		 */
		pq.add(pr);
	}
	/**
	 * Adds a vector of Probs to the heap.
	 * @param pr	Vector of probs to be added.
	 */
	public void addVectorToHeap(Vector<Prob> pr) {
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
	/**
	 * Class ScheduleComparator which is used with the priority queue to sort objects.
	 * @author CPSC 433 Toshibe
	 *
	 */
	public class ScheduleComparator implements Comparator<Object> {
		//Returns 1 if p1's eval value is bigger than p2's.  Returns 0 if equal.  Returns -1 otherwise.
		
		/**
		 * Compares inverse eval-values of Probs.  Returns 1 is p1 > p2, 0 if equal, and -1 if p1 <p2.
		 */
		public int compare(Object p1, Object p2) {
			if (((Prob) p1).getEvalInverse() > ((Prob)p2).getEvalInverse()) {
				return 1;
			}
			else if (((Prob) p1).getEvalInverse() == ((Prob) p2).getEvalInverse()) {
				return 0;
			}
			else {
				return -1;
			}
		}		
	}
	
	/**
	 * Main function used to test what elements are in prob, how the queue sorts the elements,
	 * and which Prob is returned off the top of the queue.
	 * 
	 * @param args	Command line arguments.
	 */
	public static void main (String[] args) {
		//Create elements to add to queue.
		Prob pr1 = new Prob(new Slot[5], new Slot[5], new Slot[5],new Slot[5]);
		pr1.addEvalValue(10);
		System.out.println("Added eval: " + pr1.getEvalValue() + " Inverse: " +pr1.getEvalInverse());
		
		Prob pr2 = new Prob(null, null, null,null);
		pr2.addEvalValue(5);
		System.out.println("Added eval: " + pr2.getEvalValue() + " Inverse: " +pr2.getEvalInverse());

		Prob pr3 = new Prob(null, null, null,null);
		pr3.addEvalValue(7);
		System.out.println("Added eval: " + pr3.getEvalValue() + " Inverse: " +pr3.getEvalInverse());

		Prob pr4 = new Prob(null, null, null,null);
		pr4.addEvalValue(-4);
		System.out.println("Added eval: " + pr4.getEvalValue() + " Inverse: " +pr4.getEvalInverse());
		System.out.println();

		//Create a vector to pass to heap.
		Vector<Prob> probs = new Vector<Prob>();
		probs.add(pr1);
		probs.add(pr2);
		probs.add(pr3);
		probs.add(pr4);
		
		SchedulerHeap sc = new SchedulerHeap(probs);
		
		//Print the queue to see contents.
		sc.printQueue();
		//Check which prob is at top of queue.
		Prob ans = sc.getNextProb();
		System.out.println("\nEval: " + ans.getEvalValue() + " Inverse: " + ans.getEvalInverse());
		
		Prob pr5 = new Prob(null, null,null, null);
		pr5.addEvalValue(10);
		System.out.println("Added eval: " + pr5.getEvalValue() + " Inverse: " +pr5.getEvalInverse());
		Prob pr6 = new Prob(null, null,null, null);
		pr6.addEvalValue(8);
		System.out.println("Added eval: " + pr6.getEvalValue() + " Inverse: " +pr6.getEvalInverse());
		Prob pr7 = new Prob(null, null, null,null);
		pr7.addEvalValue(2);
		System.out.println("Added eval: " + pr7.getEvalValue() + " Inverse: " +pr7.getEvalInverse());
		Vector<Prob> p = new Vector<Prob>();
		p.add(pr6);
		p.add(pr7);
		sc.addToHeap(pr5);
		sc.addVectorToHeap(p);
		sc.printQueue();
		ans = sc.getNextProb();
		System.out.println("\nEval: " + ans.getEvalValue() + " Inverse: " + ans.getEvalInverse());
		
	}
}
