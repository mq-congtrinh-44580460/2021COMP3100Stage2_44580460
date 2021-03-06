package parser;

public class ServerSpec {
    private int id;
    //0: inactive, 1: booting, 2: idle, 3: active, 4: unavailable
    private String state;
    private int currentStartTime;
    private int waitingJobs;
    private int runningJobs;
    private String type;
    private int limit;
    private int bootupTime;
    private float hourlyRate;
    private int coreCount;
    private int memory;
    private int disk;
    private int failures;
    private int totalFailTime;
    private int mttf;
    private int mttr;
    private int madf;
    private int lastStartTime;
    private int estimatedRuntime; 
    private int currentCore;

    public ServerSpec() {

    }

    public ServerSpec(int id, String type, int limit, int bootupTime, float hourlyRate, int coreCount, int memory,
            int disk) {
        this.id = id;
        this.type = type;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.coreCount = coreCount;
        this.memory = memory;
        this.disk = disk;
        this.currentCore = coreCount;
        this.estimatedRuntime = -1;
    }

    public void setState(String state, int currentStartTime, int currentCore, int waitingJobs, int runningJobs) {
        this.state = state;
        this.currentCore = currentCore;
        this.currentStartTime = currentStartTime;
        this.waitingJobs = waitingJobs;
        this.runningJobs = runningJobs;
        // this.estimatedRuntime = estimatedRuntime; 
    }

    public void setState(String state, int currentStartTime, int currentCore, int waitingJobs, int runningJobs, int failures,
            int totalFailTime, int mttf, int mttr, int madf, int lastStartTime) {
        this.state = state;
        this.currentCore = currentCore;
        this.currentStartTime = currentStartTime;
        this.waitingJobs = waitingJobs;
        this.runningJobs = runningJobs;
        this.failures = failures;
        this.totalFailTime = totalFailTime;
        this.mttf = mttf;
        this.mttr = mttr;
        this.madf = madf;
        this.lastStartTime = lastStartTime;
        // this.estimatedRuntime = estimatedRuntime;
    }

    public void setEstimatedRuntime(int estimatedRuntime){
        this.estimatedRuntime = estimatedRuntime;
    }

    public int getEstimatedRuntime(){
        return this.estimatedRuntime;
    }

    public int getCurrentCore(){
        return this.currentCore;
    }

    public int getId() {
        return this.id;
    }

    public String getState() {
        return this.state;
    }

    public int getCurrentStartTime() {
        return this.currentStartTime;
    }

    public int getWaitingJobs() {
        return this.waitingJobs;
    }

    public int getRunningJobs() {
        return this.runningJobs;
    }

    public String getType() {
        return this.type;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getBootupTime() {
        return this.bootupTime;
    }

    public float getHourlyRate() {
        return this.hourlyRate;
    }

    public int getCoreCount() {
        return this.coreCount;
    }

    public int getMemory() {
        return this.memory;
    }

    public int getDisk() {
        return this.disk;
    }

    public int getFailures() {
        return this.failures;
    }

    public int getTotalFailTime() {
        return this.totalFailTime;
    };

    public int getMttf() {
        return this.mttf;
    };

    public int mttr() {
        return this.mttr;
    };

    public int madf() {
        return this.madf;
    };

    public int lastStartTime() {
        return this.lastStartTime;
    };
}
