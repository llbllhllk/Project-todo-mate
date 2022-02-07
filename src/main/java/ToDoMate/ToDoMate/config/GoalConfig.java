package ToDoMate.ToDoMate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoalConfig {
    @Bean
    public GoalService goalService(){
        return new GoalService(goalRepository());
    }

    @Bean
    public GoalRepository goalRepository(){
        return new MemoryGoalRepository();
    }
}
