/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_Package;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Ahmed
 */
public class Disk_SSTF {
         
   Random rn= new Random();
   public int Current_position= Math.abs((rn.nextInt())%150);
   public LinkedList<Process> IO= new LinkedList<Process>();
   public LinkedList<Process> temp= new LinkedList<Process>();
   
    public Disk_SSTF(LinkedList<Process> ready)
    {
      this.IO=ready;  
    }
    public void Schedule()
    {  System.out.println("The Processes in I/O queue are: ");  
        for(int i=0;i<IO.size();i++)
        {
             
            System.out.println(IO.get(i).PID+"\t"+IO.get(i).sector);
        }    
        System.out.println("The starting head position is: "+Current_position); 
        
        while(IO.isEmpty()==false)
        {
            
            IO.forEach(o-> 
            {           
             temp.add(new Process (o.PID,Math.abs(o.sector-Current_position)));

            });
            temp.sort((b1,b2)->
            {return Integer.compare(b1.sector, b2.sector);}
            );
            Process temp1=temp.getFirst();
        for(int i=0;i<IO.size();i++)
        {
            Process granted=IO.get(i);
             if(granted.PID==temp1.PID)
             {
               System.out.println("Current Head position is sector number: "+Current_position);
                 System.out.println();
               System.out.println("The Process ID is "+granted.PID+"\tThe Sector Number is: "+granted.sector);
               System.out.println();
               Current_position=granted.sector;
               IO.remove(i);
               temp.clear();
               break;
               
             }
        }
            
    
            
            
            
        }   
        
    
    } 
}
