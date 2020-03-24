import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Main {
    private static final int HOW_MANY_REQUESTS = 100000;
    public static void main(String[] args) {
        int elapsedTime = 0;
        // generate requests
        Request[] requests = RequestGenerator.generateLowWeighted(HOW_MANY_REQUESTS,1,100);
        ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(requests));
        incomingRequests.sort(Comparator.comparingInt(Request::getAppearanceTime));

        QueueFCFS queue = new QueueFCFS();
        int totalTimeInQueue = 0;
        int longestTimeInQueue = Integer.MIN_VALUE;

        // main program loop - FCFS
        Request current = null;

        while(true) {
             if (current == null) {
                 if (queue.isEmpty()) {
                     if (incomingRequests.isEmpty()) {
                         break;
                     } else {
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
                     totalTimeInQueue += current.getTimeInQueue();
                     if (current.getTimeInQueue() > longestTimeInQueue) {
                         longestTimeInQueue = current.getTimeInQueue();
                     }
                     current = null;
                 }
                 elapsedTime += timeToFirstEvent;
                 queue.tickAll(timeToFirstEvent);
             }
        }
        System.out.println("Processor ticks: " + elapsedTime + "\n Average time in queue: " +
                totalTimeInQueue / HOW_MANY_REQUESTS + "\n Longest time in queue: " + longestTimeInQueue);
    }

    private static void addPendingRequests(QueueFCFS queue, ArrayList<Request> incomingRequests, int elapsedTime) {
        if (incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
            queue.add(incomingRequests.get(0));
            incomingRequests.remove(0);
        }
    }
}

