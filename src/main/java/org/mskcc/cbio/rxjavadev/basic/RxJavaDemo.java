package org.mskcc.cbio.rxjavadev.basic;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


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

/*
series of RxJava operation as seen in YouTube tutorial
 */
public class RxJavaDemo {

    //private static final Logger logger = Logger.getLogger(RxJavaDemo.class);

    public static void main (String...args) {
        // create an Iterable to work with
        List<String> urlList = Lists.newArrayList();
        urlList.add("www.google.com");
        urlList.add("www.nytimes.com");
        urlList.add("www.apple.com");
        urlList.add("www.mskcc.org");
        urlList.add("www.amazon.com");
        urlList.add("www.apache.org");
        urlList.add("www.whitehouse.gov");

       Observable.create(new Observable.OnSubscribe<String>() {
           @Override
           public void call(Subscriber<? super String> subscriber) {
               if (!subscriber.isUnsubscribed()) {
                   for (int i = 1; i < 5; i++) {
                       subscriber.onNext(Integer.valueOf(i).toString());
                   }
                   subscriber.onCompleted();
               }
           }
       }).subscribe(new Subscriber<String>() {
           @Override
           public void onCompleted() {
               System.out.println(("sub1 finis"));
           }

           @Override
           public void onError(Throwable throwable) {
           }

           @Override
           public void onNext(String s) {
               System.out.println("sub 1:" + s);
           }
       });
            Observable.create(subscriber -> {
                AtomicInteger i = new AtomicInteger();
                AtomicLong requested = new AtomicLong();
                subscriber.setProducer( r-> {
                    if (requested.getAndAdd(r)==0){
                        do {
                            if(subscriber.isUnsubscribed()) {break;}
                            subscriber.onNext(i.incrementAndGet());
                        }while(requested.decrementAndGet() >0);
                    }
                });
            }).observeOn(Schedulers.newThread()).take(10).forEach(System.out::println);
        //better option
        Observable.from(urlList)
                .observeOn(Schedulers.newThread())
                .take(6).forEach(System.out::println);


    }

}
