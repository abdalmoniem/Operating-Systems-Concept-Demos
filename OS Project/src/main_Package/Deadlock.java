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

    public String detect() {
        String result = "";

        for (Process i : ready_queue) {
            if (!i.Need.Compare(available)) {
                System.out.println(i.PID + " may cause deadlock");
                result += i.PID + " may cause deadlock\n";
                deadlock = true;
            }
        }

        if (deadlock) {
            System.out.println("The system is in an unsafe state.");
            result += "The system is in an unsafe state.\n";
        } else {
            System.out.println("The system is in a safe state.");
            result = "The system is in a safe state.\n";
        }
        
        return result;
    }
}
