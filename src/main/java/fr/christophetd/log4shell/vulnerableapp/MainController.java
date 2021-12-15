package fr.christophetd.log4shell.vulnerableapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.*;


@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @GetMapping("/")
//    public String index(@RequestHeader("X-Api-Version") String apiVersion) {
//        logger.info("Received a request for API version " + apiVersion);
//        return "Hello, world!";
//    }


    public String extractCookie(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        logger.info("Received a request with User-Agent: " + userAgent);

        String apiVersion = req.getHeader("X-Api-Version");
        logger.info("Received a request for API version: " + apiVersion);

        for (Cookie c : req.getCookies()) {
            logger.info("Received a request with Cookie: " + c.getValue());
        }

    return "Cookie Exploited";
}

    


}