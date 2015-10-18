package org.mskcc.cbio.rxjavadev.basic;

import org.apache.log4j.Logger;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2014 Memorial Sloan-Kettering Cancer Center.
 * <p>
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 * documentation provided hereunder is on an "as is" basis, and
 * Memorial Sloan-Kettering Cancer Center
 * has no obligations to provide maintenance, support,
 * updates, enhancements or modifications.  In no event shall
 * Memorial Sloan-Kettering Cancer Center
 * be liable to any party for direct, indirect, special,
 * incidental or consequential damages, including lost profits, arising
 * out of the use of this software and its documentation, even if
 * Memorial Sloan-Kettering Cancer Center
 * has been advised of the possibility of such damage.
 * <p>
 * Created by fcriscuo on 6/6/15.
 */
public class WorkerDemo {
    private static final Logger logger = Logger.getLogger(WorkerDemo.class);
    public static void main (String...args) {
        Scheduler.Worker worker01 = Schedulers.newThread().createWorker();
        worker01.schedule(new Action0() {
            @Override
            public void call() {
                logger.info("Working..." + Thread.currentThread().getName());
            }
        });
        worker01.unsubscribe();
        logger.info("Fibbonoci calculation using recursion");
        // recursive demo
       Scheduler.Worker worker02 = Schedulers.computation().createWorker();
        final Subscription schedule = worker02.schedule(new Action0() {
            int n = 20;
            @Override
            public void call() {
                logger.info("worker02 thread = " + Thread.currentThread().getName());
                long x = fib(n);
                logger.info("Fibbonoci for " + n++ + " = " + x);
                worker02.schedule(this);
            }
        });
        try {
            Thread.sleep(100L);
            // do work on main thread
            logger.info("++++++Fibbonocci for 10 = " + fib(10));
            Thread.sleep(1500L);
            worker02.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // delayed and periodic schedulers
        logger.info(">>>>> worker03 ");
        long delay = 500L;
        Scheduler.Worker worker03 = Schedulers.newThread().createWorker();
        Action0 action = new Action0() {
            @Override
            public void call() {
                logger.info("worker 03 invoked");
            }
        };

        worker03.schedule(action, delay, TimeUnit.MILLISECONDS);
        try {
            Thread.sleep(500L);
            worker03.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("FINIS " + Thread.currentThread().getName());

    }
    public static long fib(int n) {
        if (n <= 1) return n;
        else return fib(n-1) + fib(n-2);
    }
}
