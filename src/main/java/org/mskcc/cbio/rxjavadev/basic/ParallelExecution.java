package org.mskcc.cbio.rxjavadev.basic;

import rx.Observable;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

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
 * Created by fcriscuo on 5/23/15.
 */
public class ParallelExecution {

   public static void  main( String...args)  {
        System.out.println("------------ merging Async");
       mergingAsync();
       System.out.println("------------ mergingSync");
              mergingSync();
       System.out.println("------------ mergingSyncMadeAsync");
               mergingSyncMadeAsync();
       System.out.println("------------ flatMapExampleAsync");
              flatMapExampleAsync();
}

    /**
     * flatMap uses `merge` so any async Observables it returns will execute concurrently.
     */
    private static void flatMapExampleAsync() {
        Observable.range(0,15).flatMap(i-> {
            return getDataAsync(i);
        }).toBlocking().forEach(System.out::println);
    }

    /*
    Can make synchronous Observables, asynchronous
     */
    private static void mergingSyncMadeAsync() {
        // make a synchronous operation, synchronous by subscribing to it using subscribeOn
        Observable.merge(
                getDataSync(1).subscribeOn(Schedulers.io()),
                getDataSync(2).subscribeOn(Schedulers.io())
        )
                .toBlocking().forEach(System.out::println);

    }

    private static void mergingAsync() {
        Observable.merge(getDataAsync(1), getDataAsync(2))
                .toBlocking().forEach(System.out::println);
    }

    private static void mergingSync() {
        // the delay should be visible
        Observable.merge(getDataSync(1),getDataSync(2))
                .toBlocking().forEach(System.out::println);
    }

    // artificial representation of IO work
    static Observable<Integer> getDataAsync(int i){
        return getDataSync(i).subscribeOn(Schedulers.io());
    }
    static Observable<Integer> getDataSync(int i){
        return Observable.create((Subscriber<? super Integer> s) ->{
            // introduce some latency
            try{
                Thread.sleep(1000);
            }catch (Exception e) { e.printStackTrace();}
                s.onNext(i);
                s.onCompleted();
        });
    }
}
