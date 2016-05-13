/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_Package;

/**
 *
 * @author Ahmed
 */
public class Memory_Location 
{
    public int Usage;
    public long Allocation_Time;
    public int PID;
    public int Data;
    
    public Memory_Location(int Usage,int Allocation_Time,int PID,int Data)
    {
        this.Usage=Usage;
        this.Allocation_Time=Allocation_Time;
        this.PID=PID;
        this.Data=Data;
    }
}
