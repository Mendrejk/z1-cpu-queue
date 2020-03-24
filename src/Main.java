import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Main {
    public static void main(String[] args) {
        int elapsedTime = 0;
        // generate requests
        Request[] requests = RequestGenerator.generateLowWeighted(100000,1,100);
        ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(requests));
        incomingRequests.sort(Comparator.comparingInt(Request::getAppearanceTime));
        QueueFCFS queue = new QueueFCFS();

        // main program loop - FCFS
        Request current = null;

        while(true) {
             if (current == null) {
                 if (queue.isEmpty()) {
                     if (incomingRequests.isEmpty()) {
                         break;
                     } else {
                         // WRITE FUNCTION TO INCREASE TIME IN QUEUE
                         addPendingRequests(queue, incomingRequests, elapsedTime);
                         if (queue.isEmpty()) {
                             elapsedTime += incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                         }
                     }
                 } else {
                     current = queue.poll();
                 }
             } else {
                 int timeToFirstEvent = current.getTimeLeft();
                 if (!incomingRequests.isEmpty()) {
                     addPendingRequests(queue, incomingRequests, elapsedTime);
                     if (!incomingRequests.isEmpty() && (incomingRequests.get(0).getAppearanceTime() - elapsedTime) < timeToFirstEvent) {
                         timeToFirstEvent = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                     }
                 }
                 if (current.handle(timeToFirstEvent)) {
                     current = null;
                 }
                 elapsedTime += timeToFirstEvent;
             }
        }
        System.out.println(elapsedTime);
    }

    private static void addPendingRequests(QueueFCFS queue, ArrayList<Request> incomingRequests, int elapsedTime) {
        if (incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
            queue.add(incomingRequests.get(0));
            incomingRequests.remove(0);
        }
    }
}
