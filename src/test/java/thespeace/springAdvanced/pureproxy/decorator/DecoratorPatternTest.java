package thespeace.springAdvanced.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.decorator.code.DecoratorPatternClient;
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
}
