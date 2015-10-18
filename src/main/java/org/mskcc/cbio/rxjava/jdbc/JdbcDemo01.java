package org.mskcc.cbio.rxjava.jdbc;

import autovalue.shaded.com.google.common.common.collect.Lists;
import com.github.davidmoten.rx.jdbc.ConnectionProvider;
import com.github.davidmoten.rx.jdbc.ConnectionProviderFromUrl;
import com.github.davidmoten.rx.jdbc.Database;
import org.apache.log4j.Logger;
import org.mskcc.cbio.rxjava.jdbc.model.ClinicalAttributeModel;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
 * Created by Fred Criscuolo on 6/7/15.
 * criscuof@mskcc.org
 */
public class JdbcDemo01 {
    private static final Logger logger = Logger.getLogger(JdbcDemo01.class);
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String connUrl = new String("jdbc:mysql://" + "localhost" + "/" + "cgds_gdac"
            + "?user=" + "criscuof" + "&password=" + "fred3372");

    public static void main (String...args){
        logger.info(connUrl);
        ConnectionProvider provider = null;
       try  {
           provider = new ConnectionProviderFromUrl(connUrl);
           Database db = Database.builder().connectionProvider(provider).build();

          db.select("select cancer_study_id from cancer_study order by cancer_study_id")
                   .getAs(Integer.class)
                   .subscribe(new Action1<Integer>() {
                       @Override
                       public void call(Integer s) {
                           logger.info("Cancer study ID: " + s.toString());
                       }
                   });

          db.select("select name from cancer_study where name like ? ")
                   .parameter("%Breast%")
                  .parameter("%Colon%")
                   .getAs(String.class)
                   .subscribe(new Action1<String>() {
                       @Override
                       public void call(String s) {
                           logger.info("Cancer study name: " +s);
                       }
                   });
          final  List<ClinicalAttribute> clinAttrList = Lists.newArrayList();
  /*
           db.select("select display_name from clinical_attribute")
                   .getAs(String.class)
                   .subscribe(new Action1<String>() {
                       @Override
                       public void call(String s) {
                           logger.info("select clinical attribute display name: "+s);
                           clinAttrList.add(new ClinicalAttribute(s));
                       }
                   });
           logger.info("There are " +clinAttrList.size() +" clinical attributes");
           */

           final CountDownLatch latch = new CountDownLatch(2);
           Observable<Object>obs = getClinicalAttribtues(provider.get());
           obs.subscribeOn(Schedulers.io());

           obs.subscribe(new Subscriber<Object>() {
               @Override
               public void onCompleted() {
                   logger.info("FINIS...");
                   latch.countDown();
               }

               @Override
               public void onError(Throwable throwable) {
                    logger.error(throwable.getMessage());
               }

               @Override
               public void onNext(Object o) {
                    logger.info("Clinical attribute  object:" +o.toString());
               }
           });


           Observable<ClinicalAttributeModel> modelObs = getClinicalAttributeModel(db)
                   .take(24);
           modelObs.subscribeOn(Schedulers.io());
           modelObs.subscribe(new Subscriber<ClinicalAttributeModel>() {
               @Override
               public void onCompleted() {
                   logger.info("Model observerable completed");
                   latch.countDown();
               }

               @Override
               public void onError(Throwable throwable) {
                    logger.error(throwable.getMessage());
               }

               @Override
               public void onNext(ClinicalAttributeModel model) {
                    logger.info("Clinical attribute model: attr id: " +model.getAttributeId()
                        +"  display name " +model.getDisplayName() +"   description: " +model.getDescription());
               }
           });
           logger.info("waiting on countdown latch");
           latch.await();
           logger.info("latch down to zero");
       }catch (Exception e){
            logger.error(e.getMessage());
           e.printStackTrace();
       }  finally {
           provider.close();
       }
    }
    static Observable<Object> getClinicalAttribtues(Connection conn){

            Observable<Object>obs =  Observable.create (o-> {
                try {
                    ResultSet rs = conn.createStatement()
                            .executeQuery("select display_name from clinical_attribute");
                    while (rs.next() ){
                        o.onNext(new ClinicalAttribute(rs.getString(1)));

                    }
                    rs.close();
                    o.onCompleted();
                } catch (SQLException e) {
                    o.onError(e);
                }
            }).subscribeOn(Schedulers.io());
            return obs;
    }

    static Observable<ClinicalAttributeModel>  getClinicalAttributeModel (Database db){
        return db.select("select * from clinical_attribute where datatype = :type order by attr_id ")
                .parameter("type","STRING")
                .autoMap(ClinicalAttributeModel.class);
    }

}
