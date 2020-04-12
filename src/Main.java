import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Main {
    private static final int HOW_MANY_REQUESTS = 100000;
    private static final int SERVICE_TIME = 10;

    public static void main(String[] args) {
        // generate requests
        Request[][] requestsDataSet = Effects.chooseDataSet(HOW_MANY_REQUESTS, 1, 100);

        // it's ugly af but I was desperate to get it working
        // 0's for FCFS, 1's for SJF and 2's for RR
        int[] totalTimesAll = new int[3];
        int[] averageTimesAll = new int[3];
        int[] longestTimesAll = new int[3];
        int expropriationsSjfSum = 0;
        int switchesRrSum = 0;

        for (Request[] requests : requestsDataSet) {
            ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(requests));
            incomingRequests.sort(Comparator.comparingInt(Request::getAppearanceTime));

            int[] resultsFCFS = Processor.FCFS(deepCopyRequests(incomingRequests));
            int[] resultsSJF = Processor.SJF(deepCopyRequests(incomingRequests));
            int[] resultsRR = Processor.RR(deepCopyRequests(incomingRequests), SERVICE_TIME);

            int [] totalTimes = new int[] { resultsFCFS[0], resultsSJF[0], resultsRR[0] };
            int [] averageTimes = new int[] { resultsFCFS[1], resultsSJF[1], resultsRR[1] };
            int[] longestTimes = new int[] { resultsFCFS[2], resultsSJF[2], resultsRR[2] };
            int expropriationsSJF = resultsSJF[3];
            int switchesRR = resultsRR[3];

            for (int i = 0; i < 3; i++) {
                totalTimesAll[i] += totalTimes[i];
                averageTimesAll[i] += averageTimes[i];
                if (longestTimes[i] > longestTimesAll[i]) {
                    longestTimesAll[i] = longestTimes[i];
                }
            }
            expropriationsSjfSum += expropriationsSJF;
            switchesRrSum += switchesRR;
        }

        for (int i = 0; i < 3; i++) {
            averageTimesAll[i] /= requestsDataSet.length;
        }

        System.out.println("FCFS: totalTime = " + totalTimesAll[0] + ", averageTime = " + averageTimesAll[0] +
                ", longestTime = " + longestTimesAll[0] + ".");
        System.out.println("SJF: totalTime = " + totalTimesAll[1] + ", averageTime = " + averageTimesAll[1] +
                ", longestTime = " + longestTimesAll[1] + ", expropriations = " + expropriationsSjfSum + ".");
        System.out.println("RR: totalTime = " + totalTimesAll[2] + ", averageTime = " + averageTimesAll[2] +
                ", longestTime = " + longestTimesAll[2] + ", switches = " + switchesRrSum + ".");
    }

    private static ArrayList<Request> deepCopyRequests(ArrayList<Request> requests) {
        ArrayList<Request> copy = new ArrayList<>();
        for (Request request : requests) {
            copy.add(request.copy());
        }
        return copy;
    }


}

