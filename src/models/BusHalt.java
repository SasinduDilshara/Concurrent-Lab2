package models;

public class BusHalt {
    private int waiting = 0;
    public static BusHalt busHalt = createBusHalt();

    public static BusHalt createBusHalt() {
        if (busHalt == null) {
            busHalt = new BusHalt();
        }
        return busHalt;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int remaining) {
        System.out.println("=============================================================================");
        System.out.println("* Bus Arrived at the Halt.");
        System.out.println("* Number of Riders went aboard : " + Integer.toString(this.waiting - remaining));
        System.out.println("* Number of Remaining riders   : " + Integer.toString(remaining));
        this.waiting = remaining;
    }

    public void increaseWaitingCount() {
        this.waiting += 1;
        show("Rider");
    }

    public void decreaseWaitingCount() {
        setWaiting(--waiting);
    }

    public void show(String type) {
        System.out.println("=============================================================================");
        System.out.println("* New Rider Arrived at the halt.");
        System.out.println("* Number of Riders at the halt : " + +this.getWaiting());
    }
}
