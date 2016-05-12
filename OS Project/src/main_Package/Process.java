package main_Package;

import java.util.Random;

/**
 *
 * @author mn3m
 */
public class Process {

    public int PID;
    public int Burst_Time;
    public int Arrival_Time;
    public int Waiting_Time;
    public int TurnAround_Time;
    public int Priority;
    public int Sector;
    public Resource Need;

    public Process() {
        Random r = new Random();
        this.PID = Math.abs(r.nextInt() % 30);
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 200);
        this.Need = new Resource();
    }

    public Process(int PID) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 200);
        this.Need = new Resource();
    }

    public Process(int PID, int Sector) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Sector;
        this.Need = new Resource();
    }

    public Process(int PID, Resource Need) {
        Random r = new Random();
        this.PID = PID;
        this.Burst_Time = Math.abs(r.nextInt() % 100) + 10;
        this.Arrival_Time = Math.abs(r.nextInt() % 10) + 1;
        this.Priority = Math.abs(r.nextInt() % 30);
        this.Sector = Math.abs(r.nextInt() % 150);
        this.Need = Need;
    }

    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority) {
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
    }

    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority, Resource Need) {
        Random rn = new Random();
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
        this.Sector = Math.abs(rn.nextInt() % 200);
        this.Need = Need;
    }

    public Process(int PID, int Burst_Time, int Arrival_Time, int Priority, int Sector, Resource Need) {
        this.PID = PID;
        this.Burst_Time = Burst_Time;
        this.Arrival_Time = Arrival_Time;
        this.Priority = Priority;
        this.Sector = Sector;
        this.Need = Need;
    }
}
