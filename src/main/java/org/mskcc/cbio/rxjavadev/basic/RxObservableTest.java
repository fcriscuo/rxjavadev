package org.mskcc.cbio.rxjavadev.basic;

import org.apache.log4j.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Copyright (c) 2015 Memorial Sloan-Kettering Cancer Center.
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
 * Created by Fred Criscuolo on 7/14/15.
 * criscuof@mskcc.org
 */
public class RxObservableTest {
    private static final Logger logger = Logger.getLogger(RxObservableTest.class);
    public static void main (String...args){
        RxObservableTest test = new RxObservableTest();

    }

    public RxObservableTest() {
        this.performTests();

    }

    private void performTests() {
        Observable<Integer> psNumberObs = this.rxGetPositiveNumbers(1000).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer%2 == 0;
            }
        });
        psNumberObs.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("FINIS....");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("subscriber got :" + integer);
            }
        });

    }

    private  Observable<Integer> rxGetPositiveNumbers(final int numNumbers) {
        return Observable.create((Subscriber<? super Integer> subscriber) -> {
            for (int i = 0; i < numNumbers; i++) {
                subscriber.onNext(i);
            }
            subscriber.onCompleted();
        });
    }

}
