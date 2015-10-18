package org.mskcc.cbio.rxjava.jdbc;


        import java.sql.Connection;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.CountDownLatch;

        import com.google.common.base.Optional;
        import org.apache.log4j.Logger;
        import org.mskcc.cbio.rxjava.jdbc.model.Cbio;
        import rx.Observable;

        import rx.Observer;
        import rx.Subscriber;
        import rx.Subscription;
        import rx.functions.Action1;
        import rx.observables.AbstractOnSubscribe;
        import rx.schedulers.Schedulers;

public class DataServices {

    private static final Logger logger = Logger.getLogger(DataServices.class);
    public static List<Cbio.CancerStudy> getCancerStudyList() {
        //Optional<Connection> connOpt = ConnectionService.INSTANCE.getCbioConnection();

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
    public static void main(String...args) {
        final CountDownLatch latch = new CountDownLatch(1);
        Subscription csSub = getCancerStudyAsync().subscribe(new Observer<Cbio.CancerStudy>() {
            @Override
            public void onCompleted() {
                latch.countDown(); // signal completion
            }
            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage());
            }
            @Override
            public void onNext(Cbio.CancerStudy cancerStudy) {
                logger.info(cancerStudy.toString());
            }
        });
        try {
            latch.await();
            logger.info("FINIS....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
