package main_Package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Deadlock {

    LinkedList<Process> ready_queue;
    Resource available;
    boolean deadlock = false;
/**
 * 
 * @param ready_queue contains the process which exists in the ready queue
 * @param available contains the available resources exists in the operating system
 */
    public Deadlock(LinkedList<Process> ready_queue, Resource available) {
        this.ready_queue = ready_queue;
        this.available = available;
    }
/**
 * 
 * @return result which contains the result of the detection (if the system is in a safe state or not ".
 */
    public String detect() {
        String result = "";

        for (Process i : ready_queue) {
            if (!i.Need.Compare(available)) 
            {
                System.out.println(i.PID + " may cause deadlock");// if the needed resources of process i bigger than or equal the availbale one , deadlock may occurs
                result += i.PID + " may cause deadlock\n";  // print "process i may cause a deadlock".
                deadlock = true; //let the bolean variable deadlock = true
            }
        }

        if (deadlock) {
            System.out.println("The system is in an unsafe state.");// if the deadlock= true , prints " the system is in unsafe state".
            result += "The system is in an unsafe state.\n"; //saving the resault of the detection in this variable
        } else {
            System.out.println("The system is in a safe state.");// if the neaded resource of i smaller than tha availbale , prints "tha system is in safe state"
            result = "The system is in a safe state.\n";///saving the resault of the detection in this variable
        }

        return result; 
    }
/**
 * 
 * @return the result of the avoiding algorithm.
 */
    public String avoid() {
        String result = "";
        LinkedList<Process> execution_order_queue = new LinkedList<>();
        System.out.println();
        System.out.println("Checking for safe states...");
        long start = System.currentTimeMillis();//print the start time
        for (int index = 0; !ready_queue.isEmpty(); index = ready_queue.size() > 0 ? (index + 1) % (ready_queue.size()) : index) {
            long end = System.currentTimeMillis();//print the endtime
            if (end - start > 2000) {
                break;
            } else {
                Process i = ready_queue.get(index);// get the process from the rady queu
                if (i.Need.Compare(available)) {  //if process's i need smaller than the available 
                    available.Add(i.Need); // then we allocate the need of the process i from the availabe , then the process wil finshes it's task then after that it will release it's needs , and it will be added to the available. 
                    ready_queue.remove(index);// remove the process from the ready queue
                    execution_order_queue.add(i);//put the process in the excution order queue (this queue contains all the excutable processes).
                }
            }
        }
        result += "Safe execution order:\n"; 
        System.out.println("Safe execution order:");//print the result  which includes the safe excution order
        for (Process i : execution_order_queue) {
            System.out.println("PID: " + i.PID);
            result += "PID: " + i.PID + "\n"; //add the process pid to the result
        }
        System.out.println();
        if (ready_queue.isEmpty()) {  // after we try to allocate all the processes with it's need (the loop has been finished), if the ready queue is empty so the system is in safe state.
            System.out.println("The system is in a safe state");
        } else { //if there is still resources available in the readt queue
            System.out.println("Ready queue:");
            for (Process i : ready_queue) { /// so, the system is in unsafe state , prints the remaining processes
                System.out.println("PID: " + i.PID);
                result += "PID: " + i.PID + "\n";
            }
            result += "\nThe system is in an unsafe state\n";// print that the result is in unsafe state
            System.out.println();
            System.out.println("The system is in an unsafe state");
        }
        return result;
    }
}
