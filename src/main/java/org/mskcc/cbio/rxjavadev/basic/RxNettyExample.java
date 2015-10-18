package org.mskcc.cbio.rxjavadev.basic;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.server.HttpServer;

import java.nio.charset.Charset;

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
 * Created by Fred Criscuolo on 7/9/15.
 * criscuof@mskcc.org
 */
public final class RxNettyExample {


        public static void main(String... args) throws InterruptedException {
            HttpServer<ByteBuf, ByteBuf> server = RxNetty.createHttpServer(8080, (request, response) -> {
                System.out.println("Server => Request: " + request.getPath());
                try {
                    if ("/error".equals(request.getPath())) {
                        throw new RuntimeException("forced error");
                    }
                    response.setStatus(HttpResponseStatus.OK);

                    response.writeString("Path Requested =>: " + request.getPath() + '\n');
                    return response.close();
                } catch (Throwable e) {
                    System.err.println("Server => Error [" + request.getPath() + "] => " + e);
                    response.setStatus(HttpResponseStatus.BAD_REQUEST);
                    response.writeString("Error 500: Bad Request\n");
                    return response.close();
                }
            });

            server.start();

            RxNetty.createHttpGet("http://localhost:8080/")
                    .flatMap(response -> response.getContent())
                    .map(data -> "Client => " + data.toString(Charset.defaultCharset()))
                    .toBlocking().forEach(System.out::println);

            RxNetty.createHttpGet("http://localhost:8080/error")
                    .flatMap(response -> response.getContent())
                    .map(data -> "Client => " + data.toString(Charset.defaultCharset()))
                    .toBlocking().forEach(System.out::println);

            RxNetty.createHttpGet("http://localhost:8080/data")
                    .flatMap(response -> response.getContent())
                    .map(data -> "Client => " + data.toString(Charset.defaultCharset()))
                    .toBlocking().forEach(System.out::println);

            server.shutdown();
        }
}
