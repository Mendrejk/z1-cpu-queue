import java.util.Random;

class RequestGenerator {
    // very simple weighted distribution - 2/3 of results lay in the first quarter of length
    public static Request[] generateLowWeighted(int howMany, int minCompletionTime, int maxCompletionTime) {
        Request[] requests = new Request[howMany];

        for (int i = 0; i < requests.length; i++) {
            final Random generator = new Random();
            final int range = maxCompletionTime - minCompletionTime;

            int completionTime = generateCompletionTime(generator, range);
            int appearanceTime = 0;
            if (i != 0) {
                appearanceTime = generateAppearanceTime(generator, requests[i-1]);
            }
            requests[i] = new Request(completionTime, appearanceTime);
        }
        return requests;
    }

    private static int generateCompletionTime(Random generator, int range) {
        final double completionWeight = generator.nextDouble();
        int completionTime = 0;

        if (completionWeight <= 2.0/3.0) {
            completionTime = generator.nextInt(range / 4 + 1);
        } else if (completionWeight <= 8.0/9.0) {
            completionTime = generator.nextInt(range / 4 + 1) + range / 4;
        } else if (completionWeight <= 26.0/27.0) {
            completionTime = generator.nextInt(range / 4 + 1) + range / 2;
        } else {
            completionTime = generator.nextInt(range / 4 + 1) + 3 * range / 4;
        }
        return completionTime;
    }

    private static int generateAppearanceTime(Random generator, Request previous) {
        final double appearanceWeight = generator.nextDouble();
        int appearanceTime = 0;

        if (appearanceWeight < 0.05) {
            appearanceTime = previous.getAppearanceTime();
        } else if (appearanceWeight < 0.65) {
            appearanceTime = generator.nextInt(previous.getCompletionTime()) + previous.getAppearanceTime() + 1;
        } else {
            appearanceTime = generator.nextInt(100) + previous.getAppearanceTime() + previous.getCompletionTime() + 1;
        }
        return appearanceTime;
    }
}
