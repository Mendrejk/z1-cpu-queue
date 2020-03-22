import java.util.Random;

class RequestGenerator {
    public static Request[] generateLowWeighted(int howMany, int minCompletionTime, int maxCompletionTime) {
        Request[] requests = new Request[howMany];
        for (int i = 0; i < requests.length; i++) {
            Random generator = new Random();
            double weight = generator.nextDouble();
            int range = maxCompletionTime - minCompletionTime;
            if (weight <= 2.0/3.0) {
                requests[i] = new Request(generator.nextInt(range / 4 + 1),0);
            } else if (weight <= 8.0/9.0) {
                requests[i] = new Request(generator.nextInt(range / 4 + 1) + range / 4,0);
            } else if (weight <= 26.0/27.0) {
                requests[i] = new Request(generator.nextInt(range / 4 + 1) + range / 2,0);
            } else {
                requests[i] = new Request(generator.nextInt(range / 4 + 1) + 3 * range / 4,0);
            }
        }
        return requests;
    }
}
