class Main {
    public static void main(String[] args) {
        // testing generator spread
        Request[] reqs = RequestGenerator.generateLowWeighted(100000, 1, 100);
        int[] counts = new int[4];
        for (Request req : reqs) {
            if (req.getCompletionTime() <= 25) {
                counts[0]++;
            } else if (req.getCompletionTime() <= 50) {
                counts[1]++;
            } else if (req.getCompletionTime() <= 75) {
                counts[2]++;
            } else {
                counts[3]++;
            }
        }
        for (int count : counts) {
            System.out.println(count);
        }
    }
}
