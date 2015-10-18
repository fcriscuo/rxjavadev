package org.mskcc.cbio.rxjavadev.basic;

import org.apache.log4j.Logger;
import rx.subjects.PublishSubject;
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
public class MessageProducer {
    private Logger logger = Logger.getLogger(MessageProducer.class);
    private final Subject subject;

    public MessageProducer(Subject aSubject) {
        this.subject = aSubject;
    }

    public void produceMessages(int n){
        for (int i = 0 ; i < n ;i++){
            this.subject.onNext("This message number " +i);
        }
    }

}
