package parser;

public class JobSpec {
    //0: submitted, 1:waiting, 2: running, 3: suspended, 4: completed, 5: pre-empted, 6: failed, 7: killed
    private String state;
    private int submitTime;
    private int jobID;
    private int estRuntime;
    private int core;
    private int memory;
    private int disk;

    public JobSpec() {

    }

    public JobSpec(int submitTime, int jobID, int estRuntime, int core, int memory, int disk) {
        this.submitTime = submitTime;
        this.jobID = jobID;
        this.estRuntime = estRuntime;
        this.core = core;
        this.memory = memory;
        this.disk = disk;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    public int getSubmitTime() {
        return this.submitTime;
    }

    public int getJobID() {
        return this.jobID;
    }

    public int getEstRuntime() {
        return this.estRuntime;
    }

    public int getCore() {
        return this.core;
    }

    public int getMemory() {
        return this.memory;
    }

    public int getDisk() {
        return this.disk;
    }
}
