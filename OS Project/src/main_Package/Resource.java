package main_Package;

import java.util.Random;

/**
 *
 * @author mn3m
 */
public class Resource {
    private int A;
    private int B;
    private int C;
    
    public Resource() {
        Random r = new Random();
        this.A = Math.abs(r.nextInt(20));
        this.B = Math.abs(r.nextInt(20));
        this.C = Math.abs(r.nextInt(20));
    }
    
    public Resource(int A, int B, int C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }
    
    public boolean Compare(Resource other) {
        return (this.A <= other.A &&
                this.B <= other.B &&
                this.C <= other.C);
    }
    
    public void Add(Resource other) {
        this.A += other.A;
        this.B += other.B;
        this.C += other.C;
    }
    
    public int get_A() {
        return this.A;
    }
    
    public int get_B() {
        return this.B;
    }
    
    public int get_C() {
        return this.C;
    }
}
