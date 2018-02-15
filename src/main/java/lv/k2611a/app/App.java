package lv.k2611a.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lv.k2611a.app.domain.User;
import lv.k2611a.app.domain.UserRepository;

@EnableAutoConfiguration
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

@Configuration
@RefreshScope
@RestController
class MessageRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Value("${message:Hello default}")
    private String message;

    @RequestMapping("/")
    String getMessage() {
        return this.message;
    }

    @RequestMapping("/newUser")
    String getNewUser() {
        User user = new User();
        userRepository.save(user);
        long userId = user.getId();
        return Long.toString(userId);
    }
}
