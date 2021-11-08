package woowa.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

    private static final RestTemplate REST_TEMPLATE = create();

    public static Integer post(String uri,
                               Object... uriVariables) {
        return REST_TEMPLATE.postForObject(uri, null, Integer.class, uriVariables);
    }

    /**
     * post 로 동시 요청한다.
     */
    public static void concurrentPost(int threadCount,
                                      String uri,
                                      Object... uriVariables) {

        Random random = new Random();
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    StopWatch sw = new StopWatch();
                    sw.start();
                    Integer count = post(uri, uriVariables);
//                    if (count != null) {
//                        log.info("response count : {}", count);
//                    }
                    sw.stop();
                    log.info("걸린 시간(ms): {}", sw.getTotalTimeMillis());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private static RestTemplate create() {
        return new RestTemplateBuilder().errorHandler(new ErrorHandler()).build();
    }

    private static class ErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().isError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            String message = StreamUtils.copyToString(response.getBody(), Charset.forName("UTF-8"));
//            log.error(message);
        }

    }

}
