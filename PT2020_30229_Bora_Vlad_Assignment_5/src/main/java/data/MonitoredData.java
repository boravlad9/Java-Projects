package data;

public class MonitoredData {

    private String startTime, endTime, activity;

    public MonitoredData(String startTime, String endTime, String activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
    }

    public String toString(){
        return  startTime + " " + endTime + " " + activity;
    }

    public String getActivity() {
        return activity;
    }

    public String getEndTimeDate() {
        return endTime.substring(0, 10);
    }

    public Integer getDay(){
        return new Integer(startTime.substring(8, 10));
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
