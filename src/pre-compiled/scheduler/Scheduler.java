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

    // Algorithm Advance First Fit:
    public static ServerSpec advFF(List<ServerSpec> servers, JobSpec job){
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        //First fit approach if there are available servers that are capable of perform the job
        ServerSpec bestFit = servers.get(0);
        if (bestFit.getState() == "inactive"){
            //Calculate and set the estimated run timem for server when booting up
            bestFit.setEstimatedRuntime(bestFit.getEstimatedRuntime() + bestFit.getBootupTime() + job.getSubmitTime() + job.getEstRuntime());
            return bestFit;
        }
        //Once all the server is started , this code will search for capable server that can perform concurrencies
        for (ServerSpec server: servers){
            //concurrencies search
            if (((job.getSubmitTime() < server.getEstimatedRuntime()) && (server.getEstimatedRuntime() != -1) && (server.getCurrentCore() >= job.getCore())) 
                ){
                    if (server.getEstimatedRuntime() < job.getSubmitTime() + job.getEstRuntime())
                        server.setEstimatedRuntime(server.getEstimatedRuntime() + (server.getEstimatedRuntime() - job.getSubmitTime() - job.getEstRuntime()));
                return server;
            }
            else
            //Find capable server that finish earliest or cheapest
            if (((server.getEstimatedRuntime() < bestFit.getEstimatedRuntime()) || (server.getHourlyRate() <= bestFit.getHourlyRate()))){
                bestFit = server;
            }
            
        }
            
        //Calculate and set the estimated run timem for selected server
        bestFit.setEstimatedRuntime(bestFit.getEstimatedRuntime() + job.getSubmitTime() + job.getEstRuntime());
        return bestFit;
        
    }



}
