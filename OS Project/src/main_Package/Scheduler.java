package main_Package;

import java.awt.Color;
import java.util.*;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Scheduler {
/**
 * includes the arrangement of the schedular methods
 */
    public static final int FIFO = 0;
    public static final int SJF = 1;
    public static final int SRJF = 2;
    public static final int RR = 3;

    private int time_quantum;
    private final LinkedList<Process> ready_queue;
    private final JTextPane log_area;
/**
 * 
 * @param ready_queue list contain the process before scheduling
 * @param log_area  
 */
    public Scheduler(LinkedList<Process> ready_queue, JTextPane log_area) {
        this.ready_queue = new LinkedList<>();// creat a list 
       
        this.ready_queue.addAll(ready_queue);// put the unscheduled process in it.
        this.log_area = log_area;
    }

    /**
     * @param pane the JTextPane where you want to append text to
     * @param text the text that you want to append to the JTextPane, Note: add
     * an end line character in the end if you want new text to be appended in a
     * new line
     * @param color the color of the text
     * @param bold if true the text will be appended in bold, else it will be
     * appended PLAIN
     * @param italic if true the text will be appended in italic, else it will
     * be appended PLAIN or Bold as specified by the bold parameter
     */
    private void appendToPane(JTextPane pane, String msg, Color color, boolean bold, boolean italic) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        try {
            StyleConstants.setForeground(attr, color);
            StyleConstants.setBold(attr, bold);
            StyleConstants.setItalic(attr, italic);
            doc.insertString(doc.getLength(), msg, attr);
            pane.setCaretPosition(pane.getDocument().getLength());
        } catch (BadLocationException ex) {
            System.err.println(ex.toString());
        }
    }
/**
 * 
 * @param ready 
 */
    private void calculate_statistics(LinkedList<Process> ready) {
        String string = "";
        for (int i = 0; i < 6; i++) {
            ready.get(i).Waiting_Time = 0;// getting the process from ready and put the waiting and burst time = zero
            ready.get(i).TurnAround_Time = 0;
        }

        int smallestIndex = ready.get(0).Arrival_Time; //pick the arrival time of the smallest index
        for (int i = 5; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                ready.get(i).Waiting_Time += ready.get(j).Burst_Time; //the waiting time of process i = it's waiting time plus the burst time of the processj
            }
            
            if (ready.get(i).Arrival_Time > smallestIndex & smallestIndex > 0) { //if the arrival time of i bigger than the smallest index and bigger than zero
                ready.get(i).Waiting_Time = (ready.get(i).Waiting_Time - ready.get(i).Arrival_Time) + smallestIndex;// then waiting time of process i - it's waiting time minus arrival time plus index
            } else if (ready.get(i).Arrival_Time > smallestIndex & smallestIndex == 0) {//if the arrival time of process i bigger than the smaalest index and bigger than zero
                ready.get(i).Waiting_Time -= ready.get(i).Arrival_Time;// waiting time of i - it's waiting- arrival time
            }

            ready.get(i).TurnAround_Time = ready.get(i).Burst_Time + ready.get(i).Waiting_Time; //turnaroundtime of process i = it's burst time+waittime
        }

        double averageWT = 0;
        double averageTAT = 0;
        for (int i = 0; i < 6; i++) {
            averageWT += Math.abs(ready.get(i).Waiting_Time / 6.0); //calculate the average wait time of all process(adding then divided by their number)
            averageTAT += Math.abs(ready.get(i).TurnAround_Time / 6.0);//calculate the average turn arround time of all processes(adding then divided by their number)
        }

        string = String.format("Average Waiting Time = %.3f\nAverage TurnAround Time = %.3f", averageWT, averageTAT);//put the average wait time and the turn arround time on variable called string
        appendToPane(log_area, string + "\n\n", Color.BLACK, true, false); //append this string to pane
        log_area.setCaretPosition(log_area.getDocument().getLength());//set it's position
        System.out.println(string);//print string on the pane
    }
/**
 * 
 * @param sort_method this param specify which method we will use to sort the processes
 * @return a list of an arranged processes
 * @throws Exception 
 */
    public LinkedList<Process> sort(int sort_method) throws Exception {
        this.time_quantum = 50; //allocate the quantum no to 50
        switch (sort_method) {
            case 0: // sort using FiFO
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time)); // sort the processes according to their arrival time
                appendToPane(log_area, "[FIFO]:\n", Color.RED, true, false); //append the result in a pane and specify the pane's properties
                calculate_statistics(ready_queue); // calculate the average weight time and turn around time and print them
                return this.ready_queue; //return tha arranged list of process
            case 1:
                // sort using SJF
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Burst_Time, p2.Burst_Time));//sort the process according to it's burst time
                appendToPane(log_area, "[SJF]:\n", Color.RED, true, false);//append the result in a pane and specify the pane's properties
                calculate_statistics(ready_queue);// calculate the average weight time and turn around time and print them
                return this.ready_queue;//return tha arranged list of process
            case 2:
                //Sort using SRJF
                appendToPane(log_area, "[SRJF]:\n", Color.RED, true, false);//append the result in a pane and specify the pane's properties
                calculate_statistics(ready_queue);// calculate the average weight time and turn around time and print them
                return this.ready_queue;//return tha arranged list of process
            case 3:
                //sort using Round Robin
                LinkedList<Process> out = new LinkedList<>();
                LinkedList<Process> execution_order_list = new LinkedList<>();
                Process temp = new Process();
                Process temp1 = new Process();

                while (!this.ready_queue.isEmpty()) {
                    if (this.ready_queue.getFirst().Burst_Time > time_quantum) {//if the process burst time smaller than the quantum
                        temp = this.ready_queue.getFirst(); // then put this process in temp
                        temp.Burst_Time = this.ready_queue.getFirst().Burst_Time - time_quantum;// the time remaining from the process = the initial burst time minus quantum time
                        this.ready_queue.addLast(temp); //add the process  with it's new burst time in the ready queue
                        out.addLast(temp); //add the temp process in a list (out)

                        out.forEach(i -> {
                            execution_order_list.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));// adding the excuted process according to it's order in a list
                        });
                        out.removeFirst(); //remove the first process from out
                        this.ready_queue.removeFirst();//remove the first process from ready queue when burst time =0
                    } else { //Check if the process needs less than one time quantum to execute
                        temp1 = this.ready_queue.getFirst();// get it from ready queue
                        temp1.Burst_Time = 0;// excute the process, burst time = zero
                        out.addLast(temp1); //put the process in out list
                        out.forEach(i -> {
                            execution_order_list.add(new Process(i.PID, i.Burst_Time, i.Arrival_Time, i.Priority));// put it in the order of excution list
                        });
                        out.removeFirst(); //remove the process from out
                        this.ready_queue.removeFirst();//remove the process from ready queue
                    }
                }
                appendToPane(log_area, "[RR]:\n", Color.RED, true, false);
                calculate_statistics(execution_order_list);//calculate and print the average and turnaroun time
                return execution_order_list;//return the order of the excution
            default:
                throw new Exception("Unkown Sorting Method...");
        }
    }
/**
 * 
 * @param sort_method specify which method will schedule the process
 * @param time_quantum specify the quantum number
 * @return a list contain the order of execution
 * @throws Exception 
 */
    public LinkedList<Process> sort(int sort_method, int time_quantum) throws Exception {
        this.time_quantum = time_quantum; //allocate the quantum number
        switch (sort_method) {
            case 0:///sort by fifo
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time));// sort the processes according to their arrival time
                appendToPane(log_area, "[FIFO]:\n", Color.RED, true, false);
                calculate_statistics(ready_queue);
                return this.ready_queue;
            case 1:
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Burst_Time, p2.Burst_Time));
                appendToPane(log_area, "[SJF]:\n", Color.RED, true, false);
                calculate_statistics(ready_queue);
                return this.ready_queue;
            case 2:
                //Sort using SRJF
                appendToPane(log_area, "[SRJF]:\n", Color.RED, true, false);
                calculate_statistics(ready_queue);
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
                appendToPane(log_area, "[RR]:\n", Color.RED, true, false);
                calculate_statistics(execution_order_list);
                return execution_order_list;
            default:
                throw new Exception("Unkown Sorting Method...");
        }
    }
}
