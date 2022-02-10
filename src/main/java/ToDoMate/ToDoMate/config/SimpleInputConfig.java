package ToDoMate.ToDoMate.config;

import ToDoMate.ToDoMate.repository.MemorySimpleInputRepository;
import ToDoMate.ToDoMate.repository.SimpleInputRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleInputConfig {

    @Bean
    public SimpleInputRepository simpleInputRepository(){
        return new MemorySimpleInputRepository();
    }
}
