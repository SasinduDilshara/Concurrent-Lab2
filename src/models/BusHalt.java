package models;

public class BusHalt {
    public static int waiting = 0;
    public static BusHalt busHalt = createBusHalt();

    public static BusHalt createBusHalt() {
        if (busHalt == null) {
            return new BusHalt();
        }
        return busHalt;
    }

    public static int getWaiting() {
        return waiting;
    }

    public static void setWaiting(int waiting) {
        BusHalt.waiting = waiting;
    }

    public void increaseWaitingCount() {
        setWaiting(++waiting);
    }

    public void decreaseWaitingCount() {
        setWaiting(--waiting);
    }
}
