package main_Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class Process {

    public int PID; 
    public int Burst_Time;
    public int Arrival_Time;
    public int Waiting_Time;
    public int TurnAround_Time;
    public int Priority;
    public int Sector;
    public Resource Need;

    HashMap<Integer, Integer> Page_Table = new HashMap<>();
/**
 * constructor which generates a randomized pid, burst time, arrival, priority, and needs 
 */
    public Process() {
        Random r = new Random();
        this.PID = Math.abs(r.nextInt() % 30);
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 200);
        this.Need = new Resource();
    }
/**
 * 
 * a constructor contains a randomized available ; except the process pid
 */
    public Process(int PID) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 200);
        this.Need = new Resource();
    }
    /**
     * this constructor allocate the pid $ the sector
     * @param PID initiate the process's pid
     * @param Sector initiate the sector's value
     */
   public Process(int PID, int Sector) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Sector;
        this.Need = new Resource();
    }
/**
 * this constructor allocate the pid and the need of the process
 * @param PID initiate  the process's pid
 * @param Need initiate the process's need
 * randomized the priority, sector and burst time.
 */
    public Process(int PID, Resource Need) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 150);
        this.Need = Need;
    }
/**
 * 
 * @param PID initiate the process pid
 * @param Burst_Time initiate the burst time
 * @param Arrival_Time initiate the arrival time
 * @param Priority allocate the process's priority 
 */
    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority) {
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
    }
/**
 * 
 * @param PID allocate the pid
 * @param Burst_Time allocate the burst time
 * @param Arrival_Time allocate the arrival time
 * @param Priority allocate the priority of the process
 * @param Need allocate the process's needs
 * Randomized the sector's value
 */
    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority, Resource Need) {
        Random rn = new Random();
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
        this.Sector = Math.abs(rn.nextInt() % 200);
        this.Need = Need;
    }
/**
 * 
 * @param  allocate the pid
 * @param Burst_Time allocate the burst time 
 * @param Arrival_Time allocate the arrival time
 * @param Priority allocate the priority
 * @param Sector allocate the sector's value
 * @param Need  allocate the process's needs
 */
    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority, int Sector, Resource Need) {
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
        this.Sector = Sector;
        this.Need = Need;
    }
/**
 * this function show how we remove pages from memory according toLRU principle
 * @param frame_numbers_to_remove determine the frame numbers which will be removes
 * @param Memory determine the memory allocation
 * @param Back_Store a list to save the removed pages from the memory(hard disk)
 */
    public void LRU(int frame_numbers_to_remove, ArrayList<Memory_Location> Memory, LinkedList<Memory_Location> Back_Store) {
        for (int x = 0; x < frame_numbers_to_remove; x++) {
            int min_Index = 0;
            int min_usage = Integer.MAX_VALUE; // the page's usage number
            for (int i = 0; i < Memory.size(); i++) {
                Memory_Location current = Memory.get(i);
                if ((current.Usage < min_usage) && (current.PID != this.PID)) {
                    min_usage = current.Usage; // search for the page which has the minimum usage.
                    min_Index = i;

                }
            }
            Back_Store.addLast(Memory.get(min_Index)); //put the page which has minimum usage in the backstore
            Memory.set(min_Index, new Memory_Location(0, 0, -1, 0)); // removing this page from the memory

        }
    }
/**
 * this function is removing pages according to FIFO principal
 * @param frame_numbers_to_remove allocate no of frames to be removed
 * @param Memory list of memory
 * @param Back_Store list to save the removed frames
 */
    public void FIFO(int frame_numbers_to_remove, ArrayList<Memory_Location> Memory, LinkedList<Memory_Location> Back_Store) {
        for (int x = 0; x < frame_numbers_to_remove; x++) { //loop arround all memroy allocation to get the oldest page in the linked list
            int min_Index = 0; 
            int age = Integer.MAX_VALUE; // the age of the frames
            for (int i = 0; i < Memory.size(); i++) {
                Memory_Location current = Memory.get(i); //get memory allocation i
                if ((current.Allocation_Time < age) && (current.PID != this.PID)) {
                    age = current.Allocation_Time; //search for the page which has the maximun age in the memory(allocated in the memory for alongtime)
                    min_Index = i; //get it's index in the list

                }
            }
            Back_Store.addLast(Memory.get(min_Index)); //put the oldest page in the bachstore
            Memory.set(min_Index, new Memory_Location(0, 0, -1, 0)); //remove the oldest page from the memory
        }

    }
/**
 * this function show how we allocate pages in the memory
 * @param page_numbers allocate the page numbers
 * @param Memory list of memory allocation
 * @param mode specify how we make swap between pages
 * @param Back_Store a list contains the removed pages from the memory(hard disk)
 */
    public void allocate_memory(int page_numbers, ArrayList<Memory_Location> Memory, int mode, LinkedList<Memory_Location> Back_Store) {
        if (page_numbers < Memory.size()) {
            for (int i = 0; i < page_numbers; i++) {
                Page_Table.put(i, -1);
            }
            int page_counter = 0;
            int remaining_pages = page_numbers;
            int counter = 0;

            for (int i = 0; i < Memory.size(); i++) {
                if (Memory.get(i).PID != -1) {
                    counter++;
                }
            }

            while (remaining_pages > 0) {

                if (counter >= Memory.size()) {
                    System.out.println("Memory is full, Running LRU");
                    System.out.println("Old Memory is:");
                    Memory.forEach(o
                            -> {
                        System.out.println(o.PID + "\t" + o.Data + "\t" + o.Usage + "\t" + o.Allocation_Time);
                    });

                    if (mode == 0) {
                        LRU(remaining_pages, Memory, Back_Store);
                    } else if (mode == 1) {
                        FIFO(remaining_pages, Memory, Back_Store);
                    }
                    System.out.println("New Memory is:");
                }

                for (int i = 0; i < Memory.size(); i++) {
                    Memory_Location current = Memory.get(i);
                    if (current.PID == -1) {
                        remaining_pages--;
                        current.PID = this.PID;
                        current.Allocation_Time = this.PID;
                        current.Usage = Math.abs(new Random().nextInt(10));
                        if (page_counter < page_numbers) {
                            Page_Table.replace(page_counter, i);
                            page_counter++;
                            counter++;
                        }
                        break;
                    }

                }

            }
      
        } else {
            int pages_to_backstore = page_numbers - Memory.size();
            for (int i = 0; i < pages_to_backstore; i++) {
                Back_Store.addLast(new Memory_Location(0, 0, this.PID, 0));
            }
            this.allocate_memory(Memory.size(), Memory, 1, Back_Store);
        }
          System.out.println("The Page Table:");
        Iterator it = Page_Table.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + "\t" + pair.getValue());
        }
    }

    public void chk_pageTable(ArrayList<Memory_Location> Memory) {
        for (int i = 0; i < this.Page_Table.size(); i++) {
            int position_to_check = this.Page_Table.get(i);
            if (Memory.get(position_to_check).PID == this.PID) {
            } else {
                this.Page_Table.replace(i, -1);
            }
        }
    }

    public void data_addition(int added, ArrayList<Memory_Location> Memory) {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose the number of integer you want to add to");
        int page_number = input.nextInt();
        chk_pageTable(Memory);
        try {
            if (Page_Table.get(page_number) == -1) {
                System.out.println("Page Fault");
            } else {
                Memory.get(page_number).Data += added;
                System.out.println("The New Edited Data= " + Memory.get(page_number).Data);
            }
        } catch (NullPointerException e) {
            System.out.println("Illegal Access");
        }

    }
}
