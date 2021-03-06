import java.util.ArrayList;

class Processor {
    // the same code with more or less variation repeats itself thrice here, as I could not come up with
    // any better solution for this - trying to isolate and wrap identical parts of code into methods leads to a lot
    // of function arguments (5+). Is this the way to go still?
    static int[] FCFS(ArrayList<Request> incomingRequests) {
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
                            // queue is empty, so time has to be fast-forwarded to next request
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
        return new int[] { elapsedTime, totalTimeInQueue / HOW_MANY_REQUESTS, longestTimeInQueue };
    }

    static int[] SJF(ArrayList<Request> incomingRequests) {
        QueueSJF queue = new QueueSJF();
        final int HOW_MANY_REQUESTS = incomingRequests.size();
        int elapsedTime = 0;
        int totalTimeInQueue = 0;
        int longestTimeInQueue = Integer.MIN_VALUE;
        int expropriationCount = 0;
        Request current = null;

        while(true) {
            if (current == null) {
                if (queue.isEmpty()) {
                    if (incomingRequests.isEmpty()) {
                        break;
                    } else {
                        addPendingRequests(queue, incomingRequests, elapsedTime);
                        if (queue.isEmpty()) {
                            // queue is empty, so time has to be fast-forwarded to next request
                            elapsedTime += incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                        }
                    }
                } else {
                    current = queue.poll();
                }
            } else {
                // queue not empty -> processing requests
                if (!incomingRequests.isEmpty()) {
                    addPendingRequests(queue, incomingRequests, elapsedTime);
                }
                // expropriation
                if (!queue.isEmpty()) {
                    queue.sort();
                    if (queue.peek().getTimeLeft() < current.getTimeLeft()) {
                        queue.add(current);
                        current = queue.poll();
                        expropriationCount++;
                    }
                }

                int timeToFirstEvent = current.getTimeLeft();

                if (!incomingRequests.isEmpty() && timeToFirstEvent > incomingRequests.get(0).getAppearanceTime() - elapsedTime) {
                    timeToFirstEvent = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
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
        return new int[] { elapsedTime, totalTimeInQueue / HOW_MANY_REQUESTS, longestTimeInQueue, expropriationCount };
    }

    static int[] RR(ArrayList<Request> incomingRequests, int serviceTime) {
        QueueCPU queue = new QueueCPU();
        final int HOW_MANY_REQUESTS = incomingRequests.size();
        int elapsedTime = 0;
        int totalTimeInQueue = 0;
        int longestTimeInQueue = Integer.MIN_VALUE;
        int switchCount = 0;
        Request current = null;
        int timeToNextRequest = 0;
        boolean unfinishedHandle = false;
        int currentServiceTime = 0;

        while(true) {
            if (queue.isEmpty()) {
                if (incomingRequests.isEmpty()) {
                    break;
                } else {
                    addPendingRequests(queue, incomingRequests, elapsedTime);
                    if (!incomingRequests.isEmpty()) {
                        timeToNextRequest = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                    }
                    if (queue.isEmpty()) {
                        // queue is empty, so time has to be fast-forwarded to next request
                        elapsedTime += incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                    }
                }
            } else {
                // queue not empty -> processing requests
                while (!queue.isEmpty() || current != null) {
                    if (!unfinishedHandle) {
                        currentServiceTime = serviceTime;
                    } else {
                        currentServiceTime = serviceTime - currentServiceTime;
                        unfinishedHandle = false;
                    }
                    if (!incomingRequests.isEmpty() && timeToNextRequest == 0) {
                        addPendingRequests(queue, incomingRequests, elapsedTime);
                        if (!incomingRequests.isEmpty()) {
                            timeToNextRequest = incomingRequests.get(0).getAppearanceTime() - elapsedTime;
                        } else {
                            timeToNextRequest = Integer.MAX_VALUE;
                        }
                    }
                    if (current == null) {
                        current = queue.poll();
                    }
                    if (current.getTimeLeft() < currentServiceTime) {
                        currentServiceTime = current.getTimeLeft();
                    }
                    if (timeToNextRequest < currentServiceTime && !incomingRequests.isEmpty()) {
                        currentServiceTime = timeToNextRequest;
                        unfinishedHandle = true;
                    }
                    if (current.handle(currentServiceTime)) {
                        totalTimeInQueue += current.getTimeInQueue();
                        if (current.getTimeInQueue() > longestTimeInQueue) {
                            longestTimeInQueue = current.getTimeInQueue();
                        }
                        current = null;
                    }
                    elapsedTime += currentServiceTime;
                    timeToNextRequest -= currentServiceTime;
                    if (!queue.isEmpty() && !unfinishedHandle) {
                        queue.tickAll(currentServiceTime);
                        // round-robin
                        if (current != null) {
                            queue.add(current);
                        }
                        current = queue.poll();
                        switchCount++;
                    }
                }
            }
        }
        return new int[] { elapsedTime, totalTimeInQueue / HOW_MANY_REQUESTS, longestTimeInQueue, switchCount };
    }

    private static void addPendingRequests(QueueCPU queue, ArrayList<Request> incomingRequests, int elapsedTime) {
        while (!incomingRequests.isEmpty() && incomingRequests.get(0).getAppearanceTime() <= elapsedTime) {
            queue.add(incomingRequests.get(0));
            incomingRequests.remove(0);
        }
    }
}
