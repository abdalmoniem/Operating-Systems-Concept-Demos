package main_Package;

import java.util.*;
import javax.swing.JOptionPane;

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

      public Scheduler(LinkedList<Process> ready_queue) {
            this.ready_queue = new LinkedList<>();
            this.ready_queue.addAll(ready_queue);
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

                  ready.get(i).TurnAround_Time = ready.get(i).Burst_Time + ready.get(i).Waiting_Time;
            }

            double averageWT = 0;
            double averageTAT = 0;
            for (int i = 0; i < 6; i++) {
                  averageWT += ready.get(i).Waiting_Time / 6.0;
                  averageTAT += ready.get(i).TurnAround_Time / 6.0;
            }
            
            string = String.format("Average Waiting Time = %.3f\n Average TurnAround Time = %.3f", averageWT, averageTAT);
            JOptionPane.showMessageDialog(null, string, "Results", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(string);
      }

      public LinkedList<Process> sort(int sort_method) throws Exception {
            this.time_quantum = 50;
            switch (sort_method) {
                  case 0:
                        this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Arrival_Time, p2.Arrival_Time));
                        calculate_statistics(ready_queue);
                        return this.ready_queue;
                  case 1:
                        this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Burst_Time, p2.Burst_Time));
                        calculate_statistics(ready_queue);
                        return this.ready_queue;
                  case 2:
                        //Sort using SRJF
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
                        calculate_statistics(ready_queue);
                        return this.ready_queue;
                  case 1:
                        this.ready_queue.sort((p1, p2) -> Integer.compare(p1.Burst_Time, p2.Burst_Time));
                        calculate_statistics(ready_queue);
                        return this.ready_queue;
                  case 2:
                        //Sort using SRJF
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
                        calculate_statistics(execution_order_list);
                        return execution_order_list;
                  default:
                        throw new Exception("Unkown Sorting Method...");
            }
      }
}
