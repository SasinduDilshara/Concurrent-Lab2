import models.Bus;
import models.BusHalt;
import models.Rider;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BusHalt bushalt = BusHalt.createBusHalt();

        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore boarded = new Semaphore(0);

        Thread busThread = new Thread(new Bus(mutex, bus, boarded, bushalt));
        Thread riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));

        riderThread.setPriority(10);
        busThread.setPriority(1);

        busThread.start();
        riderThread.start();

        busThread.join();
        riderThread.join();

        System.out.println(BusHalt.getWaiting());
        System.out.println(BusHalt.getWaiting());
        System.out.println(BusHalt.getWaiting());
        System.out.println(BusHalt.getWaiting());
        System.out.println(BusHalt.getWaiting());

//        while(true) {
//            System.out.println(BusHalt.getWaiting());
//        }
    }
}
