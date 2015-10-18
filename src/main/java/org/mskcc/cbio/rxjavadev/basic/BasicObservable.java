package org.mskcc.cbio.rxjavadev.basic;

import com.google.common.collect.Lists;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2014 Memorial Sloan-Kettering Cancer Center.
 * <p/>
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
 * <p/>
 * Created by fcriscuo on 5/17/15.
 */
public class BasicObservable {

    public BasicObservable() {}

    private void performTests() {
       List<String> aList = Lists.newArrayList("A","B","C");
        Observable.from(aList)
                .lift( new InternStrings())
                .subscribe();

        Observable.create(new Observable.OnSubscribe<Integer>(){

            @Override
            public void call(Subscriber<? super Integer> observer0) {
                try {
                    if(!observer0.isUnsubscribed()){
                        for(int i =1 ; i <= 5; i++){
                            observer0.onNext(i);
                        }
                        observer0.onCompleted();
                    }
                }catch(Exception e){
                    observer0.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Next " +integer);
            }
        });



    }



    public static void main(String...args){
        BasicObservable basic = new BasicObservable();
        basic.performTests();
        Observable<String> myObservable = Observable.just("Hello World!")
                .map(new Func1<String, String>() {
                    public String call(String s) {
                        return s + " Fred";
                    }
                });

                        Action1 < String > onNext01 = new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println("Upper case  message: " + s.toUpperCase());
                            }
                        };

        Action1<String> lowerCaseAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Lower case message: " +s.toLowerCase());
            }

        };
        myObservable.subscribe(onNext01);
        myObservable.subscribe(lowerCaseAction);

        Observable.just("Hello, world!")
                .map(s -> s.hashCode())
                .subscribe(i -> System.out.println(Integer.toString(i)));

        Observable.just("Hello, world!")
                .map(s -> s + " -Fred")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> System.out.println(s));


        final List<String> saveList = Lists.newArrayList();
       urlList()
               .flatMap(urls -> Observable.from(urls))
               .flatMap(url -> getTitle(url))
               .filter(title -> title.contains(".com"))
               .take(2)
               .doOnNext(title -> saveList.add(title))
               .subscribe(title -> System.out.println(title));

            System.out.println("The save list size = " + saveList.size());



    final CountDownLatch latch = new CountDownLatch((4));
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long counter) {
                        latch.countDown();
                        System.out.println("Got: " +counter);
                    }
                });
        try {
            System.out.println("Waiting on latch to complete");
            latch.await();
            System.out.println("Latch completed");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // convert Observable to blocking using toBlocking operator
        Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5)
                .toBlocking()
                .forEach(new Action1<Long>() {
                    @Override
                    public void call(Long counter) {
                        System.out.println("Blocking Got: " + counter);
                    }
                });
        // convert output from Observable to a list
        List<Integer> list = Observable
                .just(1, 2, 3)
                .toList()
                .toBlocking()
                .single();

        // Prints: [1, 2, 3]
        System.out.println(" output as a list: " + list);

        // create an Observable 7 subscribe to it
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    System.out.println("Subscribed? " + !subscriber.isUnsubscribed());
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = 0; i < 5; i++) {
                            subscriber.onNext(i);
                        }
                        // call only once
                        subscriber.onCompleted();
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("Observable create Got: " + integer);
            }
        });

        Observable
                .just(1, 2, 3, 4, 5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer value) {
                        return sum + value;
                    }
                }).subscribe(new  Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("Sum: " + integer);
            }
        });

        Observable
                .just(1, 2, 3, 4, 5)
                .groupBy(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                }).subscribe(new Action1<GroupedObservable<Boolean, Integer>>() {
            @Override
            public void call(GroupedObservable<Boolean, Integer> grouped) {
                grouped.toList().subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        System.out.println(integers + " (Even: " + grouped.getKey() + ")");
                    }
                });
            }
        });

        urlList()
                .flatMap(urls -> Observable.from(urls))
                .flatMap(url -> getTitle(url))
                .groupBy(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.endsWith(".com");
                    }
                })
                .subscribe(new Action1<GroupedObservable<Boolean, String>>() {
                    @Override
                    public void call(GroupedObservable<Boolean, String> grouped) {
                        grouped.toList().subscribe(new Action1<List<String>>() {
                            @Override
                            public void call(List<String> strings) {
                                System.out.println(strings +" (.com " +grouped.getKey() +")");
                            }
                        });

                    }
                });
        Observable<Integer> evens = Observable.just(2, 4, 6, 8, 10);
        Observable<Integer> odds = Observable.just(1, 3, 5, 7, 9);

        Observable.merge(evens, odds)
                .toList()
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> ints) {
                        System.out.println("Merged List " + ints);
                    }
                });

        Observable
                .zip(evens, odds, new Func2<Integer, Integer, String>() {

                    @Override
                    public String call(Integer integer, Integer integer2) {
                        return "The sum of " + integer + " plus " + integer2 + " = " + (integer + integer2);
                    }
                })
               // .zip(evens, odds, (v1, v2) -> v1 + " + " + v2 + " is: " + (v1 + v2))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });

        Observable
                .just("Apples", "Bananas", "Grapes")
                .doOnNext(s -> {
                    throw new RuntimeException("I don't like: " + s);
                })
                .onErrorReturn(throwable -> {
                    System.err.println("Oops: " + throwable.getMessage());
                    return "Default";
                }).subscribe(System.out::println);

        // retry on error up to 5 times
        Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .doOnNext(integer -> {
                    if (new Random().nextInt(10) + 1 == 5) {
                        throw new RuntimeException("Boo!");
                    }
                })
                .retry(5)
                .distinct()
                .subscribe(System.out::println);
        // retry with backoff
        Observable
                .range(1, 10)
                .doOnNext(integer -> {
                    if (new Random().nextInt(10) + 1 == 5) {
                        throw new RuntimeException("Boo!");
                    }
                })
                .retryWhen(attempts ->
                        attempts.zipWith(Observable.range(1, 3), (n, i) -> i)
                                .flatMap(i -> {
                                    System.out.println("delay retry by " + i + " second(s)");
                                    return Observable.timer(i, TimeUnit.SECONDS);
                                }))
                .distinct()
                .subscribe(System.out::println);

        // specify a specific thread for the subscriber
        System.out.println(">>>>>Schedulers");
        Observable
                .range(1,8)
                .map(new Func1<Integer, Integer>() {


                    @Override
                    public Integer call(Integer integer) {
                        System.out.println("Got: " + integer + " (" + Thread.currentThread().getName() + ")");
                        return integer + 2;
                    }
                })
               .observeOn(Schedulers.computation())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("Got: " + integer + " (" + Thread.currentThread().getName() + ")");
                    }
                });


    }



    static Observable<List<String>> urlList() {
        List<String> urlList = Lists.newArrayList();
        urlList.add("www.google.com");
        urlList.add("www.nytimes.com");
        urlList.add("www.apple.com");
        urlList.add("www.mskcc.org");
        urlList.add("www.amazon.com");
        urlList.add("www.apache.org");
        urlList.add("www.whitehouse.gov");
        return Observable.just(urlList);
    }

    static Observable<String> getTitle(String url){
        return Observable.just(url);
    }

 class InternStrings implements Observable.Operator<String,String>{

     @Override
     public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
         return new Subscriber<String>() {

             @Override
             public void onCompleted() {
                 subscriber.onCompleted();
             }

             @Override
             public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
             }

             @Override
             public void onNext(String s) {
                    subscriber.onNext(s.intern());
             }
         };
     }
 }
}
