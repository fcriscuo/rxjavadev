package org.mskcc.cbio.rxjavadev.basic;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.Arrays;
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
public class TestSubscribe {
    public static void main (String...args){
        TestScheduler test = Schedulers.test();
        TestSubscriber<String> ts = new TestSubscriber<>();
        Observable.interval(200, TimeUnit.MILLISECONDS,test)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long i) {
                        return i + " value";
                    }
                }).subscribe(ts);
       test.advanceTimeBy(200, TimeUnit.MILLISECONDS);
        ts.assertReceivedOnNext(Arrays.asList("0 value"));
        test.advanceTimeTo(1000,TimeUnit.MILLISECONDS);
        ts.assertReceivedOnNext(Arrays.asList("0 value", "1 value","2 value",
         "3 value","4 value"));

    }
}
