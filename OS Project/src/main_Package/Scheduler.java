package main_Package;

import java.util.*;

/**
 *
 * @author mn3m
 */
public class Scheduler {

    public static final int FIFO = 0;
    public static final int SJF = 1;
    public static final int SRJF = 2;
    public static final int RR = 3;
    
    private final int time_quantum = 30;
    private final LinkedList<Process> ready_queue;

    public Scheduler(LinkedList<Process> ready_queue) {
        this.ready_queue = new LinkedList<>();
        this.ready_queue.addAll(ready_queue);
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
                //Sort using SRJF
                return this.ready_queue;
            case 3:
                LinkedList<Process> out = new LinkedList<>();
                LinkedList<Process> execution_order_list = new LinkedList<>();
                Process temp = new Process();
                Process temp1 = new Process();
                
                while (!this.ready_queue.isEmpty()) {
                    if (this.ready_queue.getFirst().Burst_Time > time_quantum) {
                        temp = this.ready_queue.getFirst();
                        temp.Burst_Time = this.ready_queue.getFirst().Burst_Time - time_quantum;
                        this.ready_queue.addLast(temp);
                        out.addLast(temp);

                        out.forEach(i -> {
                            execution_order_list.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));
                        });
                        out.removeFirst();
                        this.ready_queue.removeFirst();
                    } else { //Check if the process needs less than one time quantum to execute
                        temp1 = this.ready_queue.getFirst();
                        temp1.Burst_Time = 0;
                        out.addLast(temp1);
                        out.forEach(i -> {
                            execution_order_list.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));
                        });
                        out.removeFirst();
                        this.ready_queue.removeFirst();
                    }

                }
                return execution_order_list;
            default:
                throw new Exception("Unkown Sorting Method...");
        }
    }
}
