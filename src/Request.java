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

    boolean handle(int time) {
        //can't subtract progress
        if (time < 0) {
            time = 0;
        }
        timeLeft -= time;
        return timeLeft == 0;
    }

    int getCompletionTime() {
        return completionTime;
    }

    int getAppearanceTime() {
        return appearanceTime;
    }

    int getTimeLeft() {
        return timeLeft;
    }

    int getTimeInQueue() {
        return timeInQueue;
    }

    @Override
    public String toString() {
        return "completionTime: " + completionTime + ", appearanceTime: " + appearanceTime + ", timeLeft: " + timeLeft;
    }

    public Request copy() {
        int newCompletionTime = completionTime;
        int newAppearanceTime = appearanceTime;
        return new Request(newCompletionTime, newAppearanceTime);
    }
}