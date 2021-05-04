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
}
