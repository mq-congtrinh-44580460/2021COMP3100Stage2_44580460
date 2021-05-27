package scheduler;

import java.util.*;
import parser.ServerSpec;
import parser.JobSpec;

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

    // Algorithm Best Fit:
    //Pros:
    // -- Memory Efficient. The operating system allocates the job minimum possible space in the memory, making memory management very efficient.
    // Cons:
    // -- It is a Slow Process. Checking the whole memory for each job makes the working of the operating system very slow. It takes a lot of time to complete the work.
    public static ServerSpec bestFit(List<ServerSpec> servers, JobSpec job){
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        // System.out.print(servers);
        ServerSpec bestFit = servers.get(0);
        // int shortestTime = bestFit.getEstimatedRuntime;
        // ServerSpec resourceFit = servers.get(0);
        for (ServerSpec server: servers){
            // if (server.getCoreCount() <= bestFit.getCoreCount()){
            if ((server.getRunningJobs() == 0) || (server.getWaitingJobs() == 0)){
                server.setEstimatedRuntime(server.getEstimatedRuntime() + job.getEstRuntime());
                return server;
            }
                if (((job.getSubmitTime() > server.getEstimatedRuntime()) && (server.getEstimatedRuntime() < bestFit.getEstimatedRuntime() + job.getEstRuntime()/100*20) && (server.getHourlyRate() <= bestFit.getHourlyRate())
                
                )){
                    bestFit = server;
                }
                else
                if ((server.getCurrentCore() < bestFit.getCurrentCore()) || (server.getHourlyRate() <= bestFit.getHourlyRate())){
                    bestFit = server;
                }
            
        }
            // if (server.getState() == "Available" && server.getCoreCount() >= requiredCount && server.getCoreCount() <= bestFit.getCoreCount()){
            //     bestFit = server;
            // }
        
        bestFit.setEstimatedRuntime(bestFit.getEstimatedRuntime() + job.getEstRuntime());
        return bestFit;
        
    }



}
