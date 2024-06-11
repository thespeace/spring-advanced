package thespeace.springAdvanced.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.jdkdynamic.code.*;

import java.lang.reflect.Proxy;

/**
 * <h1>JDK 동적 프록시 사용</h1>
 * <ul>
 *     <li>new TimeInvocationHandler(target) : 동적 프록시에 적용할 핸들러 로직이다.</li>
 *     <li>Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler)
 *         <ul>
 *             <li>동적 프록시는 java.lang.reflect.Proxy 를 통해서 생성할 수 있다.</li>
 *             <li>클래스 로더 정보, 인터페이스, 그리고 핸들러 로직을 넣어주면 된다.
 *                 그러면 해당 인터페이스를 기반으로 동적 프록시를 생성하고 그 결과를 반환한다.</li>
 *         </ul>
 *     </li>
 * </ul>
 */
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
