package thespeace.springAdvanced.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <h1>CGLIB 프록시의 실행 로직 정의</h1>
 * <ul>
 *     <li>Object target : 프록시가 호출할 실제 대상</li>
 *     <li>proxy.invoke(target, args) : 실제 대상을 동적으로 호출한다.
 *         <ul>
 *             <li>참고로 method 를 사용해도 되지만, CGLIB는 성능상 MethodProxy proxy 를 사용하는 것을 권장한다.</li>
 *         </ul>
 *     </li>
 * </ul>
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
