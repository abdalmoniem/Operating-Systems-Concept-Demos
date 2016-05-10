/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_Package;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmed
 */
public class Disk_Scheduler {

      public static final int SSTF = 0;
      public static final int C_LOOK = 1;
      private Random rn = new Random();
      private int Current_position = Math.abs((rn.nextInt()) % 150);
      private LinkedList<Process> IO = new LinkedList<>();
      private LinkedList<Process> temp = new LinkedList<>();
      private LinkedList<Process> res = new LinkedList<>();

      public Disk_Scheduler(LinkedList<Process> ready) {
            this.IO = ready;
      }

      public String Schedule(int mode) {
            String args = "";
            switch (mode) {
                  case 0:
                        args = "";
                        System.out.println("The Processes in I/O queue are: ");
                        IO.forEach(i -> System.out.println(i.PID + "\t" + i.Sector));
                        System.out.println("The starting head position is: " + Current_position);
                        while (IO.isEmpty() == false) {
                              IO.forEach(o -> temp.add(new Process(o.PID, Math.abs(o.Sector - Current_position))));
                              temp.sort((b1, b2) -> {
                                    return Integer.compare(b1.Sector, b2.Sector);
                              });
                              Process temp1 = temp.getFirst();
                              for (int i = 0; i < IO.size(); i++) {
                                    Process granted = IO.get(i);
                                    if (granted.PID == temp1.PID) {
                                          System.out.println("Current Head position is sector number: " + Current_position);
                                          System.out.println("The Process ID is " + granted.PID + "\tThe Sector Number is: " + granted.Sector);
                                          Current_position = granted.Sector;
                                          res.add(granted);
                                          args += " " + granted.Sector;
                                          IO.remove(i);
                                          temp.clear();
                                          break;
                                    }
                              }
                        }
                        System.out.println("Seek Order:");
                        res.forEach(i -> System.out.println(i.PID + "\t" + i.Sector));
                        break;
                  case 1:
                        //clook
                        break;
            }
            return args;
      }
}
