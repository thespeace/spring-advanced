package thespeace.springAdvanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.strategy.code.template.Callback;
import thespeace.springAdvanced.trace.strategy.code.template.TimeLogTemplate;

/**
 * <h1>템플릿 콜백 패턴 - 예제</h1>
 * 템플릿 콜백 패턴을 구현해보자. ContextV2 와 내용이 같고 이름만 다르므로 크게 어려움은 없을 것이다.
 * <ul>
 *     <li>`Context` -> `Template`</li>
 *     <li>`Strategy` -> `Callback`</li>
 * </ul>
 *
 * 별도의 클래스를 만들어서 전달해도 되지만, 콜백을 사용할 경우 익명 내부 클래스나 람다를 사용하는 것이 편리하다.<br>
 * 물론 여러곳에서 함께 사용되는 경우 재사용을 위해 콜백을 별도의 클래스로 만들어도 된다.
 */
@Slf4j
public class TemplateCallbackTest {

    /**
     * <h2>템플릿 콜백 패턴 - 익명 내부 클래스</h2>
     */
    @Test
    void callbackV1() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * <h2>템플릿 콜백 패턴 - 람다</h2>
     */
    @Test
    void callbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비즈니스 로직1 실행"));
        template.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
