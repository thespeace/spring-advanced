package thespeace.springAdvanced.trace.strategy.code.template;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>전략 로직</h1>
 * 스프링의 jdbcTemplate 등등은 다 이런방식으로 구현되어 있다.
 */
@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        callback.call(); //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
