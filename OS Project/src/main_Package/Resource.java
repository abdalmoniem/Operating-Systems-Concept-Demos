package main_Package;

import java.util.Random;

public class Resource {
    private int A;
    private int B;
    private int C;
    /**
     * this constructor is randomized the value of the resources A,B& C.
     */
    public Resource() {
        Random r = new Random();
        this.A = Math.abs(r.nextInt(30));
        this.B = Math.abs(r.nextInt(30));
        this.C = Math.abs(r.nextInt(30));
    }
    /**
     * Initiating the resources values
     * @param A initiate A
     * @param B initiate B
     * @param C initiate B
     */
    public Resource(int A, int B, int C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }
    /**
     * Function which compares the resources of process with another process
     * @param other allocate another process's resources
     * @return true if all the resources of the first process smaller than the second & false if one of the first process bigger than the other
     * (compare between needs an available)
     */
    public boolean Compare(Resource other) {
        return (this.A <= other.A &&
                this.B <= other.B &&
                this.C <= other.C);
    }
    /**
     * 
     * @param put another process's resources to add it's value on the first one
     */
    public void Add(Resource other) {
        this.A += other.A;// add resource A of the first on resource A of the second
        this.B += other.B;// add resource b of the first on resource b of the second
        this.C += other.C;// add resource c of the first on resource c of the second
    }
    /**
     * 
     * @return  the value of resource A
     */
    public int get_A() {
        return this.A;
    }
    /**
     * 
     * @return the value of resource b
     */
    public int get_B() {
        return this.B;
    }
    /**
     * 
     * @return the value of resource c
     */
    public int get_C() {
        return this.C;
    }
}
