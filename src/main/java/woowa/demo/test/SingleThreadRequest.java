package woowa.demo.test;

import lombok.extern.slf4j.Slf4j;
import woowa.demo.controller.UserController;
import woowa.demo.util.RequestUtil;

@Slf4j
public class SingleThreadRequest {

    public static void main(String[] args) {
        Integer count = RequestUtil.post(UserController.ADD_CARD_URI, 1L);
        if (count != null) {
            log.info("count : {}", count);
        }
    }

}
