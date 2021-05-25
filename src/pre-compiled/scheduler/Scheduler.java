package scheduler;

import java.util.*;
import parser.ServerSpec;

public class Scheduler {
    public static ServerSpec allToLargest(List<ServerSpec> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        ServerSpec largestServer = servers.get(0);
        for (ServerSpec s : servers) {
            largestServer = largestServer.getCoreCount() < s.getCoreCount() && largestServer.getId() >= s.getId() ? s
                    : largestServer;
        }
        return largestServer;
    }

    public static ServerSpec toCapabSpec(List<ServerSpec> servers){
        return servers.get(0);
    }



    // Helper funtion: Sort asc
    // Algo used: bubble sort
    public static List<ServerSpec> sortAsc(List<ServerSpec> servers){
        // System.out.println(servers);
        int i, j;
        int n = servers.size();
        for (i = 0; i < n-1; i++)     
      
    // Last i elements are already in place 
        for (j = 0; j < n-i-1; j++) 
        if (servers.get(j).getCoreCount() > servers.get(j+1).getCoreCount()){
            ServerSpec temp = servers.get(j);
            servers.set(j, servers.get(j+1));
            servers.set(j+1, temp);
        }
        return servers;
    }

    // Algorithm First Fit:
    //Pros:
    // -- It is fast in processing. As the processor allocates the nearest available memory partition to the job, it is very fast in execution.
    // Cons:
    // -- It wastes a lot of memory. The processor ignores if the size of partition allocated to the job is very large as compared to the size of job or not.
    public static ServerSpec firstFit(List<ServerSpec> servers, int requiredCount){
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        for (ServerSpec s : servers) {
            if (s.getCoreCount() >= requiredCount){
                return s;
            }
        }
        return null;
    }

    // Algorithm Best Fit:
    //Pros:
    // -- Memory Efficient. The operating system allocates the job minimum possible space in the memory, making memory management very efficient.
    // Cons:
    // -- It is a Slow Process. Checking the whole memory for each job makes the working of the operating system very slow. It takes a lot of time to complete the work.
    public static ServerSpec bestFit(List<ServerSpec> servers, int requiredCount){
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        // System.out.print(servers);
        ServerSpec bestFit = servers.get(0);
        // ServerSpec resourceFit = servers.get(0);
        for (ServerSpec server: servers){
            if (server.getCoreCount() <= bestFit.getCoreCount()){
                bestFit = server;
            }
            // if (server.getState() == "Available" && server.getCoreCount() >= requiredCount && server.getCoreCount() <= bestFit.getCoreCount()){
            //     bestFit = server;
            // }
        }
        
        return bestFit;
        
    }

    // Algorithm Worst Fit:
    //Pros:
    // -- Since this process chooses the largest hole/partition, therefore there will be large internal fragmentation. Now, this internal fragmentation will be quite big so that other small processes can also be placed in that leftover partition. 
    // Cons:
    // -- It is a slow process because it traverses all the partitions in the memory and then selects the largest partition among all the partitions, which is a time-consuming process.
    public static ServerSpec worstFit(List<ServerSpec> servers, int requiredCount){
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        ServerSpec worstFit = servers.get(0);
        ServerSpec resourceFit = servers.get(0);
        for (ServerSpec server: servers){
            if (server.getCoreCount() >= requiredCount && server.getCoreCount() <= resourceFit.getCoreCount()){
                resourceFit = server;
            }
            if (server.getState() == "Available" && server.getCoreCount() >= requiredCount && server.getCoreCount() <= worstFit.getCoreCount()){
                worstFit = server;
            }
        }
        if (worstFit != null){
            return worstFit;
        }
        else{
            return resourceFit;
        }
        
    }


}
