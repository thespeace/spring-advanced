package thespeace.springAdvanced.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * <h1>동적 프록시 기술 - 리플렉션</h1>
 * + {@link /docs/15.Dynamic_proxy_technology-Reflection.md}
 */
@Slf4j
public class ReflectionTest {

    /**
     * <h2>Reflection 사용 전</h2>
     * <ul>
     *     <li>공통 로직1과 공통 로직2는 호출하는 메서드만 다르고 전체 코드 흐름이 완전히 같다.
     *         <ul>
     *             <li>먼저 start 로그를 출력한다.</li>
     *             <li>어떤 메서드를 호출한다.</li>
     *             <li>메서드의 호출 결과를 로그로 출력한다.</li>
     *         </ul>
     *     </li>
     *     <li>여기서 공통 로직1과 공통 로직 2를 하나의 메서드로 뽑아서 합칠 수 있을까?</li>
     *     <li>쉬워 보이지만 메서드로 뽑아서 공통화하는 것이 생각보다 어렵다. 왜냐하면 중간에
     *         호출하는 메서드가 다르기 때문이다. </li>
     *     <li>호출하는 메서드인 target.callA() , target.callB() 이 부분만 동적으로
     *         처리할수 있다면 문제를 해결할 수 있을 듯 하다.</li>
     * </ul>
     *
     * 이럴 때 사용하는 기술이 바로 리플렉션이다. 리플렉션은 클래스나 메서드의 메타정보를 사용해서 동적으로 호출하는
     * 메서드를 변경할 수 있다. 바로 리플렉션 사용해보자.
     *
     * @reference : 람다를 사용해서 공통화 하는 것도 가능하다. 여기서는 람다를 사용하기 어려운 상황이라 가정하자.
     *              그리고 리플렉션 학습이 목적이니 리플렉션에 집중하자.
     */
    @Test
    void reflection0() {
        Hello target = new Hello();
        
        //공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); //호출하는 메서드가 다름.
        log.info("result={}", result1);
        //공통 로직1 종료

        //공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); //호출 대상이 다름, 동적 처리 필요.
        log.info("result={}", result2);
        //공통 로직2 종료
    }

    /**
     * <h2>Reflection 추가</h2>
     * {@code methodCallA.invoke(target)} : 획득한 메서드 메타정보로 실제 인스턴스의 메서드를 호출한다.<br>
     * 여기서 methodCallA 는 Hello 클래스의 callA() 이라는 메서드 메타정보이다.
     * methodCallA.invoke(인스턴스) 를 호출하면서 인스턴스를 넘겨주면 해당 인스턴스의 callA() 메서드를 찾아서 실행한다.
     * 여기서는 target 의 callA() 메서드를 호출한다.<p><p>
     *
     * 그런데 target.callA() 나 target.callB() 메서드를 직접 호출하면 되지 이렇게 메서드 정보를 획득해서 메서드를
     * 호출하면 어떤 효과가 있을까? 여기서 중요한 핵심은 클래스나 메서드 정보를 동적으로 변경할 수 있다는 점이다.<p><p>
     *
     * 기존의 callA() , callB() 메서드를 직접 호출하는 부분이 Method 로 대체되었다. 덕분에 이제 공통 로직을 만들수 있게 되었다.
     */
    @Test
    void reflection1() throws Exception {
        //클래스 정보, 클래스 메타정보를 획득한다. 참고로 내부 클래스는 구분을 위해 $ 를 사용한다.
        Class classHello = Class.forName("thespeace.springAdvanced.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        //callA 메서드 정보, 해당 클래스의 call 메서드 메타정보를 획득한다.
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        //callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    /**
     * <h2>Reflection, 공통 처리 로직 추가</h2>
     *
     * <ul>
     *     <li>dynamicCall(Method method, Object target)
     *         <ul>
     *             <li>공통 로직1, 공통 로직2를 한번에 처리할 수 있는 통합된 공통 처리 로직이다.</li>
     *             <li>Method method : 첫 번째 파라미터는 호출할 메서드 정보가 넘어온다. 이것이 핵심이다.
     *                 기존에는 메서드 이름을 직접 호출했지만, 이제는 Method 라는 메타정보를 통해서 호출할
     *                 메서드 정보가 동적으로 제공된다.</li>
     *             <li>Object target : 실제 실행할 인스턴스 정보가 넘어온다. 타입이 Object 라는 것은 어떠한
     *                 인스턴스도 받을 수 있다는 뜻이다. 물론 method.invoke(target) 를 사용할 때 호출할 클래스와
     *                 메서드 정보가 서로 다르면 예외가 발생한다.</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    @Test
    void reflection2() throws Exception {
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
