package thespeace.springAdvanced.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.decorator.code.Component;
import thespeace.springAdvanced.pureproxy.decorator.code.DecoratorPatternClient;
import thespeace.springAdvanced.pureproxy.decorator.code.MessageDecorator;
import thespeace.springAdvanced.pureproxy.decorator.code.RealComponent;

@Slf4j
public class DecoratorPatternTest {

    /**
     * <h1>데코레이터 패턴을 도입하기 전</h1>
     * 클래스 의존 관계 : `Client -> Component <- RealComponent`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> realComponent`<p><p>
     */
    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    /**
     * <h1>데코레이터 패턴(응답 값을 꾸며주는) 도입</h1>
     * 프록시를 활용해서 부가 기능을 추가해보자.<br>
     * 이렇게 프록시로 부가 기능을 추가하는 것을 데코레이터 패턴이라 한다.<p><p>
     *
     * 클래스 의존 관계 : `Client -> Component <- RealComponent and MessageDecorator`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> messageDecorator -( operation() )-> realComponent`<p><p>
     */
    @Test
    void decorator1() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }
}
