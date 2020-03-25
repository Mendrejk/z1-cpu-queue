import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Main {
    private static final int HOW_MANY_REQUESTS = 100000;
    public static void main(String[] args) {
        // generate requests
        Request[] requests = RequestGenerator.generateLowWeighted(HOW_MANY_REQUESTS, 1, 100);
        ArrayList<Request> incomingRequests = new ArrayList<>(Arrays.asList(requests));
        incomingRequests.sort(Comparator.comparingInt(Request::getAppearanceTime));

        Processor.FCFS(deepCopyRequests(incomingRequests));
        Processor.SJF(deepCopyRequests(incomingRequests));
    }

    private static ArrayList<Request> deepCopyRequests(ArrayList<Request> requests) {
        ArrayList<Request> copy = new ArrayList<>();
        for (Request request : requests) {
            copy.add(request.copy());
        }
        return copy;
    }
}

