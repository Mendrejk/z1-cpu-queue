import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Main {
    public static void main(String[] args) {
        int elapsedTime = 0;
        // generate requests
        Request[] request = RequestGenerator.generateLowWeighted(10000,1,100);
        ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(request));
        incomingRequests.sort(Comparator.comparingInt(Request::getAppearanceTime));
        QueueFCFS queue = new QueueFCFS();
        int counter = 0;

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
                             elapsedTime += incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                         }
                     }
                 } else {
                     current = queue.poll();
                 }
             } else {
                 if (incomingRequests.isEmpty()) {
                     elapsedTime += current.getTimeLeft();
                     current = null;
                     counter++;
                 } else {
                     // TODO chane this (this while appears twice); completely rewrite loop or make it into function at least
                     while (!incomingRequests.isEmpty() && incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
                         queue.add(incomingRequests.get(0));
                         incomingRequests.remove(0);
                     }
                     int timeToFirstEvent = current.getTimeLeft();
                     if ((incomingRequests.get(0).getAppearanceTime() - elapsedTime) < timeToFirstEvent) {
                         timeToFirstEvent = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                     }
                     if (current.handle(timeToFirstEvent)) {
                         current = null;
                         counter++;
                     }
                     elapsedTime += timeToFirstEvent;
                 }
             }
        }
        System.out.println(counter);
    }
}
