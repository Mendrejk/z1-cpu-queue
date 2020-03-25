import java.util.ArrayList;

class QueueCPU {
    protected ArrayList<Request> queue;

    QueueCPU() {
        queue = new ArrayList<>();
    }

    void add(Request request) {
        queue.add(request);
    }

    Request poll() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    Request peek() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.get(0);
    }

    boolean isEmpty() {
        return queue.isEmpty();
    }

    void tickAll(int time) {
        for (Request request : queue) {
            request.tick(time);
        }
    }
}
