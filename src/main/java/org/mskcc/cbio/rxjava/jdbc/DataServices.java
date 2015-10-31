package org.mskcc.cbio.rxjava.jdbc;


        import java.sql.Connection;
        import java.sql.ResultSet;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Set;
        import java.util.concurrent.CountDownLatch;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.TimeUnit;
        import com.google.common.base.Joiner;
        import com.google.common.base.Splitter;
        import com.google.common.base.Stopwatch;
        import com.google.common.collect.Sets;
        import com.sun.istack.internal.Nullable;
        import org.apache.log4j.Logger;
        import org.mskcc.cbio.rxjava.jdbc.model.Cbio;
        import rx.*;
        import rx.functions.Action0;
        import rx.functions.Action1;
        import rx.schedulers.Schedulers;

public class DataServices {

    private static final Logger logger = Logger.getLogger(DataServices.class);
    private static final Splitter commaSplitter = Splitter.on(',');
    private static final Joiner commaJoiner = Joiner.on(",");


    public static List<Cbio.CancerStudy> getCancerStudyList() {
        try (Connection c = ConnectionService.INSTANCE.getCbioConnection().get()) {
            ResultSet rs = c.createStatement().executeQuery("select * from cgds_gdac.cancer_study");
            ArrayList<Cbio.CancerStudy> genes = new ArrayList<>();
            while (rs.next()) {
               genes.add(Cbio.CancerStudy.create(rs.getString("CANCER_STUDY_IDENTIFIER"),rs.getString("TYPE_OF_CANCER_ID"),
                       rs.getString("NAME"), rs.getString("SHORT_NAME"),rs.getString("DESCRIPTION")) );
            }
            rs.close();
            return genes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Observable<Cbio.CancerStudy> getCancerStudiesWrapped() {
        return Observable.defer(() -> {
            return Observable.from(getCancerStudyList());
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<Cbio.CancerStudy> getCancerStudyAsync() {
        Connection c = ConnectionService.INSTANCE.getCbioConnection().get();
        return Observable.<Cbio.CancerStudy> create(o -> {
            try (ResultSet rs = c.createStatement().executeQuery("select * from cancer_study"); ) {
                while (rs.next() && !o.isUnsubscribed() ) {
                    o.onNext(Cbio.CancerStudy.create(rs.getString("CANCER_STUDY_IDENTIFIER"), rs.getString("TYPE_OF_CANCER_ID"),
                            rs.getString("NAME"), rs.getString("SHORT_NAME"), rs.getString("DESCRIPTION")));
                    Thread.sleep(100L);  // simulate long query
                }
                o.onCompleted();
            } catch (Exception e) {
                logger.error(e.getMessage());
                o.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<Cbio.GeneticAlteration> getGeneticAltObs (Integer geneticProfileId, @Nullable Set<Long> entrezIdSet){
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String queryString = "SELECT * FROM genetic_alteration WHERE "
                + " GENETIC_PROFILE_ID = " + geneticProfileId;
        if( entrezIdSet != null && entrezIdSet.size()>0 ) {
            queryString = queryString + " AND ENTREZ_GENE_ID IN ("
                    + commaJoiner.join(entrezIdSet) + ")";
        }
        final String query = queryString;
        return Observable.<Cbio.GeneticAlteration> create (o ->{
            try (Connection c = ConnectionService.INSTANCE.getCbioConnection().get()){
                ResultSet rs = c.createStatement().executeQuery( query);
                while (rs.next() && !o.isUnsubscribed()) {
                    o.onNext(Cbio.GeneticAlteration.create(rs.getInt("GENETIC_PROFILE_ID"), rs.getInt("ENTREZ_GENE_ID"),
                            commaSplitter.splitToList(rs.getString("VALUES"))));
                }
                logger.info("Query completed");
                rs.close();
                o.onCompleted();
            } catch (Exception e){
                o.onError(e);
            }
                }

        ).subscribeOn(Schedulers.io()).take(1000);
    }



    public static void main(String...args) {
        final CountDownLatch latch = new CountDownLatch(1);

        Set<Long> geneSet = Sets.newHashSet();
        geneSet.add(677835L);
        geneSet.add(9636L);
        geneSet.add(728661L);
        geneSet.add(118424L);
        ExecutorService executor = Executors.newFixedThreadPool(10);
       //Observable<Object> geneticAltObs = DataServices.getGeneticAlterationObs(2,geneSet);
         Observable<Cbio.GeneticAlteration> geneticAltObs = DataServices.getGeneticAltObs(2,null);
        final Stopwatch subWatch = Stopwatch.createUnstarted();

        geneticAltObs.doOnNext(new Action1<Cbio.GeneticAlteration>() {
            @Override
            public void call(Cbio.GeneticAlteration geneticAlteration) {
                Scheduler.Worker worker = Schedulers.from(executor).createWorker();
                final Subscription schedule = worker.schedule(new Action0() {
                    @Override
                    public void call() {
                        //logger.info(Thread.currentThread().getName()); // display what thread this is running on
                        logger.info(geneticAlteration.toString());
                        try {
                            // complete a compute intensive task here
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
        });


        /*
        geneticAltObs.subscribe(new Subscriber<Cbio.GeneticAlteration>() {
            @Override
            public void onStart(){
                subWatch.start();
                logger.info("Subscriber starting");
                //request(100L);
            }

            @Override
            public void onCompleted() {
                subWatch.stop();
                long seconds = subWatch.elapsed(TimeUnit.SECONDS);
                logger.info("Subscription completed  time = " +seconds +" seconds");
                latch.countDown();

            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage());
                subWatch.stop();
                latch.countDown();
            }

            @Override
            public void onNext(Cbio.GeneticAlteration o) {
                logger.info(Thread.currentThread().getName()); // display what thread this is running on
                logger.info(o.toString());
                try {
                    // complete a compute intensive task here
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

             public void onNext(Cbio.GeneticAlteration o) {
                Scheduler.Worker worker = Schedulers.from(executor).createWorker();
                final Subscription schedule = worker.schedule(new Action0() {
                    @Override
                    public void call() {
                        //logger.info(Thread.currentThread().getName()); // display what thread this is running on
                        logger.info(o.toString());
                        try {
                            // complete a compute intensive task here
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });

            }

        });
            */
        try {
            latch.await();
            logger.info("FINIS....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
