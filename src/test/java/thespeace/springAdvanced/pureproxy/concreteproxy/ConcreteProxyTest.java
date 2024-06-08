package thespeace.springAdvanced.pureproxy.concreteproxy;

import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.concreteproxy.code.ConcreteClient;
import thespeace.springAdvanced.pureproxy.concreteproxy.code.ConcreteLogic;
import thespeace.springAdvanced.pureproxy.concreteproxy.code.TimeProxy;

public class ConcreteProxyTest {

    /**
     * <h2>구체 클래스 기반 프록시 도입 전</h2>
     * 클래스 의존 관계 : Client -> ConcreteLogic
     * 런타임 객체 의존 관계 : client -( operation() )-> concreteLogic
     */
    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    /**
     * <h2>구체 클래스 기반 프록시 도입</h2>
     * 지금까지 인터페이스를 기반으로 프록시를 도입했다.<br>
     * 그런데 자바의 다형성은 인터페이스를 구현하든, 아니면 클래스를 상속하든 상위 타입만 맞으면 다형성이 적용된다.<br>
     * 쉽게 이야기해서 인터페이스가 없어도 프록시를 만들수 있다는 뜻이다.
     * 그래서 이번에는 인터페이스가 아니라 클래스를 기반으로 상속을 받아서 프록시를 만들어보자.<p><p>
     *
     * 클래스 의존 관계 : Client -> ConcreteLogic <-(상속)- TimeProxy
     * 런타임 객체 의존 관계 : client -( operation() )-> timeProxy -( operation() )-> concreteLogic<p><p>
     *
     * ConcreteLogic에 할당할 수 있는 객체(+다형성)
     * <ul>
     *     <li>ConcreteLogic = concreteLogic (본인과 같은 타입을 할당)</li>
     *     <li>ConcreteLogic = timeProxy (자식 타입을 할당)</li>
     * </ul>
     */
    @Test
    void addProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();
    }
}
