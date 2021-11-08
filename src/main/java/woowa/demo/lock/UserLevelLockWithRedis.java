package woowa.demo.lock;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class UserLevelLockWithRedis {
    private final RedissonClient redissonClient;

    public UserLevelLockWithRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> T executeWithLock(String userLockName,
                                 int timeoutSeconds,
                                 Supplier<T> supplier) {
        RLock rLock = redissonClient.getLock(userLockName);

        try {
            if (!rLock.tryLock(timeoutSeconds, timeoutSeconds, TimeUnit.SECONDS)) {
                throw new RuntimeException("lock을 획득하는데 실패했습니다.");
            }
            return supplier.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("작업 중 문제가 발생했습니다.");
        } finally {
            rLock.unlock();
        }
    }
}
