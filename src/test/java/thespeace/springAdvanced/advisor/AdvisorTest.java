package thespeace.springAdvanced.advisor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import thespeace.springAdvanced.proxy.common.advice.TimeAdvice;
import thespeace.springAdvanced.proxy.common.service.ServiceImpl;
import thespeace.springAdvanced.proxy.common.service.ServiceInterface;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    /**
     * <h2>어드바이저 - 예제 코드 1</h2>
     * 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가지고 있다.<br>
     * 프록시 팩토리를 통해 프록시를 생성할 때 어드바이저를 제공하면 어디에 어떤 기능을 제공할 지 알 수 있다.<p>
     *
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

    /**
     * <h2>직접 만든 포인트컷 - 예제 코드 2</h2>
     *
     * <h3>포인트컷이 적용되어야 하는 경우(그림으로 정리)</h3>
     * <ol>
     *     <li>클라이언트가 프록시의 save() 를 호출한다.</li>
     *     <li>포인트컷에 Service 클래스의 save() 메서드에 어드바이스를 적용해도 될지 물어본다.</li>
     *     <li>포인트컷이 true 를 반환한다. 따라서 어드바이스를 호출해서 부가 기능을 적용한다.</li>
     *     <li>이후 실제 인스턴스의 save() 를 호출한다.</li>
     * </ol>
     *
     * <h3>포인트컷이 적용되지 않는 경우(그림으로 정리)</h3>
     * <ol>
     *     <li>클라이언트가 프록시의 find() 를 호출한다.</li>
     *     <li>포인트컷에 Service 클래스의 find() 메서드에 어드바이스를 적용해도 될지 물어본다.</li>
     *     <li>포인트컷이 false 를 반환한다. 따라서 어드바이스를 호출하지 않고, 부가 기능도 적용되지 않는다.</li>
     *     <li>실제 인스턴스를 호출한다.</li>
     * </ol>
     */
    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    /**
     * <h2>직접 구현한 포인트컷</h2>
     * 현재 메서드 기준으로 로직을 적용하면 된다.
     * 클래스 필터는 항상 true 를 반환하도록 했고, 메서드 비교 기능은 직접 구현한 MyMethodMatcher 를 사용한다.
     *
     * @see Pointcut org.springframework.aop.Pointcut 인터페이스를 구현
     * @see MyMethodMatcher 직접 구현한 MethodMatcher
     */
    static class MyPointcut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    /**
     * <h2>직접 구현한 MethodMatcher</h2>
     * <ul>
     *     <li>isRuntime() , matches(... args) : isRuntime() 이 값이 참이면 matches(... args) 메서드가 대신 호출된다.
     *                                           동적으로 넘어오는 매개변수를 판단 로직으로 사용할 수 있다.
     *         <ul>
     *             <li>isRuntime() 이 false 인 경우 클래스의 정적 정보만 사용하기 때문에 스프링이 내부에서 캐싱을 통해
     *                 성능 향상이 가능하지만, isRuntime() 이 true 인 경우 매개변수가 동적으로 변경된다고 가정하기
     *                 때문에 캐싱을 하지 않는다.</li>
     *             <li>크게 중요한 부분은 아니니 참고만 하고 넘어가자.</li>
     *         </ul>
     *     </li>
     * </ul>
     * @see MethodMatcher org.springframework.aop.MethodMatcher 인터페이스를 구현
     */
    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        /**
         * 해당 메서드에 method, targetClass 정보가 넘어온다.<br>
         * 이 정보로 어드바이스를 적용할지 적용하지 않을지 판단할 수 있다.
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName); //"save"인 경우 true 반환하도록 판단 로직을 적용.
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * <h2>스프링이 제공하는 포인트컷 - 예제 코드 3</h2>
     * <h3>스프링은 무수히 많은 포인트컷을 제공한다.</h3>
     * <ul>
     *     <li>NameMatchMethodPointcut : 메서드 이름을 기반으로 매칭한다. 내부에서는 PatternMatchUtils 를
     *         사용한다. 예) *xxx* 허용</li>
     *     <li>JdkRegexpMethodPointcut : JDK 정규 표현식을 기반으로 포인트컷을 매칭한다.</li>
     *     <li>TruePointcut : 항상 참을 반환한다.</li>
     *     <li>AnnotationMatchingPointcut : 애노테이션으로 매칭한다.</li>
     *     <li>AspectJExpressionPointcut : aspectJ 표현식으로 매칭한다.</li>
     * </ul><p><p>
     *
     * <h2>가장 중요한 것은 aspectJ 표현식</h2>
     * 여기에서 사실 다른 것은 중요하지 않다. 실무에서는 사용하기도 편리하고 기능도 가장 많은
     * aspectJ 표현식을기반으로 사용하는 AspectJExpressionPointcut 을 사용하게 된다.<br>
     * aspectJ 표현식과 사용방법은 중요해서 이후 AOP를 설명할 때 자세히 설명하겠다.<br>
     * 지금은 Pointcut 의 동작 방식과 전체 구조에 집중하자.
     *
     * @see NameMatchMethodPointcut 스프링이 제공하는 NameMatchMethodPointcut 사용
     */
    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
