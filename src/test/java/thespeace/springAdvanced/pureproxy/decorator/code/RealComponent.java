package thespeace.springAdvanced.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component{

    @Override
    public String operation() {
        log.info("Realcomponent 실행");
        return "data";
    }
}
