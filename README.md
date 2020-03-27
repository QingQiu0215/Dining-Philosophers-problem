# Dining-Philosophers-problem
A classical problem of synchronization – the Dining Philosophers problem.

1. The Philosopher Class
Implemented the Philosopher class. Specifically, eat(), think(), talk(), and run() methods.

2. The Monitor
Implemented the Monitor class for the problem.  both deadlock- and starvation-free used Java’s synchronization primitives such as wait()/notify()/notifyAll() or uses Java utility lock and condition variables; 

a. A philosopher is allowed to pick up the chopsticks if they are both available. It has to be atomic
so that no deadlock is possible.

b. Starvation handled.

c. If a given philosopher has decided to make a statement, he/she can do so only if no one else is
talking at the moment. The philosopher wishing to make the statement first makes a request to
talk; and has to wait if someone else is talking. When talking is finished then others are notifies
by endTalk.

