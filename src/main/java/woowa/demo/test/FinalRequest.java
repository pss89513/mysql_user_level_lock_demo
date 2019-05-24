package woowa.demo.test;

import lombok.extern.slf4j.Slf4j;
import woowa.demo.controller.UserController;
import woowa.demo.util.RequestUtil;

/**
 * 최종 버전 UserLevelLock 호출
 */
@Slf4j
public class FinalRequest {

    private static final int THREAD_COUNT = 40;

    public static void main(String[] args) {
        RequestUtil.concurrentPost(THREAD_COUNT, UserController.ADD_CARD_URI_FINAL, 1L);
    }

}
