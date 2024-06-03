package thespeace.springAdvanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.strategy.code.strategy.ContextV1;
import thespeace.springAdvanced.trace.strategy.code.strategy.Strategy;
import thespeace.springAdvanced.trace.strategy.code.strategy.StrategyLogic1;
import thespeace.springAdvanced.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
public class ContextV1Test {

    @Test
    void StrategyV0() {
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
     * <h2>전략 패턴 사용</h2>
     * 코드를 보면 의존관계 주입을 통해 `ContextV1`에 `Strategy`의 구현체인 `strategyLogic1`를 주입하는 것을
     * 확인할 수 있다. 이렇게해서 `Context`안에 원하는 전략을 주입한다.<br>
     * 이렇게 원하는 모양으로 조립을 완료하고 난 다음에 `context1.execute()`를 호출해서 `context`를 실행한다.
     *
     * @see docs/07.Strategy_Pattern-Example1.md
     */
    @Test
    void strategyV1() {
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * <h2>전략 패턴 익명 내부 클래스 1</h2>
     * 전략 패턴도 익명 내부 클래스를 사용할 수 있다.<br>
     * 실행 결과를 보면 `ContextV1Test$1` , `ContextV1Test$2` 와 같이
     * 익명 내부 클래스가 생성된 것을 확인할 수 있다.
     */
    @Test
    void strategyV2() {
        Strategy strategyLogic1 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("strategyLogic1={}", strategyLogic1.getClass());
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("strategyLogic2={}", strategyLogic2.getClass());
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * <h2>전략 패턴 익명 내부 클래스 2</h2>
     * 익명 내부 클래스를 변수에 담아두지 말고, 생성하면서 바로 ContextV1 에 전달해도 된다.
     */
    @Test
    void strategyV3() {
        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        context2.execute();
    }

    /**
     * <h2>전략 패턴, 람다</h2>
     * 익명 내부 클래스를 자바8부터 제공하는 람다로 변경할 수 있다.<br>
     * 람다로 변경하려면 인터페이스에 메서드가 1개만 있으면 되는데, 여기에서 제공하는
     * Strategy 인터페이스는 메서드가 1개만 있으므로 람다로 사용할 수 있다.
     *
     * @see docs/08.Strategy_Pattern-Example2.md
     */
    @Test
    void strategyV4() {
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
        context2.execute();
    }
}
