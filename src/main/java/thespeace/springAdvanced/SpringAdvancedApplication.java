package thespeace.springAdvanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import thespeace.springAdvanced.proxy.config.AppV1Config;
import thespeace.springAdvanced.proxy.config.AppV2Config;
import thespeace.springAdvanced.proxy.config.v1_proxy.ConcreteProxyConfig;
import thespeace.springAdvanced.proxy.config.v1_proxy.InterfaceProxyConfig;
import thespeace.springAdvanced.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import thespeace.springAdvanced.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import thespeace.springAdvanced.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import thespeace.springAdvanced.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;
import thespeace.springAdvanced.proxy.config.v4_postprocessor.BeanPostProcessorConfig;
import thespeace.springAdvanced.proxy.config.v5_autoproxy.AutoProxyConfig;
import thespeace.springAdvanced.trace.logtrace.LogTrace;
import thespeace.springAdvanced.trace.logtrace.ThreadLocalLogTrace;

//@Import(AppV1Config.class) //스프링 빈 등록.
//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicProxyFilterConfig.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(ProxyFactoryConfigV2.class)
//@Import(BeanPostProcessorConfig.class)
@Import(AutoProxyConfig.class)
//컴포넌트 스캔에 의해 hello.proxy.config 위치의 설정 파일들이 스프링 빈으로 자동 등록 되지 않도록 컴포넌스 스캔의 시작 위치를 지정.
@SpringBootApplication(scanBasePackages = "thespeace.springAdvanced.proxy.app") //주의.
public class SpringAdvancedApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAdvancedApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}
}
