package thespeace.springAdvanced.proxyfactory;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import thespeace.springAdvanced.proxy.common.advice.TimeAdvice;
import thespeace.springAdvanced.proxy.common.service.ConcreteService;
import thespeace.springAdvanced.proxy.common.service.ServiceImpl;
import thespeace.springAdvanced.proxy.common.service.ServiceInterface;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>프록시 팩토리의 기술 선택 방법</h2>
 * <ul>
 *     <li>대상에 인터페이스가 있으면: JDK 동적 프록시, 인터페이스 기반 프록시</li>
 *     <li>대상에 인터페이스가 없으면: CGLIB, 구체 클래스 기반 프록시</li>
 *     <li>proxyTargetClass=true : CGLIB, 구체 클래스 기반 프록시, 인터페이스 여부와 상관없음</li>
 * </ul><p><p>
 *
 * <h2>정리</h2>
 * <ul>
 *     <li>프록시 팩토리의 서비스 추상화 덕분에 구체적인 CGLIB, JDK 동적 프록시 기술에 의존하지 않고, 매우 편리하게
 *         동적 프록시를 생성할 수 있다.</li>
 *     <li>프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있었다. 이것은
 *         프록시 팩토리가 내부에서 JDK 동적 프록시인 경우 InvocationHandler 가 Advice 를 호출하도록 개발해두고,
 *         CGLIB인 경우 MethodInterceptor 가 Advice 를 호출하도록 기능을 개발해두었기 때문이다.</li>
 * </ul><p><p>
 *
 * @reference 스프링 부트는 AOP를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다.
 *            따라서 인터페이스가 있어도 항상 CGLIB를 사용해서 구체 클래스를 기반으로 프록시를 생성한다.
 *            자세한 이유는 나중에 알아보자.
 */
@Slf4j
public class ProxyFactoryTest {

    /**
     * <h2>인터페이스에 프록시 적용</h2>
     * <ul>
     *     <li>new ProxyFactory(target) : 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께 넘겨준다.
     *         프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다. 만약 이 인스턴스에 인터페이스가 있다면
     *         JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면 CGLIB를 통해서
     *         동적 프록시를 생성한다. 여기서는 target 이 new ServiceImpl() 의 인스턴스이기 때문에
     *         ServiceInterface 인터페이스가 있다. 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를 생성한다.</li>
     *     <li>proxyFactory.addAdvice(new TimeAdvice()) : 프록시 팩토리를 통해서 만든 프록시가 사용할 부가기능
     *         로직을 설정한다. JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB가 제공하는
     *         MethodInterceptor 의 개념과 유사하다. 이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스( Advice )라 한다.
     *         번역하면 조언을 해준다고 생각하면 된다.</li>
     *     <li>proxyFactory.getProxy() : 프록시 객체를 생성하고 그 결과를 받는다.</li>
     * </ul>
     */
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); //클래스 정보로 동적 프록시 적용 여부 직접 확인.

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); //프록시 팩토리를 통해서 프록시가 생성되면 JDK 동적 프록시나, CGLIB 모두 참이다.
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue(); //프록시 팩토리를 통해서 프록시가 생성되고, JDK 동적 프록시인 경우 참.
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse(); //프록시 팩토리를 통해서 프록시가 생성되고, CGLIB 동적 프록시인 경우 경우 참.
    }

    /**
     * <h2>구체 클래스에 프록시를 적용</h2>
     */
    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //인터페이스가 있지만 구체클래스를 상속받아서 사용.(인터페이스가 있어도 강제로 CGLIB를 사용)
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
