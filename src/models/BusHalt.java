package models;

public class BusHalt {
    private int waiting = 0;
    public static BusHalt busHalt = createBusHalt();

    public static BusHalt createBusHalt() {
        if (busHalt == null) {
            busHalt =  new BusHalt();
        }
        return busHalt;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int new_waiting) {
        this.waiting = new_waiting;
        System.out.println("* Bus Arrived at the Halt " + Integer.toString(this.waiting - new_waiting));
    }

    public void increaseWaitingCount() {
        this.waiting += 1;
        show("Rider");
    }

    public void decreaseWaitingCount() {
        setWaiting(--waiting);
    }

    public void show(String type) {
        System.out.println("* Number of Riders at the Halt:- " + this.getWaiting() );
    }
}
