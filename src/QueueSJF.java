import java.util.Comparator;

class QueueSJF extends QueueCPU{
    @Override
    Request poll() {
        queue.sort(Comparator.comparingInt(Request::getCompletionTime));
        return super.poll();
    }

    void sort() {
        queue.sort(Comparator.comparingInt(Request::getCompletionTime));
    }
}