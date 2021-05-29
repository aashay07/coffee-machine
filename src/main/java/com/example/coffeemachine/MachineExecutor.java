package com.example.coffeemachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;


public class MachineExecutor {
    /**
     * MachineExecutor class is multithreading class used to execute prepare beverage method in parallel
     * with thread limit of no of outlets present in coffee machine
     */

    private final ExecutorService exec;
    private final Semaphore semaphore;
    private final int bound;

    public MachineExecutor(ExecutorService exec, int bound) {
        this.exec = exec;
        semaphore = new Semaphore(bound);
        this.bound = bound;
    }

    /**
     * Semaphore lock will be aquired to execute run() method, and release the lock once 
     * the process is completed on the thread
     * 
     * @param command
     * @throws InterruptedException
     */
    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (final RejectedExecutionException e) {
            semaphore.release();
        }
    }

    /**
     * Check if all the semaphores are released and executor is ready to be
     * terminated
     */
    public boolean isReadyToTerminate() {
        return semaphore.availablePermits() == bound;
    }

    public void shutdown() {
        exec.shutdown();
    }
}
