package parser;

import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Parser {
    public static JobSpec parseJob(String[] job) {
        JobSpec spec = null;
        try {
            spec = new JobSpec(Integer.parseInt(job[1]), Integer.parseInt(job[2]), Integer.parseInt(job[3]),
                    Integer.parseInt(job[4]), Integer.parseInt(job[5]), Integer.parseInt(job[6]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spec;
    }

    public static List<ServerSpec> parseServer() {
        List<ServerSpec> result = new ArrayList<ServerSpec>();
        try {
            File dsSystem = new File("ds-system.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(dsSystem);
            document.getDocumentElement().normalize();
            NodeList servers = document.getElementsByTagName("server");
            for (int i = 0; i < servers.getLength(); i++) {
                Node node = servers.item(i);
                Element spec = (Element) node;
                String type = spec.getAttribute("type");
                int limit = Integer.parseInt(spec.getAttribute("limit"));
                int bootupTime = Integer.parseInt(spec.getAttribute("bootupTime"));
                float hourlyRate = Float.parseFloat(spec.getAttribute("hourlyRate"));
                int coreCount = Integer.parseInt(spec.getAttribute("coreCount"));
                int memory = Integer.parseInt(spec.getAttribute("memory"));
                int disk = Integer.parseInt(spec.getAttribute("disk"));
                if (limit > 1) {
                    for (int j = 0; j < limit; j++) {
                        ServerSpec specification = new ServerSpec(j, type, limit, bootupTime, hourlyRate, coreCount,
                                memory, disk);
                        result.add(specification);
                    }
                } else {
                    ServerSpec specification = new ServerSpec(0, type, limit, bootupTime, hourlyRate, coreCount, memory,
                            disk);
                    result.add(specification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
