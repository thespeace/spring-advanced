package thespeace.springAdvanced.pureproxy.concreteproxy;

import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.concreteproxy.code.ConcreteClient;
import thespeace.springAdvanced.pureproxy.concreteproxy.code.ConcreteLogic;

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
}
