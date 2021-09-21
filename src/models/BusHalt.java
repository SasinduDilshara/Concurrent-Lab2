package models;

public class BusHalt {
    public int waiting = 0;
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

    public void setWaiting(int waiting, boolean flag) {
        this.waiting = waiting;
        if (flag == true) {
            show("Bus");
        }
    }

    public void increaseWaitingCount() {
        setWaiting(++waiting, false);
        show("Rider");
    }

    public void decreaseWaitingCount() {
        setWaiting(--waiting, false);
    }

    public void show(String type) {
        System.out.println(type + " - Number of Riders:- " + this.getWaiting());
    }
}
