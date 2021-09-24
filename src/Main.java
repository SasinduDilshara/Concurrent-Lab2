import models.Bus;
import models.BusHalt;
import models.Rider;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    private static Integer MAX_PASSENGER = 200;
    private static Integer MAX_BUSES = 50;

    public static void main(String[] args) throws InterruptedException {
        BusHalt bushalt = BusHalt.createBusHalt();

        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore boarded = new Semaphore(0);

        List<Thread> riders = new ArrayList<>();
        List<Thread> buses = new ArrayList<>();
        Random rand = new Random();

        Runnable busHaltSimulation = () -> {
            for (int i = 0; i < MAX_PASSENGER; i++ ){
                try {
                    Thread.sleep( Math.abs(rand.nextInt() * 1000) % 1000);
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
            }
        };

        Runnable busScheduleSimulation = () -> {
            for (int i = 0; i < MAX_BUSES; i++) {
                try {
                    Thread.sleep( Math.abs(rand.nextInt() * 10000) % 10000);
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