/**
 * Class SchedulerException that handles the errors in the code.
 * @author CPSC 433 Toshibe.
 *
 */

public class SchedulerException extends Exception {

    /**
     * Constructor calls Exception super class with message
     */
    public SchedulerException() {
        super("Scheduler Exception");
    }

    /**
     * Constructor calls Exception super class with message
     * @param message The message of exception
     */
    public SchedulerException(String message) {
        super(message);
    }

}
