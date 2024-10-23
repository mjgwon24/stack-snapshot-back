package stackup.stack_snapshot_back.restController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestRestController {

    @CrossOrigin(origins = "${server.cross-origin-url}")
    @GetMapping("/test")
    public String testHome() {
        log.info("test api is called");
        return "this is test home";
    }
}
