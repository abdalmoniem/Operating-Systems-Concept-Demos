package main_Package;

import java.util.*;

/**
 *
 * @author mn3m
 */
public class Scheduler {

    public static int FIFO = 0;
    public static int SJF = 1;
    public static int SRJF = 2;
    public static int RR = 3;
    private int time_quantum = 30;     //Defining the time quantum of the system

    private LinkedList<Process> ready_queue;

    public Scheduler(LinkedList<Process> ready_queue) {
        this.ready_queue = ready_queue;
    }

    public LinkedList<Process> sort(int sort_method) throws Exception {
        switch (sort_method) {
            case 0:
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time));
                return this.ready_queue;
            case 1:
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Burst_Time, p2.Burst_Time));
                return this.ready_queue;
            case 2:
                
                return this.ready_queue;
            case 3:
                LinkedList<Process> out = new LinkedList<Process>();
                LinkedList<Process> res = new LinkedList<Process>();
                Process temp = new Process();
                Process temp1 = new Process();
                while (ready_queue.isEmpty() == false) {
                    if (ready_queue.getFirst().Burst_Time > time_quantum) {
                        temp = ready_queue.getFirst();
                        temp.Burst_Time = ready_queue.getFirst().Burst_Time - time_quantum;
                        ready_queue.addLast(temp);
                        out.addLast(temp);

                        for (Process i : out) //Check if the process needs more than one time quantum to execute 
                        {
                            res.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));
                        }
                        out.removeFirst();
                        ready_queue.removeFirst();
                    } else //Check if the process needs less than one time quantum to execute
                    {
                        temp1 = ready_queue.getFirst();
                        temp1.Burst_Time = 0;
                        out.addLast(temp1);
                        for (Process i : out) {
                            res.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));

                        }
                        out.removeFirst();
                        ready_queue.removeFirst();
                    }

                }
                return res;
            default:
                throw new Exception("Unkown Sorting Method...");
        }
    }
}
