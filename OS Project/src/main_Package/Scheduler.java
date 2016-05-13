package main_Package;

import java.awt.Color;
import java.util.*;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author mn3m
 */
public class Scheduler {

    public static final int FIFO = 0;
    public static final int SJF = 1;
    public static final int SRJF = 2;
    public static final int RR = 3;
    
    private int time_quantum;
    private final LinkedList<Process> ready_queue;
    private final JTextPane log_area;

    public Scheduler(LinkedList<Process> ready_queue, JTextPane log_area) {
        this.ready_queue = new LinkedList<>();
        this.ready_queue.addAll(ready_queue);
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

    private void calculate_statistics(LinkedList<Process> ready) {
        String string = "";
        for (int i = 0; i < 6; i++) {
            ready.get(i).Waiting_Time = 0;
            ready.get(i).TurnAround_Time = 0;
        }

        int smallestIndex = ready.get(0).Arrival_Time;
        for (int i = 5; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                ready.get(i).Waiting_Time += ready.get(j).Burst_Time;
            }
            
            if (ready.get(i).Arrival_Time > smallestIndex & smallestIndex > 0) {
                ready.get(i).Waiting_Time = (ready.get(i).Waiting_Time - ready.get(i).Arrival_Time) + smallestIndex;
            } else if (ready.get(i).Arrival_Time > smallestIndex & smallestIndex == 0) {
                ready.get(i).Waiting_Time -= ready.get(i).Arrival_Time;
            }

            ready.get(i).TurnAround_Time = ready.get(i).Burst_Time + ready.get(i).Waiting_Time;
        }

        double averageWT = 0;
        double averageTAT = 0;
        for (int i = 0; i < 6; i++) {
            averageWT += Math.abs(ready.get(i).Waiting_Time / 6.0);
            averageTAT += Math.abs(ready.get(i).TurnAround_Time / 6.0);
        }

        string = String.format("Average Waiting Time = %.3f\nAverage TurnAround Time = %.3f", averageWT, averageTAT);
        appendToPane(log_area, string + "\n\n", Color.BLACK, true, false);
        log_area.setCaretPosition(log_area.getDocument().getLength());
        System.out.println(string);
    }

    public LinkedList<Process> sort(int sort_method) throws Exception {
        this.time_quantum = 50;
        switch (sort_method) {
            case 0:
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time));
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

    public LinkedList<Process> sort(int sort_method, int time_quantum) throws Exception {
        this.time_quantum = time_quantum;
        switch (sort_method) {
            case 0:
                this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time));
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
