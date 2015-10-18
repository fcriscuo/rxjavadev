package org.mskcc.cbio.rxjavadev.basic;

import org.apache.log4j.Logger;

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
 * Created by Fred Criscuolo on 7/30/15.
 * criscuof@mskcc.org
 */
public class SubjectTest {
    private static final Logger logger = Logger.getLogger(SubjectTest.class);
    public static void main (String...args){
        SubjectConsumer consumer = new SubjectConsumer((SubjectService.INSTANCE.getSubject()));
        logger.info("Consumer activated");
        MessageProducer producer = new MessageProducer((SubjectService.INSTANCE.getSubject()));
        logger.info("Producer started");
        producer.produceMessages(10);
        SubjectService.INSTANCE.getSubject().onCompleted();

    }
}
