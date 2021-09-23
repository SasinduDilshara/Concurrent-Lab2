import helper.ExponentialDistribution;

import models.Bus;
import models.BusHalt;
import models.Rider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {
    public static String END_MESSAGE = "This is the end of the Simulation";

    private static Integer MAX_PASSENGER = 190;
    private static Integer MAX_BUSES = 20;

    private static Double busMean = 20 * 60 * 1000 * 1.0;
    private static Double riderMean = 30 * 1000 * 1.0;

    private static Integer maxPassengers = 9;

    private static Integer busCount = 0;
    private static Integer riderCount = 0;

    public static void main(String[] args) throws InterruptedException {

        try {
            maxPassengers = Integer.valueOf(args[0]);
            MAX_PASSENGER = Integer.valueOf(args[1]);
            MAX_BUSES = Integer.valueOf(args[2]);
        } catch (Exception ex) {
            System.out.println(
                "Maximum Passengers count per bus - " + maxPassengers + "\n" +
                "Passengers count - " + MAX_PASSENGER + "\n" +
                "Bus count - " + MAX_BUSES
            );
        }

        BusHalt bushalt = BusHalt.createBusHalt();
        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore boarded = new Semaphore(0);

        List<Thread> riders = new ArrayList<>();
        List<Thread> buses = new ArrayList<>();
        Boolean isFinished = false;

        ExponentialDistribution busExponentialDistribution = new ExponentialDistribution(busMean, MAX_BUSES);
        ExponentialDistribution riderExponentialDistribution = new ExponentialDistribution(riderMean, MAX_PASSENGER);

        Runnable busHaltSimulation = () -> {
            for (int i = 0; i < MAX_PASSENGER; i++ ) {
                try {
                    Thread.sleep((long)(Math.abs(riderExponentialDistribution.getNext())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));
                riderThread.start();
                riders.add(riderThread);
            }

            for (int i=0; i < riders.size(); i++) {
                try {
                    riders.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                riderCount = riderCount + 1;
            }
        };

        Runnable busScheduleSimulation = () -> {
            for (int i = 0; i < MAX_BUSES; i++) {
                try {
                    Thread.sleep((long)(Math.abs(busExponentialDistribution.getNext())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread busThread = new Thread(new Bus(mutex, bus, boarded, bushalt, maxPassengers));
                busThread.start();
                buses.add(busThread);
            }

            for (int i=0; i < buses.size(); i++) {
                try {
                    buses.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                busCount = busCount + 1;
            }
        };

        Thread busHaltSimulationThread = new Thread(busHaltSimulation);
        Thread busScheduleSimulationThread = new Thread(busScheduleSimulation);

        busHaltSimulationThread.start();
        Thread.sleep(100);
        busScheduleSimulationThread.start();

        while ((busCount < MAX_BUSES) && (riderCount < MAX_PASSENGER)) {
            Thread.sleep(10);
        }

        System.out.println("\n" + END_MESSAGE);

        busHaltSimulationThread.join();
        busScheduleSimulationThread.join();
    }
}
