class Request {
    private final int completionTime;
    private int timeLeft;
    private int timeInQueue;

    Request(int completionTime) {
        if (completionTime <= 0) {
            completionTime = 1;
        }

        this.completionTime = completionTime;
        timeLeft = completionTime;
        timeInQueue = 0;
    }

    void tick(int time) {
        timeInQueue += time;
    }

    void handle(int time) {
        //can't subtract progress
        if (time < 0) {
            time = 0;
        }

        timeLeft -= time;
    }

    @Override
    public String toString() {
        return Integer.toString(completionTime);
    }
}