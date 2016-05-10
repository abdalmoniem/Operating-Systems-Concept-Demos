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
    private int current_position;
    private LinkedList<Process> io_queue = new LinkedList<>();
    private LinkedList<Process> temp = new LinkedList<>();
    private LinkedList<Process> temp1 = new LinkedList<Process>();
    private LinkedList<Process> temp2 = new LinkedList<Process>();
    private LinkedList<Integer> res = new LinkedList<>();

    public Disk_Scheduler(LinkedList<Process> IO) {
        Random rn = new Random();
        this.current_position = Math.abs((rn.nextInt()) % 200);
        this.io_queue = IO;
    }
    
    public Disk_Scheduler(LinkedList<Process> IO, int current_position) {
        this.current_position = current_position;
        this.io_queue = IO;
    }

    public String Schedule(int mode) {
        String args = "";
        res.clear();
        res.add(current_position);
        switch (mode) {
            case 0:
                System.out.println("The Processes in I/O queue are: ");
                io_queue.forEach(i -> System.out.println(i.PID + "\t" + i.Sector));
                System.out.println("The starting head position is: " + current_position);
                while (io_queue.isEmpty() == false) {
                    io_queue.forEach(o -> temp.add(new Process(o.PID, Math.abs(o.Sector - current_position))));
                    temp.sort((b1, b2) -> {
                        return Integer.compare(b1.Sector, b2.Sector);
                    });
                    Process temp1 = temp.getFirst();
                    for (int i = 0; i < io_queue.size(); i++) {
                        Process granted = io_queue.get(i);
                        if (granted.PID == temp1.PID) {
                            System.out.println("Current Head position is sector number: " + current_position);
                            System.out.println("The Process ID is " + granted.PID + "\tThe Sector Number is: " + granted.Sector);
                            current_position = granted.Sector;
                            res.add(granted.Sector);
                            io_queue.remove(i);
                            temp.clear();
                            break;
                        }
                    }
                }
                System.out.println("Seek Order:");
                res.forEach(i -> System.out.println(i));
                break;
            case 1:
                System.out.println("The Processes in I/O queue are: ");

                // printing the processes before scheduling
                io_queue.forEach(i -> System.out.println(i.PID + "\t" + i.Sector));
                System.out.println("The starting head position is: " + current_position);// print the current head position.

                io_queue.forEach(i -> {
                    if (i.Sector > current_position) {
                        temp1.add(new Process(i.PID, Math.abs(i.Sector)));// if the process's Sector bigger than the head's current position, add it on temp1.
                    } else {
                        temp2.add(new Process(i.PID, Math.abs(i.Sector)));//if the process's Sector smaller than the head's current position, add it on temp2.
                    }
                });
                temp1.sort((b1, b2) -> {
                    return Integer.compare(b1.Sector, b2.Sector);
                }); //arranging the processes in temp1 in ascending order according to the sectors' values.
                temp2.sort((b1, b2) -> {
                    return Integer.compare(b1.Sector, b2.Sector);
                });//arranging the processes in temp2 in ascending order according to the sectors' values.

                System.out.println("moving right : ");// the head moves right.
                temp1.forEach(i -> {
                    System.out.println("The Process ID is " + i.PID + "\tThe Sector Number is: " + i.Sector);//printing the process after schedulung; but when the head moves at the right position 
                    System.out.println();
                    current_position = i.Sector;//change the value of the head position to the current Sector
                    res.add(current_position);
                    System.out.println("Current Head position is sector number: " + current_position);//print the value of the head position at this moment
                });
                System.out.println("moving left : ");//the head moves left
                temp2.forEach(i -> {
                    System.out.println("The Process ID is " + i.PID + "\tThe Sector Number is: " + i.Sector);//print the process after scheduling , when the head moves left.
                    System.out.println();
                    current_position = i.Sector;//change the value of the head position to the current Sector
                    res.add(current_position);
                    System.out.println("Current Head position is sector number: " + current_position);//print the value of the head position at this moment
                    System.out.println();
                });
                System.out.println("Seek Order:");
                res.forEach(i -> System.out.println(i));
                break;
        }
        
        for(int i=0; i<res.size(); i++)
            args += " " + res.get(i);
        return args;
    }
}
