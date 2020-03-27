

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor  
{
	int numberOfChopsticks;


	enum states {THINKING, EATING, HUNGRY, WAITING, TALKING}

	/**
	 * An array of chopsticks
	 * If the value is 1 the chopstick is in use.
	 * If the value is 0 the chopstick is available.
	 * When initialized, all chopsticks are available (value is 0)
	 */
	int[] chopstick;


	/**
	 * Array containing the states of all the philosophers
	 * 
	 */
	states[] philosopherState;
	/**
	 * Record if the philosopher eated or not.
	 * Ensures that people don't starve by not getting a turn 
	 */
	int [] philosopherEated;
	/**
	 * if all philosophers have eaten, the count==4(number of philosophers)
	 */
	int count=0; 
	/**
	 * true if a philosopher is currently talking, false otherwise
	 */
	boolean isTalking;


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		//Number of chopsticks is equal to the number of philosophers
				numberOfChopsticks = piNumberOfPhilosophers;

				//Initialize chopstick array
				chopstick = new int[numberOfChopsticks];

				//philosopher states array
				philosopherState = new states[piNumberOfPhilosophers];
				philosopherEated=new int[piNumberOfPhilosophers];
				//initialize all the states to thinking
				for(states state : philosopherState){
					state = states.THINKING;
				}
				//initially, all philosophers have not eaten.
				for(int i=0;i<philosopherEated.length;i++)
				{
					philosopherEated[i]=-1;
				}				
				//no one is talking
				isTalking = false;


	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		/*
		 * Philo X can pick up chopsticks[X-1] (to the left) and chopsticks[X] (to the right)
		 */
		int idx = piTID - 1;	
		while(true){
			if(count!=philosopherEated.length)//if there are any philosophers have not eaten, they will eat firstly.
			{
				//testing if chopsticks are used and if the philosopher has eaten or not
				if((chopstick[idx] == 0 && chopstick[(idx+1) % numberOfChopsticks] == 0&&philosopherEated[idx]==-1))
				{
					//EAT WITH CHOPSTICKS

					//Changes the availibility of the chopsticks
					chopstick[idx] = 1;
					chopstick[(idx+1) % numberOfChopsticks] = 1;

					//System.out.println("Philosopher "+ piTID + " picks up chopsticks " + idx +", " + ((idx+1)% numberOfChopsticks));
					philosopherState[idx] = states.EATING;

					philosopherEated[idx] = 1;
					count++;
					break;					
				} 
				else 
				{
					//System.out.println("Philosopher " + piTID + " is waiting for the chopsticks " + idx +", " + ((idx+1)% numberOfChopsticks) );
					philosopherState[idx] = states.HUNGRY;
					try 
					{
						wait();
					} catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else//if all philosophers have eaten, they are not starved. They can eat without starvation.
			{
				//testing if chopsticks are used
				if((chopstick[idx] == 0 && chopstick[(idx+1) % numberOfChopsticks] == 0))
				{
					//EAT WITH CHOPSTICKS

					//Changes the availibility of the chopsticks
					chopstick[idx] = 1;
					chopstick[(idx+1) % numberOfChopsticks] = 1;

					//System.out.println("Philosopher "+ piTID + " picks up chopsticks " + idx +", " + ((idx+1)% numberOfChopsticks));
					philosopherState[idx] = states.EATING;

					philosopherEated[idx] = 1;
					
					break;					
				} 
				else 
				{
					//System.out.println("Philosopher " + piTID + " is waiting for the chopsticks " + idx +", " + ((idx+1)% numberOfChopsticks) );
					philosopherState[idx] = states.HUNGRY;
					try 
					{
						wait();
					} catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int idx = piTID - 1;
		if(chopstick[idx] == 1 && chopstick[(idx+1) % numberOfChopsticks] == 1){

			//PUT DOWN CHOPSTICKS
			chopstick[idx] = 0;
			chopstick[(idx+1) % numberOfChopsticks] = 0;
			
			//System.out.println("Philosopher "+ piTID + " put down chopsticks " + idx +", " + ((idx+1)% numberOfChopsticks));
			
			philosopherState[idx] = states.THINKING;
			notifyAll();

		} else {
			//Something went wrong if this is called. edit: commented out because it wont be called. 
			System.err.println("Chopsticks are already put down " + idx + ", " + ((idx+1)% numberOfChopsticks));
		}


	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		while(true){
			if(!isTalking && philosopherState[(piTID-1)] != states.EATING){
				philosopherState[(piTID-1)] = states.TALKING;
				isTalking = true;
				break;
			} else {
				philosopherState[(piTID-1)] = states.WAITING;
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		isTalking = false;
		philosopherState[(piTID-1)] = states.THINKING;
		notifyAll();

	}
}

// EOF
