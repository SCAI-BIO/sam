package de.fraunhofer.scai.bio.sam.config;

import de.fraunhofer.scai.bio.sam.task.ExportTaskWorker;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * BeanConfig
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@Configuration
@EnableScheduling
public class BeanConfig {

    @Bean
    public ExportTaskWorker task() {
        return new ExportTaskWorker();
    }

    @Bean
    OkHttpClient getokHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    private static final int RETRY_BACKOFF_DELAY = 10;
                    private static final int MAX_TRY_COUNT = 4;

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        // try the request
                        Response response = null;
                        int tryCount = 1;
                        while (tryCount <= MAX_TRY_COUNT) {
                            try {
                                response = chain.proceed(request);
                                break;
                            } catch (Exception e) {
                                if ("Canceled".equalsIgnoreCase(e.getMessage())) {
                                    // Request canceled, do not retry
                                    throw e;
                                }
                                if (tryCount >= MAX_TRY_COUNT) {
                                    // max retry count reached, giving up
                                    throw e;
                                }

                                try {
                                    // sleep delay * try count (e.g. 1st retry after 3000ms, 2nd after 6000ms, etc.)
                                    Thread.sleep(RETRY_BACKOFF_DELAY * tryCount);
                                } catch (InterruptedException e1) {
                                    throw new RuntimeException(e1);
                                }
                                tryCount++;
                            }
                        }
                        // otherwise just pass the original response on
                        return response;
                    }
                })
                .build();
    }
}
