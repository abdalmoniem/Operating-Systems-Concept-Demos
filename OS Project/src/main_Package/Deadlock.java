package main_Package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public String avoid() {
        String result = "";
        LinkedList<Process> execution_order_queue = new LinkedList<>();
        System.out.println();
        System.out.println("Checking for safe states...");
        long start = System.currentTimeMillis();

        for (int index = 0; !ready_queue.isEmpty(); index = ready_queue.size() > 0 ? (index + 1) % (ready_queue.size()) : index) {
            long end = System.currentTimeMillis();
            if (end - start > 2000) {
                break;
            } else {
                Process i = ready_queue.get(index);
                if (i.Need.Compare(available)) {
                    available.Add(i.Need);
                    ready_queue.remove(index);
                    execution_order_queue.add(i);
                }
            }
        }

        System.out.println("Safe execution order:");
        result += "Safe execution order:\n";
        for (Process i : execution_order_queue) {
            System.out.println("PID: " + i.PID);
            result += "[P" + i.PID + "] -> ";
        }
        if(!execution_order_queue.isEmpty())
            result = result.substring(0, result.length() - 3) + "\n";
        else
            result += "None...\n";
        result += "\n";
        System.out.println();

        if (ready_queue.isEmpty()) {
            System.out.println("The system is in a safe state");
            result += "The system is in a safe state\n";
        } else {
            System.out.println("Ready queue:");
            result += "Processes remaining in Ready queue:\n";
            for (Process i : ready_queue) {
                System.out.println("PID: " + i.PID);
                result += "[P" + i.PID + "] -> ";
            }
            result = result.substring(0, result.length() - 3) + "\n";
            result += "\n";
            System.out.println();
            System.out.println("The system is in an unsafe state");
            result += "\nThe system is in an unsafe state\n";
        }
        return result;
    }
}
