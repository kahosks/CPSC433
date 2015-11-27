/**
 * Class Driver that instantiates the Scheduler class which runs the program.
 */
public class Driver{

	public static void main(String[] args) throws SchedulerException{
		Scheduler sch = new Scheduler();
		sch.start(args);
	}
}
