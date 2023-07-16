package by.nortin.restjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

    @GetMapping("/security")
    public String getSecurityInfo() {
        return "Security";
    }

    @GetMapping("/unsecurity")
    public String getUnSecurityInfo() {
        return "UnSecurity";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "Admin page";
    }
}
