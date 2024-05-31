package thespeace.springAdvanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>ThreadLocal</h1>
 * <ul>
 *     <li>값 저장: ThreadLocal.set(xxx)</li>
 *     <li>값 조회: ThreadLocal.get()</li>
 *     <li>값 제거: ThreadLocal.remove()</li>
 * </ul>
 * 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 ThreadLocal.remove() 를 호출해서 쓰레드 로컬에
 * 저장된 값을 제거해주어야 한다.
 */
@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
