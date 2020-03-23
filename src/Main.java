import java.util.ArrayList;
import java.util.Arrays;

class Main {
    public static void main(String[] args) {
        int elapsedTime = 0;
        // generate requests
        Request[] request = RequestGenerator.generateLowWeighted(10000,1,100);
        ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(request));
        QueueFCFS queue = new QueueFCFS();

        // main program loop - FCFS
        Request current = null;

        while(true) {
             if (current == null) {
                 if (queue.isEmpty()) {
                     if (incomingRequests.isEmpty()) {
                         break;
                     } else {
                         // TODO SORT INCOMING REQUESTS AND CHANGE INCOMING REQS INTO QUEUE MAYBE?
                         // WRITE FUNCTION TO INCREASE TIME IN QUEUE
                         while (!incomingRequests.isEmpty() && incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
                             queue.add(incomingRequests.get(0));
                             incomingRequests.remove(0);
                         }
                         if (queue.isEmpty()) {
                             elapsedTime += elapsedTime - incomingRequests.get(0).getAppearanceTime();
                             continue;
                         }
                     }
                 } else {
                     current = queue.poll();
                 }
             } else {
                 if (incomingRequests.isEmpty()) {
                     elapsedTime += current.getCompletionTime();
                     current = null;
                 } else {
                     int timeToFirstEvent = current.getCompletionTime();
                     if ((elapsedTime - incomingRequests.get(0).getAppearanceTime()) < timeToFirstEvent) {
                         timeToFirstEvent = elapsedTime - incomingRequests.get(0).getAppearanceTime();
                     }
                 }
             }
        }
    }
}
