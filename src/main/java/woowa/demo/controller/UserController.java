package woowa.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import woowa.demo.lock.UserLevelLockFinal;
import woowa.demo.lock.UserLevelLockWithJdbcTemplate;
import woowa.demo.service.UserService;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    public static final String ADD_CARD_URI = "http://localhost:8080/users/{userId}/add-new-card";
    public static final String ADD_CARD_URI_WITH_TEMPLATE = "http://localhost:8080/users/{userId}/add-new-card-with-template";
    public static final String ADD_CARD_URI_FINAL = "http://localhost:8080/users/{userId}/add-new-card-final";

    private static final int LOCK_TIMEOUT_SECONDS = 3;

    private final UserService userService;
    private final UserLevelLockWithJdbcTemplate userLevelLockWithJdbcTemplate;
    private final UserLevelLockFinal userLevelLockFinal;


    public UserController(UserService userService,
                          UserLevelLockWithJdbcTemplate userLevelLockWithJdbcTemplate,
                          UserLevelLockFinal userLevelLockFinal) {
        this.userService = userService;
        this.userLevelLockWithJdbcTemplate = userLevelLockWithJdbcTemplate;
        this.userLevelLockFinal = userLevelLockFinal;
    }

    @PostMapping("/{userId}/add-new-card")
    public int addNewCard(@PathVariable Long userId) {
        return userService.addNewCard(userId);
    }

    @PostMapping("/{userId}/add-new-card-with-template")
    public int addNewCardWithTemplate(@PathVariable Long userId) {
        return userLevelLockWithJdbcTemplate.executeWithLock(
                String.valueOf(userId),
                LOCK_TIMEOUT_SECONDS,
                () -> userService.addNewCard(userId)
        );
    }

    @PostMapping("/{userId}/add-new-card-final")
    public int addNewCardFinal(@PathVariable Long userId) {
        return userLevelLockFinal.executeWithLock(
                String.valueOf(userId),
                LOCK_TIMEOUT_SECONDS,
                () -> userService.addNewCard(userId)
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleError(IllegalStateException exception) {
        String message = exception.getMessage();
        log.error(message);
        return message;
    }

}
