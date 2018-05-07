package org.anhcraft.spaciouslib.scheduler;

public abstract class TaskScheduler {
    protected Runnable runnable;
    protected boolean stopped;
    protected Thread thread;

    public TaskScheduler(Runnable runnable){
        this.runnable = runnable;
        this.stopped = false;
    }

    /**
     * Runs this scheduler
     */
    public abstract void run();

    /**
     * Stops this scheduler
     */
    public void stop(){
        this.stopped = true;
    }
}
