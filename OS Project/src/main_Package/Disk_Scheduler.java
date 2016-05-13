package main_Package;

import java.util.LinkedList;
import java.util.Random;


public class Disk_Scheduler {

    public static final int SSTF = 0;
    public static final int C_LOOK = 1;
    private int current_position; // the position of the disk's head
    private LinkedList<Process> io_queue = new LinkedList<>(); // the queue which contains the requests before scheduling in sstf mode
    private final LinkedList<Process> temp = new LinkedList<>();
    private final LinkedList<Process> temp1 = new LinkedList<>();//contains the requests which has position after the head's current position in clook mode 
    private final LinkedList<Process> temp2 = new LinkedList<>();//contains the requests which has position brfore the head's current position in clook mode
    private final LinkedList<Integer> res = new LinkedList<>();//contains the requests after scheduling in sstf mode

    /**
     *
     * @param IO : contains the disk's requests before scheduling
     */
    public Disk_Scheduler(LinkedList<Process> IO) {
        Random rn = new Random();
        this.current_position = Math.abs((rn.nextInt()) % 200);//generating random current position
        this.io_queue = IO;//putting the requests in the io queue
    }

    /**
     *
     * @param IO : contains the disk's requests before scheduling
     * @param current_position : the disk's head current position
     */
    public Disk_Scheduler(LinkedList<Process> IO, int current_position) {
        this.current_position = current_position;
        this.io_queue = IO;
    }

    /**
     *
     * @param mode : according to the value of this variable , the requests will
     * be scheduled in sstf mode or clook mode.
     * @return args which contains the arrange of the requests after scheduling
     */
    public String Schedule(int mode) {
        String args = "";
        res.clear();
        res.add(current_position);//adding the head position to the res
        switch (mode) {
            case 0:
                System.out.println("The Processes in I/O queue are: ");
                io_queue.forEach(i -> System.out.println(i.PID + "\t" + i.Sector));// print the waiting requests before scheduling
                System.out.println("The starting head position is: " + current_position);//print the first value of the head's disk
                while (io_queue.isEmpty() == false) {
                    io_queue.forEach(o -> temp.add(new Process(o.PID, Math.abs(o.Sector - current_position))));//adding all the requests in temp after subtracting the current position from the secor.
                    temp.sort((b1, b2) -> {
                        return Integer.compare(b1.Sector, b2.Sector);//then arrange them according to the smallest sector's value
                    });
                    Process temp1 = temp.getFirst();// pick the first process in temp
                    for (int i = 0; i < io_queue.size(); i++) {
                        Process granted = io_queue.get(i); 
                        if (granted.PID == temp1.PID) {
                            System.out.println("Current Head position is sector number: " + current_position);
                            System.out.println("The Process ID is " + granted.PID + "\tThe Sector Number is: " + granted.Sector);
                            current_position = granted.Sector;
                            res.add(granted.Sector);// put the request in res after it has been excuted.
                            io_queue.remove(i);//remove the request from io
                            temp.clear();//remove the request from temp
                            break;
                        }
                    }
                }
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
                    current_position = i.Sector;//change the value of the head position to the current Sector
                    res.add(current_position);
                    System.out.println("Current Head position is sector number: " + current_position);//print the value of the head position at this moment
                });
                System.out.println("moving left : ");//the head moves left
                temp2.forEach(i -> {
                    System.out.println("The Process ID is " + i.PID + "\tThe Sector Number is: " + i.Sector);//print the process after scheduling , when the head moves left.
                    current_position = i.Sector;//change the value of the head position to the current Sector
                    res.add(current_position);//put tje requst in res after it has been excuted
                    System.out.println("Current Head position is sector number: " + current_position);//print the value of the head position at this moment
                });
                break;
        }
        System.out.println("Seek Order:");//print the seek order
        res.forEach(i -> System.out.print(i + " -> "));
        System.out.println("\b \b\b \b\b \b");

        for (int i = 0; i < res.size(); i++) {
            args += " " + res.get(i);//put the result of the scheduling in arg[]
        }
        return args;
    }
}
