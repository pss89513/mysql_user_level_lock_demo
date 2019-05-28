package woowa.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * JdbcTemplate 을 이용하여 USER LEVEL LOCK 을 사용하는 클래스
 */
@Slf4j
public class UserLevelLockWithJdbcTemplate {

    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";
    private static final String EXCEPTION_MESSAGE = "LOCK 을 수행하는 중에 오류가 발생하였습니다.";
    private static final ResultSetExtractor<Integer> RESULT_SET_EXTRACTOR = rs -> {
        if (rs.next()) {
            return rs.getInt(1);
        }
        return null;
    };

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserLevelLockWithJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    //    @Transactional
    public <T> T executeWithLock(String userLockName,
                                 int timeoutSeconds,
                                 Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
//            getLockAndLoggingConnection(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
//            releaseLockAndLoggingConnection(userLockName);
        }
    }

    /**
     * NamedParameterJdbcTemplate 사용
     */
    private void getLock(String userLockName,
                         int timeoutSeconds) {

        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);
        params.put("timeoutSeconds", timeoutSeconds);
        log.info("GetLock!! userLockName : [{}], timeoutSeconds : [{}]", userLockName, timeoutSeconds);
        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result, userLockName, "GetLock");
    }

    /**
     * GET_LOCK 을 실행할때 얻어온 Connection 을 로그에 남겨준다.
     */
    private void getLockAndLoggingConnection(String userLockName, int timeoutSeconds) {
        Integer result = namedParameterJdbcTemplate.getJdbcTemplate().query(con -> {
                log.info("GetLock!! connection : [{}], userLockName : [{}], timeoutSeconds : [{}]", con, userLockName, timeoutSeconds);
                PreparedStatement preparedStatement = con.prepareStatement("SELECT GET_LOCK(?, ?)");
                preparedStatement.setString(1, userLockName);
                preparedStatement.setInt(2, timeoutSeconds);
                return preparedStatement;
            }, RESULT_SET_EXTRACTOR
        );
        checkResult(result, userLockName, "GetLock");
    }

    /**
     * NamedParameterJdbcTemplate 사용
     */
    private void releaseLock(String userLockName) {

        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);

        log.info("ReleaseLock!! userLockName : [{}]", userLockName);
        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result, userLockName, "ReleaseLock");
    }

    /**
     * RELEASE_LOCK 을 실행할 때 얻어온 Connection 을 로그에 남긴다.
     */
    private void releaseLockAndLoggingConnection(String userLockName) {
        Integer result = namedParameterJdbcTemplate.getJdbcTemplate().query(con -> {
                    log.info("ReleaseLock!! connection : [{}], userLockName : [{}]", con, userLockName);
                    PreparedStatement preparedStatement = con.prepareStatement("SELECT RELEASE_LOCK(?)");
                    preparedStatement.setString(1, userLockName);
                    return preparedStatement;
                }, RESULT_SET_EXTRACTOR
        );
        checkResult(result, userLockName, "ReleaseLock");
    }

    private void checkResult(Integer result,
                             String userLockName,
                             String type) {
        if (result == null) {
            log.error("USER LEVEL LOCK 쿼리 결과 값이 없습니다. type = [{}], userLockName : [{}]", type, userLockName);
            throw new RuntimeException(EXCEPTION_MESSAGE);
        }
        if (result != 1) {
            log.error("USER LEVEL LOCK 쿼리 결과 값이 1이 아닙니다. type = [{}], result : [{}] userLockName : [{}]", type, result, userLockName);
            throw new RuntimeException(EXCEPTION_MESSAGE);
        }
    }

}
