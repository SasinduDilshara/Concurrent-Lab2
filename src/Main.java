import helper.ExponentialDistribution;
import models.Bus;
import models.BusHalt;
import models.Rider;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {
    private static Integer MAX_PASSENGER = 13;
    private static Integer MAX_BUSES = 10;
    private static Double busMean = 20 * 60 * 1000 * 0.001;
    private static Double riderMean = 30 * 1000 * 0.001;

    public static void main(String[] args) throws InterruptedException {
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
            for (int i = 0; i < MAX_PASSENGER; i++ ){
                try {
                    Thread.sleep((long)(Math.abs(riderExponentialDistribution.getNext())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread riderThread = new Thread(new Rider(mutex, bus, boarded, bushalt));
                riderThread.start();
                riders.add(riderThread);
                System.out.println(bushalt.getWaiting());
            }

            for (int i=0; i < riders.size(); i++) {
                try {
                    riders.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable busScheduleSimulation = () -> {
            for (int i = 0; i < MAX_BUSES; i++) {
                try {
                    Thread.sleep((long)(Math.abs(busExponentialDistribution.getNext())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread busThread = new Thread(new Bus(mutex, bus, boarded, bushalt));
                busThread.start();
                buses.add(busThread);
            }

            for (int i=0; i < buses.size(); i++) {
                try {
                    buses.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread busHaltSimulationThread = new Thread(busHaltSimulation);
        Thread busScheduleSimulationThread = new Thread(busScheduleSimulation);

        busHaltSimulationThread.start();
        busScheduleSimulationThread.start();

        busHaltSimulationThread.join();
        busScheduleSimulationThread.join();

        System.out.println("\nEnd of the Simulation");
    }
}