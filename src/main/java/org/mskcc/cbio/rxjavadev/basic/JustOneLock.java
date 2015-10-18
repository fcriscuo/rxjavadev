package org.mskcc.cbio.rxjavadev.basic;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

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
 * Created by Fred Criscuolo on 6/5/15.
 * criscuof@mskcc.org
 */
class JustOneLock {
    FileLock lock;
    FileChannel channel;

    public boolean isAppActive() throws Exception{
        File file = new File(System.getProperty("user.home"),
                "FireZeMissiles1111" + ".tmp");
        channel = new RandomAccessFile(file, "rw").getChannel();

        lock = channel.tryLock();
        if (lock == null) {
            return true;
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    lock.release();
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }

    public static void main(String[] args)throws Exception {
        JustOneLock u = new JustOneLock();

        if (u.isAppActive()) {
            System.out.println("Already active, stop!");
            System.exit(1);
        }
        else {
            System.out.println("NOT active... Do hard work for 10 seconds.");
            try{Thread.sleep(10000);}catch(Exception e){}
        }
    }
}
