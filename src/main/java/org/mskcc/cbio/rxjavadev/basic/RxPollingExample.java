package org.mskcc.cbio.rxjavadev.basic;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
public class RxPollingExample {

    private static final int INITIAL_DELAY = 1000;
    private static final int POLLING_INTERVAL = 1000;
    private static final String DATE_FORMAT = "h:mm:ss a";

    public static void main(String[] args) {
        Observable.create((Subscriber<? super Calendar> subscriber) ->
                Schedulers.immediate().createWorker()
                .schedulePeriodically(() ->
                                subscriber.onNext(Calendar.getInstance(Locale.US)),
                        INITIAL_DELAY, POLLING_INTERVAL, TimeUnit.MILLISECONDS))
                .take(10)
                .observeOn(Schedulers.io())
                .subscribe(calendar -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                    String time = simpleDateFormat.format(calendar.getTime());
                    System.out.println(time);
                }, Throwable::printStackTrace);

    }
}
