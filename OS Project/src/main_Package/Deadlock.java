package main_Package;

import java.util.LinkedList;

/**
 *
 * @author mn3m
 */
public class Deadlock {

    LinkedList<Process> ready_queue;
    Resource available;
    boolean deadlock = false;

    public Deadlock(LinkedList<Process> ready_queue, Resource available) {
        this.ready_queue = ready_queue;
        this.available = available;
    }

    public void detect() {
        ready_queue.forEach(i -> {
            if (!i.Need.Compare(available)) {
                System.out.println(i.PID + " will cause deadlock");
                deadlock = true;
            }
        });
        
        if(deadlock)
            System.out.println("The system is in an unsafe state.");
        else
            System.out.println("The system is in a safe state.");
    }
}
