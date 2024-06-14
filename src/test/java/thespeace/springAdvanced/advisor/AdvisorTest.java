package thespeace.springAdvanced.advisor;

import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import thespeace.springAdvanced.proxy.common.advice.TimeAdvice;
import thespeace.springAdvanced.proxy.common.service.ServiceImpl;
import thespeace.springAdvanced.proxy.common.service.ServiceInterface;

/**
 * <h1>어드바이저 - 예제 코드 1</h1>
 * 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가지고 있다.<br>
 * 프록시 팩토리를 통해 프록시를 생성할 때 어드바이저를 제공하면 어디에 어떤 기능을 제공할 지 알 수 있다.<p><p>
 */
public class AdvisorTest {

    /**
     * <ul>
     *     <li>new DefaultPointcutAdvisor : Advisor 인터페이스의 가장 일반적인 구현체이다.
     *         생성자를 통해 하나의 포인트컷과 하나의 어드바이스를 넣어주면 된다.
     *         어드바이저는 하나의 포인트컷과 하나의 어드바이스로 구성된다.</li>
     *     <li>Pointcut.TRUE : 항상 true 를 반환하는 포인트컷이다. 이후에 직접 포인트컷을 구현해볼 것이다. </li>
     *     <li>new TimeAdvice() : 앞서 개발한 TimeAdvice 어드바이스를 제공한다.</li>
     *     <li>proxyFactory.addAdvisor(advisor) : 프록시 팩토리에 적용할 어드바이저를 지정한다.
     *         어드바이저는 내부에 포인트컷과 어드바이스를 모두 가지고 있다.
     *         따라서 어디에 어떤 부가 기능을 적용해야 할지 어드바이저 하나로 알 수 있다.
     *         프록시 팩토리를 사용할 때 어드바이저는 필수이다.</li>
     *     <li>그런데 생각해보면 이전에 분명히 proxyFactory.addAdvice(new TimeAdvice()) 이렇게
     *         어드바이저가 아니라 어드바이스를 바로 적용했다.
     *         이것은 단순히 편의 메서드이고 결과적으로 해당 메서드 내부에서 지금 코드와 똑같은 다음 어드바이저가 생성된다.
     *         DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice())</li>
     * </ul><p><p>
     *
     * <h2>프록시 팩토리 - 어드바이저 관계</h2>
     * `ProxyFactory -> Advisor -> Pointcut, Advice`
     */
    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
