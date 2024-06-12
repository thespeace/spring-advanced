package thespeace.springAdvanced.cglib;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import thespeace.springAdvanced.cglib.code.TimeMethodInterceptor;
import thespeace.springAdvanced.proxy.common.service.ConcreteService;

/**
 * <h1>CGLIB - 예제 코드</h1>
 * ConcreteService 는 인터페이스가 없는 구체 클래스이다. 여기에 CGLIB를 사용해서 프록시를 생성해보자.
 * <ul>
 *     <li>Enhancer : CGLIB는 Enhancer 를 사용해서 프록시를 생성한다.</li>
 *     <li>enhancer.setSuperclass(ConcreteService.class) : CGLIB는 구체 클래스를 상속 받아서
 *         프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.</li>
 *     <li>enhancer.setCallback(new TimeMethodInterceptor(target)) : 프록시에 적용할
 *         실행 로직을 할당한다.
 *     </li>
 *     <li>enhancer.create() : 프록시를 생성한다.
 *         앞서 설정한 enhancer.setSuperclass(ConcreteService.class) 에서 지정한 클래스를
 *         상속 받아서 프록시가 만들어진다.</li>
 * </ul>
 *
 * JDK 동적 프록시는 인터페이스를 구현(implement)해서 프록시를 만든다. CGLIB는 구체 클래스를
 * 상속(extends)해서 프록시를 만든다.
 */
@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
}
