import java.util.Comparator;

public class QueueSJF extends QueueCPU{
    @Override
    Request poll() {
        queue.sort(Comparator.comparingInt(Request::getCompletionTime));
        return super.poll();
    }
}