package thespeace.springAdvanced.proxy.app.v1;

import org.springframework.web.bind.annotation.*;

/**
 * <h1>V1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록</h1>
 * 실무에서는 스프링 빈으로 등록할 클래스는 인터페이스가 있는 경우도 있고 없는 경우도 있다.<br>
 * 그리고 스프링 빈을 수동으로 직접 등록하는 경우도 있고, 컴포넌트 스캔으로 자동으로 등록하는
 * 경우도 있다. 이런 다양한 케이스에 프록시를 어떻게 적용하는지 알아보기 위해 다양한 예제를 준비해보자.<p><p>
 *
 * Controller , Service , Repository 에 인터페이스를 도입하고, 스프링 빈으로 수동 등록.
 */
@RestController //스프링은 @Controller, @RestController가 있어야 스프링 컨트롤러로 인식
public interface OrderControllerV1 {

    /**
     * <h2>LogTrace 를 적용할 대상</h2>
     * `@RequestParam("itemId") String itemId` : 인터페이스에는 @RequestParam("itemId") 의 값을
     * 생략하면 itemId 단어를 컴파일 이후 자바 버전에 따라 인식하지 못할 수 있다. 인터페이스에서는 꼭 넣어주자.
     * 클래스에는 생략해도 대부분 잘 지원된다.
     */
    @GetMapping("/proxy/v1/request")
    String request(@RequestParam("itemId") String itemId);

    /**
     * <h2>단순히 LogTrace 를 적용하지 않을 대상</h2>
     */
    @GetMapping("/proxy/v1/no-log")
    String noLog();
}
