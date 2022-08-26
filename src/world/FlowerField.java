package world;

import bee.Worker;

/**
 * The field of flowers that are ripe for the worker bees to gather the nectar and pollen resources.
 * The bees can arrive in any order and they are immediately allowed to start gathering,
 * as long as there is a free flower. Otherwise the bee must wait until a flower becomes free.
 * @author RIT CS
 * @author Ronit Boddu, rb1209@g.rit.edu
 * @author Shivani Singh, ss5243@g.rit.edu
 */
public class FlowerField {
    /**
     * the maximum number of workers allowed in the field at the same time
     */
    private static final int MAX_WORKERS=10;
    /**
     * the number of workers in the flower field
     */
    private int workerNum;

    /**
     * A shared object
     */
    private final Object lock = new Object();

    /**
     * constructor to initialize the FlowerField class
     */
    public FlowerField(){
        workerNum=0;
    }

    /**
     * Worker bee enters the flower field and waits if the all the flowers are occupied.
     * @param worker Worker bee
     * @return void
     */
    public void enterField(Worker worker){
        System.out.println("*FF* "+worker.toString() +" enters field");
        synchronized (lock){
            while(workerNum>=MAX_WORKERS ){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        workerNum++;
    }

    /**
     * Exits field after extracting the pollen/nectar from the flower.
     * @param worker Worker Bee
     * @return void
     */
    public void exitField(Worker worker){
        synchronized (lock){
            if(workerNum>0){
                workerNum--;
                lock.notify();
                System.out.println("*FF* "+worker.toString()+ " leaves field");
            }
        }
    }
}
