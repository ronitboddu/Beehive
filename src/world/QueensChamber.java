package world;

import bee.Drone;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The queen's chamber is where the mating ritual between the queen and her drones is conducted.
 * The drones will enter the chamber in order. If the queen is ready and a drone is in here,
 * the first drone will be summoned and mate with the queen. Otherwise the drone has to wait.
 * After a drone mates they perish, which is why there is no routine for exiting
 *  @author RIT CS
 *  @author Ronit Boddu, rb1209@g.rit.edu
 *  @author Shivani Singh, ss5243@g.rit.edu
 */
public class QueensChamber {
    /**
     * to indicate whether a queen is ready to mate or not
     */
    private boolean isQueenReady;
    /**
     * A shared object
     */
    Object lock = new Object();
    /**
     * concurrent linked queue of drone
     */
    private LinkedList<Drone> droneQueue;

    /**
     * The constructor to initialize the QueensChamber
     */
    public QueensChamber(){
        isQueenReady=false;
        droneQueue = new LinkedList<>();
    }

    /**
     * A drone enters the chamber.
     * @param drone
     * @return void
     */
    public void enterChamber(Drone drone){
        System.out.println("*QC* "+drone.toString()+" enters chamber");
        synchronized (lock){
            droneQueue.add(drone);
            if (!isQueenReady || droneQueue.peek()!=drone){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            droneQueue.poll();
        }

        System.out.println("*QC* "+ drone +" leaves chamber");
    }

    /**
     * When the queen is ready, they will summon the next drone from the collection (if at least one is there).
     * @return void
     */
    public void summonDrone(){
        synchronized (lock) {
            if (droneQueue.size() > 0) {
                Drone mateDrone = droneQueue.peek();
                mateDrone.setMated();
                System.out.println("*QC* Queen mates with "+ mateDrone);
                lock.notifyAll();
            }
        }
    }

    /**
     * At the end of the simulation the queen uses this routine repeatedly
     * to dismiss all the drones that were waiting to mate.
     * @return void
     */
    public void dismissDrone(){
        synchronized (lock){
            lock.notifyAll();
            while(hasDrone()){
                Drone notMatedDrone = droneQueue.poll();
                System.out.println("*QC* "+notMatedDrone.toString()+" leaves chamber");
            }
        }
    }

    /**
     * Are there any waiting drones? The queen uses this to check if she can mate,
     * and also in conjunction with dismissDrone().
     * @return boolean
     */
    public boolean hasDrone(){
        return !droneQueue.isEmpty();
    }

    /**
     * to indicate that the queen is ready for the mating ritual
     * @param queenReady
     * @return void
     */
    public void setQueenReady(boolean queenReady) {
        isQueenReady = queenReady;
    }
}
