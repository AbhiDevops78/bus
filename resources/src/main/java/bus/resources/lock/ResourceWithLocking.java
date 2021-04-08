package bus.resources.lock;

import io.github.harishb2k.easy.lock.DistributedLock;
import io.github.harishb2k.easy.lock.IDistributedLock;
import io.github.harishb2k.easy.lock.IDistributedLockIdResolver;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ResourceWithLocking {
    private static final AtomicLong COUNTER = new AtomicLong();

    @DistributedLock(lockIdResolver = InternalDistributedLockIdResolver.class)
    public Map<String, Object> methodWhichShouldBeLocked(String someId) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {
        }
        log.trace("Called method methodWhichShouldBeLocked - id={}", someId);

        Map<String, Object> result = new HashMap<>();
        result.put("counter", COUNTER.incrementAndGet());
        result.put("id", someId);
        return result;
    }

    @Singleton
    public static class InternalDistributedLockIdResolver implements IDistributedLockIdResolver {
        @Override
        public IDistributedLock.LockRequest createLockRequest(MethodInvocation invocation, Object[] arguments) {
            return IDistributedLock.LockRequest.builder()
                    .lockId(arguments[0].toString())
                    .build();
        }
    }
}