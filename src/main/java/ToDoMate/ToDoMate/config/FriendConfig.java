package ToDoMate.ToDoMate.config;

import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.repository.MemoryFriendRepository;
import ToDoMate.ToDoMate.service.FriendService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendConfig {

    @Bean
    public FriendService friendService(){
        return new FriendService(friendRepository());
    }

    @Bean
    public FriendRepository friendRepository(){
        return new MemoryFriendRepository();
    }

}
