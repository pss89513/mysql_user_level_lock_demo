package woowa.demo.test;

import woowa.demo.controller.UserController;
import woowa.demo.util.RequestUtil;

public class WithRedisRequest {
    private static final int THREAD_COUNT = 300;

    public static void main(String[] args) {
        RequestUtil.concurrentPost(THREAD_COUNT, UserController.ADD_CARD_URI_WITH_REDIS, 1L);
    }
}
