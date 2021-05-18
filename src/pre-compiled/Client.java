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
        String algo = "";
        if (args.length > 1 && args[0] == "-a"){
            algo = args[1];
        }

        try {
            s = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String request = HELO;
            send(out, request);

            int nRecs = 0;
            int counter = 0;
            JobSpec jobSpec = new JobSpec();
            ServerSpec scheduled = new ServerSpec();
            List<ServerSpec> servers = new ArrayList<ServerSpec>();
            List<ServerSpec> capables = new ArrayList<ServerSpec>();

            while (true) {
                String response = in.readLine();
                // System.out.println(response);

                if (request.equals(HELO) && response.equals(OK)) {
                    String username = System.getProperty("user.name");

                    request = AUTH + " " + username;
                    send(out, request);
                    continue;
                }

                if (request.startsWith(AUTH) && response.equals(OK)) {
                    servers = Parser.parseServer();

                    servers = Scheduler.sortAsc(servers);

                    request = REDY;
                    send(out, request);
                    continue;
                }

                if (request.equals(REDY)) {
                    if (response.equals(NONE)) {
                        request = QUIT;
                    } else {
                        String[] job = response.split("\\s+");

                        if (job[0].equals(JOBN)) {
                            jobSpec = Parser.parseJob(job);
                            
                            if (algo == "wf" || algo == "bf"){
                                request = GETS + " Capable " + job[4] + " " + job[5] + " " + job[6];
                            }
                            else if (algo == "ff"){
                                request = GETS + " Avail " + job[4] + " " + job[5] + " " + job[6];
                            }
                        }
                    }
                    send(out, request);
                    continue;
                }

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
                        // if (nRecs == servers.size()) {
                        //     scheduled = Scheduler.allToLargest(servers);
                        // } else {
                        //     scheduled = Scheduler.allToLargest(capables);
                        // }
                        if (algo == "wf"){
                            scheduled = Scheduler.worstFit(capables, jobSpec.getCore());
                        }
                        else if (algo == "bf"){
                            // scheduled = Scheduler
                        } 
                        else if (algo == "ff"){
                            
                        }
                        scheduled = Scheduler.toCapabSpec(capables);
                        capables.clear();
                        counter = 0;

                        request = SCHD + " " + jobSpec.getJobID() + " " + scheduled.getType() + " " + scheduled.getId();
                    } else {
                        String[] serverInfo = response.split("\\s+");

                        for (ServerSpec server : servers) {
                            if (server.getType().equals(serverInfo[0])
                                    && server.getId() == Integer.parseInt(serverInfo[1])) {
                                if (serverInfo.length == 9) {
                                    server.setState(serverInfo[2], Integer.parseInt(serverInfo[3]),
                                            Integer.parseInt(serverInfo[7]), Integer.parseInt(serverInfo[8]));
                                } else {
                                    server.setState(serverInfo[2], Integer.parseInt(serverInfo[3]),
                                            Integer.parseInt(serverInfo[7]), Integer.parseInt(serverInfo[8]),
                                            Integer.parseInt(serverInfo[9]), Integer.parseInt(serverInfo[10]),
                                            Integer.parseInt(serverInfo[11]), Integer.parseInt(serverInfo[12]),
                                            Integer.parseInt(serverInfo[13]), Integer.parseInt(serverInfo[14]));
                                }
                                capables.add(server);
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
