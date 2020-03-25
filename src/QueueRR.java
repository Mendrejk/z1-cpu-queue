import java.util.Iterator;

public class QueueRR extends QueueCPU implements  Iterable<Request>{
    @Override
    public Iterator<Request> iterator() {
        return queue.iterator();
    }

    public int size() {
        return queue.size();
    }

    public Request get(int i) {
        return queue.get(i);
    }
}
