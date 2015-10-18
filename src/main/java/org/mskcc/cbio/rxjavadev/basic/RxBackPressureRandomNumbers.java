package org.mskcc.cbio.rxjavadev.basic;

import rx.Observable;
import rx.Subscriber;

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
 * Created by Fred Criscuolo on 7/10/15.
 * criscuof@mskcc.org
 */
public class RxBackPressureRandomNumbers {

    /**
     * This main method subscribes to two random number Observable functions zipped together
     * to form a single result.
     *
     * Zip may be one of the most useful Rx functions for REST applications because it allows
     * you to easily construct your UI from multiple requests or data sources which may arrive
     * asynchronously at different times. Zip combines several source Observables by obtaining
     * the latest result from each one.
     *
     * When one Observable produces results faster than another, this results in "backpressure."
     * There are many reactive tricks to address backpressure. The zip() function natively handles
     * backpressure by waiting until all source Observables have produced an event or result before
     * consuming a result from any one of them.
     *
     * There are numerous other solutions to backpressure depending upon your needs. buffer(),
     * throttleFirst(), throttleLast(), debounce(), and window() all offer alternative means
     * to selectively consume the emissions of source Observables.
     *
     * Try adding a random delay to one source Observable to see the zip() operator deal with
     * backpressure the same way it handles network congestion. Try adding a timeout() and retry()
     * for longer delays.
     *
     * Remember, order of operations matters! Using a timeout() and retry() on each source Observable
     * is different than using it on the zipped Observable.
     *
     * @param args
     */
    public static void main(String[] args) {
        Observable.zip(rxGetPositiveNumbers(100), rxGetNegativeNumbers(100), (integer, integer2) -> integer - integer2)
                .filter(integer -> integer > -1000 && integer < 1000)
                .subscribe(integer -> {
                    System.out.print(integer);
                    System.out.print("\n");
                }, Throwable::printStackTrace);
    }

    /**
     * Generates an Observable containing random positive integers from 1 to 10000
     * @param numNumbers
     * @return
     */
    private static Observable<Integer> rxGetPositiveNumbers(final int numNumbers) {
        return Observable.create((Subscriber<? super Integer> subscriber) -> {
            for (int i = 0; i < numNumbers; i++) {
                subscriber.onNext(i);
            }
        });
    }
    /**
     * Generates an Observable containing random negative integers from -10000 to -1
     * @param numNumbers
     * @return
     */
    private static Observable<Integer> rxGetNegativeNumbers(final int numNumbers) {
        return Observable.create((Subscriber<? super Integer> subscriber) -> {
            for (int i = 0; i < numNumbers; i++) {
                subscriber.onNext(i * -1);
            }
        });
    }
}

