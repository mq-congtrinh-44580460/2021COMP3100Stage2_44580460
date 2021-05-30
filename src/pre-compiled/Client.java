import java.net.*;
import java.io.*;
import java.util.*;
import parser.*;
import scheduler.*;

public class Client {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 50000;
    public static final String HELO = "HELO";
    public static final String AUTH = "AUTH";
    public static final String QUIT = "QUIT";
    public static final String REDY = "REDY";
    public static final String DATA = "DATA";
    public static final String JOBN = "JOBN";
    public static final String NONE = "NONE";
    public static final String GETS = "GETS";
    public static final String SCHD = "SCHD";
    public static final String ERR = "ERR";
    public static final String OK = "OK";
    public static final String DOT = ".";

    public static void send(DataOutputStream out, String request) {
        try {
            out.write((request + "\n").getBytes());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Socket s = null;
        // String algo = "";
        // if (args.length > 1 && args[0] == "-a") {
        // algo = args[1];
        // }

        try {
            s = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String request = HELO;
            send(out, request);

            int nRecs = 0;
            int counter = 0;
            // int Availables = 0;
            JobSpec jobSpec = new JobSpec();
            ServerSpec scheduled = new ServerSpec();
            List<ServerSpec> servers = new ArrayList<ServerSpec>();
            List<ServerSpec> capables = new ArrayList<ServerSpec>();
            List<JobSpec> jobs = new ArrayList<JobSpec>();

            while (true) {
                String response = in.readLine();
                // System.out.println(response);
                // Greeting request
                if (request.equals(HELO) && response.equals(OK)) {
                    String username = System.getProperty("user.name");

                    request = AUTH + " " + username;
                    send(out, request);
                    continue;
                }
                // Authen request
                if (request.startsWith(AUTH) && response.equals(OK)) {
                    servers = Parser.parseServer();
                    
                    //Sort all the servers in ascending order
                    servers = Scheduler.sortAsc(servers);

                    request = REDY;
                    send(out, request);
                    continue;
                }
                // Receiving and handling jobs
                if (request.equals(REDY)) {
                    if (response.equals(NONE)) {
                        request = QUIT;
                    } else {
                        String[] job = response.split("\\s+");

                        if (job[0].equals(JOBN)) {
                            jobSpec = Parser.parseJob(job);
                            jobs.add(jobSpec);

                            // First is to use available server
                            request = GETS + " Avail " + job[4] + " " + job[5] + " " + job[6];

                        }
                    }
                    send(out, request);
                    continue;
                }
                //Parse the data response
                if (request.startsWith(GETS)) {
                    String[] data = response.split("\\s+");

                    if (data[0].equals(DATA)) {
                        nRecs = Integer.parseInt(data[1]);

                        request = OK;
                        send(out, request);
                        continue;
                    }
                }

                if (request.equals(OK)) {
                    if (response.equals(DOT)) {
                        if (nRecs == 0) {
                            // Capable is used when there is no available servers
                            request = GETS + " Capable " + jobSpec.getCore() + " " + jobSpec.getMemory() + " "
                                    + jobSpec.getDisk();
                            // scheduled = Scheduler.bestFit(servers, jobSpec.getCore());
                            // request = GETS + "All";
                        } else {
                            scheduled = Scheduler.advFF(capables, jobSpec);
                            for (ServerSpec server : servers) {
                                if (server.getType().equals(scheduled.getType())
                                        && server.getId() == scheduled.getId()) {
                                    server = scheduled;
                                }
                            }
                            capables.clear();
                            counter = 0;
                            request = SCHD + " " + jobSpec.getJobID() + " " + scheduled.getType() + " "
                                    + scheduled.getId();
                        }

                    } else {

                        // Parsing either available or capable servers to update servers' states
                        String[] serverInfo = response.split("\\s+");
                        // System.out.print(counter);
                        for (int i = 0; i < serverInfo.length; i++) {
                            for (ServerSpec server : servers) {
                                if (server.getType().equals(serverInfo[0])
                                        && server.getId() == Integer.parseInt(serverInfo[1])) {
                                    if (serverInfo.length == 9)
                                        // Non-failure parsing
                                        if (serverInfo.length == 9) {
                                            server.setState(serverInfo[2], Integer.parseInt(serverInfo[3]),
                                                    Integer.parseInt(serverInfo[4]), Integer.parseInt(serverInfo[7]),
                                                    Integer.parseInt(serverInfo[8]));
                                        } else {
                                            // Failure parsing
                                            server.setState(serverInfo[2], Integer.parseInt(serverInfo[3]),
                                                    Integer.parseInt(serverInfo[4]), Integer.parseInt(serverInfo[7]),
                                                    Integer.parseInt(serverInfo[8]), Integer.parseInt(serverInfo[9]),
                                                    Integer.parseInt(serverInfo[10]), Integer.parseInt(serverInfo[11]),
                                                    Integer.parseInt(serverInfo[12]), Integer.parseInt(serverInfo[13]),
                                                    Integer.parseInt(serverInfo[14]));
                                        }
                                    capables.add(server);
                                    // System.out.println(server);
                                }
                            }
                        }
                        counter++;
                        if (counter < nRecs) {
                            continue;
                        }
                    }

                    send(out, request);

                    continue;
                }

                if (request.startsWith(SCHD) && response.equals(OK)) {
                    request = REDY;
                    send(out, request);
                    continue;
                }

                if ((request.equals(QUIT) && response.equals(QUIT)) || response.startsWith(ERR)) {
                    System.out.println(response);
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Close: " + e.getMessage());
                }
            }
        }
    }
}
