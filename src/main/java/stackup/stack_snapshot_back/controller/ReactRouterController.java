package stackup.stack_snapshot_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactRouterController {

    @GetMapping({"/", "/picture/**", "/test"})
    public String forwardReactRoutes() {
        return "forward:/index.html";
    }
}
