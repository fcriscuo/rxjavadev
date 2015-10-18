package org.mskcc.cbio.rxjavadev.basic;

import org.apache.log4j.Logger;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.Subject;

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
 * Created by Fred Criscuolo on 7/29/15.
 * criscuof@mskcc.org
 */
public class SubjectConsumer
{
    private Subject subject;
    private Logger logger = Logger.getLogger(SubjectConsumer.class);


    public SubjectConsumer(Subject aSubject) {
        this.subject = aSubject;
        this.activate();
    }

    private void activate() {

            this.subject.subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    logger.info("Completed");
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.error(throwable.getMessage());
                }

                @Override
                public void onNext(String s) {
                    logger.info("Received: " +s);
                }
            });


    }
}
