package thespeace.springAdvanced.trace.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.threadlocal.code.FieldService;

/**
 * <h1>동시성 문제</h1>
 * 여러 쓰레드가 동시에 같은 인스턴스의 필드 값을 변경하면서 발생하는 문제를 동시성 문제라 한다.<br>
 * 이런 동시성 문제는 여러 쓰레드가 같은 인스턴스의 필드에 접근해야 하기 때문에 트래픽이 적은 상황에서는
 * 확률상 잘 나타나지 않고, 트래픽이 점점 많아질 수 록 자주 발생한다.<br>
 * 특히 스프링 빈 처럼 싱글톤 객체의 필드를 변경하며 사용할 때 이러한 동시성 문제를 조심해야 한다.<p><p>
 *
 * 그렇다면 지금처럼 싱글톤 객체의 필드를 사용하면서 동시성 문제를 해결하려면 어떻게 해야할까?
 * 다시 파라미터를 전달하는 방식으로 돌아가야 할까? 이럴 때 사용하는 것이 바로 `쓰레드 로컬`이다.
 *
 * @reference 이런 동시성 문제는 지역 변수에서는 발생하지 않는다. 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
 *            동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용 필드에
 *            접근할 때 발생한다.<br>
 *            동시성 문제는 값을 읽기만 하면 발생하지 않는다. 어디선가 값을 변경하기 때문에 발생한다
 */
@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    /**
     * <h2>동시성 문제 - 예제 코드</h2>
     * sleep(100) 을 설정해서 thread-A 의 작업이 끝나기 전에 thread-B 가 실행되도록 해보자.<br>
     * 참고로 FieldService.logic() 메서드는 내부에 sleep(1000) 으로 1초의 지연이 있다.
     * 따라서 1초 이후에 호출하면 순서대로 실행할 수 있다.
     * 다음에 설정할 100(ms)는 0.1초이기 때문에 thread-A 의 작업이 끝나기 전에 thread-B 가 실행된다.<p><p>
     *
     * 결과적으로 Thread-A 입장에서는 저장한 데이터와 조회한 데이터가 다른 문제가 발생한다.
     */
    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start(); //A 실행.
//        sleep(2000); //동시성 문제 발생 X.
        sleep(100); //동시성 문제 발생 O.
        threadB.start(); //B 실행.

        sleep(2000); //메인 쓰레드 종료 대기.
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
