package hello.advanced;

import hello.advanced.logtrace.FieldLogTrace;
import hello.advanced.logtrace.LogTrace;
import hello.advanced.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
