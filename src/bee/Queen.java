package bee;

import util.RandomBee;
import world.BeeHive;

/**
 * The queen is the master of the bee hive and the only bee that is allowed
 * to mate with the drones.  The way the queen works is she will try to
 * mate with a drone if these two conditions are met:<br>
 * <br>
 * <ul>
 *     <li>The bee hive has 1 unit of nectar and 1 unit of pollen</li>
 *     <li>There is a drone available and ready to do the wild thing</li>
 * </ul>
 * <br>
 * After the stimulating mating session which takes one unit of time,
 * the queen produces between 1 and 4 new bees (if resources exist).
 * Finally, the queen takes a break and smokes a cigarette and puts on some
 * netflix before she chills with her next drone.
 *
 * @author RIT CS
 * @author Ronit Boddu, rb1209@g.rit.edu
 * @author Shivani Singh, ss5243@g.rit.edu
 */
public class Queen extends Bee {
    /**
     * the amount of time the queen waits after performing a task, whether she mated
     * this specific time or not.
     */
    public final static int SLEEP_TIME_MS = 1000;
    /** the time it takes for the queen and the drone to mate */
    public final static int MATE_TIME_MS = 1000;
    /** the minimum number of new bees that will be created by one mating session */
    public final static int MIN_NEW_BEES = 1;
    /** the maximum number of new bees that will be created by one mating session */
    public final static int MAX_NEW_BEES = 4;

    /**
     * Create the queen.  She should get the queen's chamber from the bee hive.
     *
     * @param beeHive the bee hive
     */
    protected Queen(BeeHive beeHive) {
        super(Role.QUEEN, beeHive);
    }

    /**
     * The queen will continue performing her task of mating until the bee hive
     * becomes inactive. Each time she tries to mate, whether successful or not,
     * she will sleep for the required time.
     * The queen will first check that both conditions are met (see the class
     * level description).  If so, the queen will summon the next drone,
     * and sleep to simulate the mating time.  Next,
     * the queen will roll the dice to see how many bees she should
     * try and create, between the min and max inclusive.  Each time there are
     * enough resources a new bee is created.  The bees are created based on
     * another random dice roll - a nectar worker bee has a 20% chance
     * of being created, a pollen bee has a 20% change of being created,
     * and a drone has a 60% change of being created.  After all the bees
     * are created for a single mating message, you should display:<br>
     * <br>
     * <tt>*Q* Queen birthed # children</tt><br>
     * <br>
     * <br>
     * When the simulation is over and before the queen can retire, she needs
     * to make sure that she individually dismisses each drone that is
     * still waiting in her chamber.
     */
    public void run() {
        while(beeHive.isActive()){
            if(beeHive.hasResources() && beeHive.getQueensChamber().hasDrone()){
                beeHive.getQueensChamber().setQueenReady(true);
                beeHive.getQueensChamber().summonDrone();
                try {
                    sleep(MATE_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int numOffSpring = RandomBee.nextInt(MIN_NEW_BEES,MAX_NEW_BEES);
                int count=0;
                while(beeHive.hasResources() && numOffSpring>0){
                    int offSpringType = RandomBee.nextInt(1,5);
                    beeHive.claimResources();
                    Bee bee = giveBirth(offSpringType);
                    count++;
                    numOffSpring--;
                    beeHive.addBee(bee);
                }
                System.out.println("*Q* Queen birthed "+count+" children");
                try {
                    sleep(SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                beeHive.getQueensChamber().setQueenReady(false);
            }
        }
        beeHive.getQueensChamber().dismissDrone();
    }

    /**
     * The queen gives birth to bees where bees are created  The bees are created based on another random dice roll
     * a nectar worker bee has a 20% chance of being created, a pollen bee has a 20% change of being created, and a
     * drone has a 60% change of being created. After all the bees are created for a single mating message, you should
     * display:
     * @param offSpringType the offspring type: 1=Nectar, 2=Pollen, 3=4=5= Drone.
     * @return Bee
     */
    private Bee giveBirth(int offSpringType){
        if (offSpringType==1){
            return Bee.createBee(Role.WORKER, Worker.Resource.NECTAR,beeHive);
        }
        else if(offSpringType == 2){
            return Bee.createBee(Role.WORKER, Worker.Resource.POLLEN, beeHive);
        }
        else {
            return Bee.createBee(Role.DRONE, Worker.Resource.NONE, beeHive);
        }
    }
}