package thespeace.springAdvanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import thespeace.springAdvanced.proxy.config.AppV1Config;

@Import(AppV1Config.class) //스프링 빈 등록.
//컴포넌트 스캔에 의해 hello.proxy.config 위치의 설정 파일들이 스프링 빈으로 자동 등록 되지 않도록 컴포넌스 스캔의 시작 위치를 지정.
@SpringBootApplication(scanBasePackages = "thespeace.springAdvanced.proxy.app") //주의.
public class SpringAdvancedApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAdvancedApplication.class, args);
	}

}
