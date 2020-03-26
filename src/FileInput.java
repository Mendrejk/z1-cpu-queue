import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileInput {
    private static Request[] RequestsFromFile(String filepath) {
        ArrayList<Request> requests = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(filepath));
            while (scan.hasNext()) {
                int[] data = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                requests.add(new Request(data[0], data[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return requests.toArray(new Request[0]);
    }

    static Request[] chooseDataSet(int HOW_MANY_REQUESTS, int minCompletionTime, int maxCompletionTime) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose dataSet: 0 - random, 1 - FCFS worst, 2 - SJF worst, 3 - RR worst.");
        int choice = Integer.parseInt(scan.nextLine());
        Request[] requests = new Request[0];
        switch (choice) {
            case 1:
                // FCFS works terribly with this dataset as it has to handle the huge request first and a dozen of small
                // requests need wait for it to finish, floating their timeInQueue
                requests = RequestsFromFile("src/DataSetFCFS.txt");
                break;
            case 2:
                // SJF still has the best average queue time (and it will basically always do), but expropriation leads
                // to starving of the first, large request in the dataset and thus longest time in queue is the largest
                // for this algorithm
                requests = RequestsFromFile("src/DataSetSJF.txt");
                break;
            case 3:
                // Round-Robin struggles with a dataset that fills the queue up with a lot of large processes, as it
                // will be constantly switching between them and it will take very long to finish any of them (it will
                // finish all of them in similar time though). Thus, the average time in queue is the largest here. Also,
                // the algorithm is making a very large amount of process switches (expropriations).
                requests = RequestsFromFile("src/DataSetRR.txt");
                break;
            default:
                System.out.println("How many requests to generate? (enter for default (100000); note that more than " +
                        "100000 requests will take a long time to process!");
                String howMany = scan.nextLine();
                if (!howMany.equals("")) {
                    HOW_MANY_REQUESTS = Integer.parseInt(howMany);
                }
                System.out.println("What should the minimal process completion time be? enter for default (1)");
                String minimalTime = scan.nextLine();
                if (!minimalTime.equals("")) {
                    minCompletionTime = Integer.parseInt(minimalTime);
                }
                System.out.println("What should the maximal process completion time be? enter for default (100)");
                String maximalTime = scan.nextLine();
                if (!minimalTime.equals("")) {
                    maxCompletionTime = Integer.parseInt(maximalTime);
                }
                requests = RequestGenerator.generateLowWeighted(HOW_MANY_REQUESTS, minCompletionTime, maxCompletionTime);
        }
        return requests;
    }
}
