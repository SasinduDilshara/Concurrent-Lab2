package models;

import com.sun.tools.javac.Main;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Bus implements Runnable {
    public String id;
    public Semaphore mutex;
    public Semaphore bus;
    public Semaphore boarded;
    public final String departMessage = "Departed";
    public final int MAXIMUM_RIDERS = 20;
    private int numberOfSelectedRiders = 0;
    private BusHalt busHalt;

    public Bus(String id, Semaphore mutex, Semaphore bus, Semaphore boarded, BusHalt busHalt) {
        this.id = id;
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.busHalt = busHalt;
    }

    public Bus(Semaphore mutex, Semaphore bus, Semaphore boarded, BusHalt busHalt) {
        this.id = UUID.randomUUID().toString();
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.busHalt = busHalt;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    public Semaphore getBus() {
        return bus;
    }

    public void setBus(Semaphore bus) {
        this.bus = bus;
    }

    public Semaphore getBoarded() {
        return boarded;
    }

    public void setBoarded(Semaphore boarded) {
        this.boarded = boarded;
    }

    public String depart() {
        return this.toString() + " is " + departMessage;
    }

    @Override
    public void run() {
        while (true) {
//            System.out.println("Bus executed \n");
            try {
                Thread.sleep(150);
                mutex.acquire();
            } catch (InterruptedException e) {
                System.out.println("Mutex in the " + this.toString() + "is got interrupted");
            }
            numberOfSelectedRiders = Math.min(busHalt.getWaiting(), MAXIMUM_RIDERS);
            for (int i = 0; i < numberOfSelectedRiders; i++) {
                bus.release();
                try {
                    boarded.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Boarded in the " + this.toString() + "is got interrupted");
                }
            }
//            numberOfSelectedRiders = Math.max(busHalt.getWaiting() - MAXIMUM_RIDERS, 0);
            busHalt.setWaiting(Math.max(busHalt.getWaiting() - MAXIMUM_RIDERS, 0), true);
            mutex.release();
            depart();
        }
    }
}
