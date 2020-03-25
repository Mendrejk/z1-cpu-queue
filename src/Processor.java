import java.util.ArrayList;

public class Processor {
    static void FCFS(ArrayList<Request> incomingRequests) {
        QueueCPU queue = new QueueCPU();
        final int HOW_MANY_REQUESTS = incomingRequests.size();
        int elapsedTime = 0;
        int totalTimeInQueue = 0;
        int longestTimeInQueue = Integer.MIN_VALUE;
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
                // queue not empty -> processing requests
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
        System.out.println("FCFS: Processor ticks: " + elapsedTime + "\n Average time in queue: " +
                totalTimeInQueue / HOW_MANY_REQUESTS + "\n Longest time in queue: " + longestTimeInQueue);
    }

    static void SJF(ArrayList<Request> incomingRequests) {
        QueueSJF queue = new QueueSJF();
        final int HOW_MANY_REQUESTS = incomingRequests.size();
        int elapsedTime = 0;
        int totalTimeInQueue = 0;
        int longestTimeInQueue = Integer.MIN_VALUE;

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
                // queue not empty -> processing requests
                int timeToFirstEvent = Integer.MAX_VALUE;
                if (!incomingRequests.isEmpty()) {
                    addPendingRequests(queue, incomingRequests, elapsedTime);

                    // expropriation
                    if (!queue.isEmpty() && queue.peek().getTimeLeft() < current.getTimeLeft()) {
                        queue.add(current);
                        current = queue.poll();
                    }

                    if (!incomingRequests.isEmpty()) {
                        timeToFirstEvent = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                    }
                }
                if (current.getTimeLeft() < timeToFirstEvent) {
                    timeToFirstEvent = current.getTimeLeft();
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
        System.out.println("SJF: Processor ticks: " + elapsedTime + "\n Average time in queue: " +
                totalTimeInQueue / HOW_MANY_REQUESTS + "\n Longest time in queue: " + longestTimeInQueue);
    }

    private static void addPendingRequests(QueueCPU queue, ArrayList<Request> incomingRequests, int elapsedTime) {
        if (incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
            queue.add(incomingRequests.get(0));
            incomingRequests.remove(0);
        }
    }
}
