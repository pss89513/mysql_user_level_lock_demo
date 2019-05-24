package woowa.demo.test;

import lombok.extern.slf4j.Slf4j;
import woowa.demo.controller.UserController;
import woowa.demo.util.RequestUtil;

/**
 * 동시에 등록 요청
 */
@Slf4j
public class MultiThreadRequest {

    private static final int THREAD_COUNT = 40;

    public static void main(String[] args) {
        RequestUtil.concurrentPost(THREAD_COUNT, UserController.ADD_CARD_URI, 1L);
    }

}
