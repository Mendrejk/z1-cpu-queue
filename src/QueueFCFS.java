import java.util.ArrayDeque;
import java.util.Arrays;

public class QueueFCFS {
    ArrayDeque<Request> queue;

    QueueFCFS() {
        queue = new ArrayDeque<Request>();
    }

    void add(Request request) {
        queue.add(request);
    }

    Request poll() {
        return queue.poll();
    }

    boolean isEmpty() {
        return queue.isEmpty();
    }
}
