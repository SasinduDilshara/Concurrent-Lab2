import models.Bus;
import models.BusHalt;
import models.Rider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import static models.BusHalt.busHalt;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int MAX_PASSENGER = 1000;
        BusHalt bushalt = BusHalt.createBusHalt();

        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore boarded = new Semaphore(0);

        Thread busThread = new Thread(new Bus(mutex, bus, boarded, bushalt));
//        Thread riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));

//        riderThread.setPriority(10);
        busThread.setPriority(1);

        busThread.start();

        List<Thread> riders = new ArrayList<>();
        Thread riderThread;
        Random rand = new Random();

        for (int i = 0; i < MAX_PASSENGER; i++) {
            Thread.sleep(Math.abs(rand.nextInt()) % 10);
            riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));
            riderThread.start();
            riders.add(riderThread);
            System.out.println(busHalt.getWaiting());
        }

        busThread.join();
        for (int i =0; i < MAX_PASSENGER; i++) {
            riders.get(i).join();
        }

        busThread.join();
//        riderThread.join();;
    }
}

//import models.Bus;
//import models.BusHalt;
//import models.Rider;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.Semaphore;
//
//import static models.BusHalt.busHalt;
//
//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        int MAX_PASSENGER = 1000;
//        BusHalt bushalt = BusHalt.createBusHalt();
//
//        Semaphore mutex = new Semaphore(1);
//        Semaphore bus = new Semaphore(0);
//        Semaphore boarded = new Semaphore(0);
//
////        Thread busThread = new Thread(new Bus(mutex, bus, boarded, bushalt));
////        Thread riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));
//
////        riderThread.setPriority(10);
////        busThread.setPriority(1);
//
////        busThread.start();
//
//        List<Thread> riders = new ArrayList<>();
//        Thread riderThread;
//
//        List<Thread> busses = new ArrayList<>();
//        Thread busThread;
//
//        Random rand = new Random();
//
//        for (int i = 0; i < MAX_PASSENGER; i++) {
//            riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));
//            busThread = new Thread(new Bus(mutex, bus, boarded, bushalt));
//            riderThread.start();
//            busThread.start();
//            riders.add(riderThread);
//            busses.add(busThread);
//            System.out.println(busHalt.getWaiting());
//        }
//
//        busThread.join();
//        for (int i =0; i < MAX_PASSENGER; i++) {
//            riders.get(i).join();
//        }
//
//        busThread.join();
////        riderThread.join();;
//    }
//}

