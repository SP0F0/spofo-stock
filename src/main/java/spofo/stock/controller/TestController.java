package spofo.stock.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    @CrossOrigin("*")
    public String testResult() {
        return "/test - 주식 서버입니다.";
    }

    @GetMapping("/test/callStock")
    @CrossOrigin("*")
    public String testCallStock() {
        return "/test/callStock - 주식 서버 호출 성공했습니다.";
    }
}
