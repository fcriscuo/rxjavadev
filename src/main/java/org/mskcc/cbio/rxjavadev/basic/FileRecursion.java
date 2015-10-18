package org.mskcc.cbio.rxjavadev.basic;

import rx.Observable;

import java.io.File;


/**
 * Copyright (c) Fred Criscuolo
 * fcriscu1@jhu.edu
 * <p>
 * <p>
 * Created by fcriscuo on 6/25/15.
 */
public class FileRecursion {

    static Observable<File> listFiles (File f) {
        if( f.isDirectory()){
            return Observable.from(f.listFiles()).flatMap(FileRecursion::listFiles);
        }
        return Observable.just(f);
    }
    public static void main (String...args) {
        Observable.just(new File("/Users/criscuof/softwaredev"))
                .flatMap(FileRecursion::listFiles)
                .subscribe(f -> System.out.println(f.getAbsolutePath()));
    }
}
