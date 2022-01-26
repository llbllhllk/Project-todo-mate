package ToDoMate.ToDoMate.config;

import ToDoMate.ToDoMate.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindConfig {

    @Bean
    public AuthenticationService authenticationService(){
        return new AuthenticationService();
    }
}
