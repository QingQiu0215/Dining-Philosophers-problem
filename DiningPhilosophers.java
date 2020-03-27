import java.util.Scanner;

/**
 * Class DiningPhilosophers 
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca   
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			int iPhilosophers = getNumberOfPhiloWanted();

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
	/**
	 * Asks the user to specify the number of philosophers.
	 * Returns the number specified OR uses the default number if the user doesn't specify 
	 * @return
	 */
	public static int getNumberOfPhiloWanted(){
		try{
		Scanner input = new Scanner(System.in);
		
		System.out.println("Set the number of philosophers: ");
		String line = input.nextLine();
		input.close();
		
		int num = Integer.parseInt(line);	
		
		if(num <= 0){	
		System.out.println(num + " is not a positive decimal integer, using default number of philosophers");
			return DEFAULT_NUMBER_OF_PHILOSOPHERS;
		} else {
			return num;
		}
		
		}
		catch(NumberFormatException e){
			System.out.println("Using the default number of philosophers (4).");
			return DEFAULT_NUMBER_OF_PHILOSOPHERS;
		}
	}//getNumberOfPhiloWanted
}

// EOF
