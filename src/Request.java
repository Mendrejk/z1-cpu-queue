class Request {
    private final int completionTime;
    private final int appearanceTime;
    private int timeLeft;
    private int timeInQueue;

    Request(int completionTime, int appearanceTime) {
        if (completionTime <= 0) {
            completionTime = 1;
        }

        this.completionTime = completionTime;
        this.appearanceTime = appearanceTime;
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

    int getCompletionTime() { // TODO: remove
        return completionTime;
    }

    @Override
    public String toString() {
        return Integer.toString(completionTime);
    }
}