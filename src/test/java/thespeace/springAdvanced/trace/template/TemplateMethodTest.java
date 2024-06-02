package thespeace.springAdvanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.template.code.AbstractTemplate;
import thespeace.springAdvanced.trace.template.code.SubClassLogic1;
import thespeace.springAdvanced.trace.template.code.SubClassLogic2;

/**
 * <h1>템플릿 메서드 패턴 - 예제 1</h1>
 * logic1() , logic2() 를 호출하는 단순한 테스트 코드이다.<br>
 * logic1() 과 logic2() 는 시간을 측정하는 부분과 비즈니스 로직을 실행하는 부분이 함께 존재한다.
 * <ul>
 *     <li>변하는 부분: 비즈니스 로직</li>
 *     <li>변하지 않는 부분: 시간 측정</li>
 * </ul>
 * 이제 템플릿 메서드 패턴을 사용해서 변하는 부분과 변하지 않는 부분을 분리해보자.
 */
@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * <h2>템플릿 메서드 패턴 적용</h2>
     * 템플릿 메서드 패턴은 이렇게 다형성을 사용해서 변하는 부분과 변하지 않는 부분을 분리하는 방법이다.
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    /**
     * <h2>템플릿 메서드 패턴, 익명 내부 클래스 사용</h2>
     * 템플릿 메서드 패턴은 `SubClassLogic1` , `SubClassLogic2` 처럼 클래스를 계속 만들어야 하는 단점이 있다.
     * 익명 내부 클래스를 사용하면 이런 단점을 보완할 수 있다.<br>
     * 익명 내부 클래스를 사용하면 객체 인스턴스를 생성하면서 동시에 생성할 클래스를 상속 받은 자식 클래스를 정의할 수
     * 있다. 이 클래스는 SubClassLogic1 처럼 직접 지정하는 이름이 없고 클래스 내부에 선언되는 클래스여서 익명 내부
     * 클래스라 한다.
     */
    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("클래스 이름1={}", template1.getClass()); //TemplateMethodTest$1(자바가 임의로 만들어주는 익명 내부 클래스 이름)
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("클래스 이름2={}", template2.getClass()); //TemplateMethodTest$2(자바가 임의로 만들어주는 익명 내부 클래스 이름)
        template2.execute();
    }
}
