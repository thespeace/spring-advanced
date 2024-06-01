package thespeace.springAdvanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thespeace.springAdvanced.trace.logtrace.FieldLogTrace;
import thespeace.springAdvanced.trace.logtrace.LogTrace;
import thespeace.springAdvanced.trace.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        //동시성 문제가 있는 FieldLogTrace대신에 문제를 해결한 ThreadLocalLogTrace를 스프링 빈으로 등록.
//        return new FieldLogTrace();
        return new ThreadLocalLogTrace();
    }
}
