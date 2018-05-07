package org.anhcraft.spaciouslib.scheduler;

public class TimerTask extends TaskScheduler {
    private long period;
    private long delay;

    /**
     * Creates a timer scheduler
     * @param runnable the runnable for executing
     * @param delay the delay time (in seconds) before the first execution time
     * @param period the period time (in seconds) between execution times
     */
    public TimerTask(Runnable runnable, long delay, long period) {
        super(runnable);
        this.period = period;
        this.delay = delay;
    }

    @Override
    public void run() {
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay*1000);
                    while(true) {
                        if(stopped){
                            thread.interrupt();
                            break;
                        }
                        runnable.run();
                        Thread.sleep(period*1000);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.thread.start();
    }
}
