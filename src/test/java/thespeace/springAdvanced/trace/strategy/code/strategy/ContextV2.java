package thespeace.springAdvanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>전략을 파라미터로 전달 받는 방식</h1>
 * 이전에는 Context 의 필드에 Strategy 를 주입해서 사용했다.<br>
 * 이번에는 전략을 필드로 가지지 않고, 실행할 때 직접 파라미터로 전달해서 사용해보자.<br>
 * 대신에 전략을 메소드가 호출될 때 마다 항상 파라미터로 전달 받는다.
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call(); //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
