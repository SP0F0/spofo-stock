package spofo.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testResult() {
        return "/test - 주식 서버입니다.";
    }

    @GetMapping("/test/callStock")
    public String testCallStock() {
        return "/test/callStock - 주식 서버 호출 성공했습니다.";
    }

    @GetMapping("/test/callPortfolio")
    public String testCallPortfolio() {
        RestClient restClient = RestClient.create();

        String resultByPortfolio = restClient.get()
                .uri("http://portfolio.spofo.net:8080/test")
                .retrieve()
                .body(String.class);

        return "주식 서버가 포트폴리오 서버를 호출했습니다. : " + resultByPortfolio;
    }

    @GetMapping("/test/callAuth")
    public String testCallAuth() {
        RestClient restClient = RestClient.create();

        String resultByAuth = restClient.get()
                .uri("http://auth.spofo.net:8080/test")
                .retrieve()
                .body(String.class);

        return "주식 서버가 인증 서버를 호출했습니다. : " + resultByAuth;
    }
}
