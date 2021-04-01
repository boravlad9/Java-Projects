package business;

import data.MonitoredData;
import presentation.Writer;

import java.math.BigInteger;
import java.nio.file.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;


public class Executor {
    private String fileName;
    private ArrayList<MonitoredData> dates;
    private HashMap<String, Integer> hashMap;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Executor(String fileName) {
        this.fileName = fileName;
    }

    public void execute(){
        read();
        countDays();
        hashMap = countAppearance();
        countActivityAppearance();
        activity();
        filter();
    }

    public void read() {
        dates = new ArrayList<>();
        String filename = fileName;
        try(Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(current -> dates.add(new MonitoredData(current.substring(0, 19), current.substring(21, 40), current.substring(42))));
        } catch (Exception e) {
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (MonitoredData data : dates) {
            stringBuilder.append( data.toString());
            stringBuilder.append("\n");
        }
        Writer.write("Task_1.txt", stringBuilder.toString());
    }

    public void countDays(){
        ArrayList<String> dateList = new ArrayList<String>();
        dates.forEach(current -> dateList.add(current.getEndTimeDate()));
        Set<String> uniqueDates = new HashSet<String>(dateList);
        Writer.write("Task_2.txt", Integer.toString(uniqueDates.size()));
    }

    public HashMap<String, Integer> countAppearance(){
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        ArrayList<String> activities = new ArrayList<String>();
        dates.forEach(current -> activities.add(current.getActivity()));
        activities.stream().distinct().forEach(current ->  hashMap.put(current, (int)dates.stream().filter(
                var-> var.getActivity().equals(current)).count()));
        StringBuilder stringBuilder = new StringBuilder();
        activities.stream().distinct().forEach(current -> stringBuilder.append(current +" "+ hashMap.get(current) + "\n"));
        Writer.write("Task_3.txt", stringBuilder.toString());
        return hashMap;
    }

    public Map<Integer, Map<String, Integer>> countActivityAppearance(){
        Map<Integer, Map<String, Integer>> map = new HashMap<> ();
        List<Integer> allDays = new ArrayList<>();
        dates.forEach(current -> allDays.add(current.getDay()));
        List<Integer> distinctDays = allDays.stream().distinct().collect(Collectors.toList());
        for (Integer currentDay : distinctDays){
            HashMap<String, Integer> newMap = new HashMap<>();
            ArrayList<String> activities = new ArrayList<>();
            dates.forEach(current -> activities.add(current.getActivity()));
            activities.stream().distinct().forEach(current ->  newMap.put(current, (int)dates.stream().filter(
                    var-> var.getActivity().equals(current) && var.getDay().equals(currentDay)).count()));
            map.put(currentDay, newMap);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Integer, Map<String, Integer>> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("\n");
        }
        Writer.write("Task_4.txt", stringBuilder.toString());
        return map;
    }

    public HashMap<String, BigInteger> activity() {
        List<String> activities = new ArrayList<>();
        List<String> activityList;
        dates.forEach(current -> activities.add(current.getActivity()));
        activityList = activities.stream().distinct().collect(Collectors.toList());
        HashMap<String, BigInteger> activitiesTotalDuration = new HashMap<>();

        for(String current : activityList) {
            activitiesTotalDuration.put(current, new BigInteger("0"));
        }
        activityList.forEach(
                current -> dates.stream()
                        .filter(date -> date.getActivity().equals(current)).forEach(p -> {
                            try {
                                activitiesTotalDuration.put(current,
                                        activitiesTotalDuration.get(current).add( new BigInteger(
                                                Long.valueOf((format.parse(p.getEndTime()).getTime() -
                                                format.parse(p.getStartTime()).getTime())/1000).toString())));
                            }
                            catch (Exception e) {
                            }
                        }));
        StringBuilder stringBuilder = new StringBuilder();
        for(String current : activityList) {
            stringBuilder.append(current + " " +
                    activitiesTotalDuration.get(current).divide(new BigInteger(Integer.valueOf(24*3600).toString())) + " " +
                    activitiesTotalDuration.get(current).divide(new BigInteger("3600")).mod(new BigInteger("24")) + ":"+
                    activitiesTotalDuration.get(current).divide(new BigInteger("60")).mod(new BigInteger("60")) + ":" +
                    activitiesTotalDuration.get(current).mod(new BigInteger("60")) + "\n");
        }
        Writer.write("Task_5.txt", stringBuilder.toString());
        return activitiesTotalDuration;
    }

    private void filter() {
        List<String> activities = new ArrayList<>();
        HashMap<String, Integer> counter = new HashMap<>();
        List<MonitoredData> filterList;
        List<String> activityList;
        dates.forEach(current -> activities.add(current.getActivity()));
        activityList = activities.stream().distinct().collect(Collectors.toList());

        filterList = dates.stream().filter(currentDate -> {
                    try {
                        return (format.parse(currentDate.getEndTime()).getTime() -
                                format.parse(currentDate.getStartTime()).getTime())/1000/60 % 60 < 5;
                    }
                    catch (ParseException e) {
                    }
                    return false;
                }).collect(Collectors.toList());

        activityList.forEach(current -> counter.put(current,
                (int) filterList.stream().filter(currentActivity -> currentActivity.getActivity().equals(current)).count()));
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : activityList) {
            float division = ((float)counter.get(string)) / hashMap.get(string);
            if(division > 0.9) {
                stringBuilder.append(string);
                stringBuilder.append("\n");
            }
        }
        Writer.write("Task_6.txt", stringBuilder.toString());
    }

}
