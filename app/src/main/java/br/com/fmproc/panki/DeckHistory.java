package br.com.fmproc.panki;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeckHistory implements Serializable{
    private String name;
    private Map<String, int[]> historyMap = new HashMap<String, int[]>();

    public void setName(String name){
        this.name = name;
    }

    public void addCard(String card, int[] lastOccurences){
        if (lastOccurences==null  || lastOccurences.length != 5){
            throw new RuntimeException("LastOccurrences diferente de 5");
        }
        historyMap.put(card, lastOccurences);
    }

    public int getLastThreeOccurrencesMean(String card) {
        int ret = 0;

        if (historyMap.containsKey(card)){
            ret = (historyMap.get(card)[4]+historyMap.get(card)[3]+historyMap.get(card)[2])/3;
        }

        return ret;
    }

    public boolean isLastThreeZero(String card){
        boolean ret = true;

        if (historyMap.containsKey(card)){
            int[] lastOccurrences = historyMap.get(card);
            ret = lastOccurrences[4]==0 && lastOccurrences[3]==0 && lastOccurrences[2]==0;
        }

        return ret;
    }

    public boolean isLastFourZero(String card){
        boolean ret = true;

        if (historyMap.containsKey(card)){
            int[] lastOccurrences = historyMap.get(card);
            ret = lastOccurrences[4]==0 && lastOccurrences[3]==0 && lastOccurrences[2]==0 && lastOccurrences[1]==0;
        }

        return ret;
    }

    public boolean isLastFiveZero(String card){
        boolean ret = true;

        if (historyMap.containsKey(card)){
            int[] lastOccurrences = historyMap.get(card);
            ret = lastOccurrences[4]==0 && lastOccurrences[3]==0 && lastOccurrences[2]==0 && lastOccurrences[1]==0 && lastOccurrences[0]==0;
        }

        return ret;
    }

    public void addLastOccurrence(String card, int occurrence){
        if (!historyMap.containsKey(card)){
            historyMap.put(card, new int[]{1,1,1,1,1});
        }

        int[] lastOccurrences = historyMap.get(card);
        lastOccurrences[0] = lastOccurrences[1];
        lastOccurrences[1] = lastOccurrences[2];
        lastOccurrences[2] = lastOccurrences[3];
        lastOccurrences[3] = lastOccurrences[4];
        lastOccurrences[4] = occurrence;
    }

    public Map<String, int[]> getFullHistory(){
        return historyMap;
    }

    public void persist(){
        try {
            PrintWriter pw = new PrintWriter(new File(name), "UTF-8");
            for (String card : historyMap.keySet()){
                pw.write(card+";"+historyMap.get(card)[0]+";"+historyMap.get(card)[1]+";"+historyMap.get(card)[2]+";"+historyMap.get(card)[3]+";"+historyMap.get(card)[4]+"\r\n");
            }
            pw.flush();
            pw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}