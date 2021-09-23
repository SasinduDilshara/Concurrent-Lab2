package models;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Rider implements Runnable {
    public String id;
    public Semaphore mutex;
    public Semaphore bus;
    public Semaphore boarded;
    public final String boardMessage = "Boarded";
    private BusHalt busHalt;

    public Rider(String id, Semaphore mutex, Semaphore bus, Semaphore boarded, BusHalt busHalt) {
        this.id = id;
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.busHalt = busHalt;
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id='" + id + '\'' +
                '}';
    }

    public Rider(Semaphore mutex, Semaphore bus, Semaphore boarded, BusHalt busHalt) {
        this.id = UUID.randomUUID().toString();
        this.mutex = mutex;
        this.bus = bus;
        this.boarded = boarded;
        this.busHalt = busHalt;
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

    public String board() {
        return this.toString() + " is " + boardMessage;
    }

    @Override
    public void run() {
            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                System.out.println("Mutex in the " + this.toString() + "is got interrupted");
            }
            busHalt.increaseWaitingCount();
            mutex.release();

            try {
                bus.acquire();
            } catch (InterruptedException e) {
                System.out.println("Bus in the " + this.toString() + "is got interrupted");
            }
            board();
            boarded.release();
    }
}
