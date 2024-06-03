package thespeace.springAdvanced.trace.callback;

/**
 * <h1>콜백을 전달하는 인터페이스</h1>
 *
 */
public interface TraceCallback<T> { //<T> 제네릭을 사용, 콜백의 반환 타입을 정의.
    T call();
}