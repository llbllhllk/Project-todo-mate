package ToDoMate.ToDoMate.config;

import ToDoMate.ToDoMate.repository.MemberRepository;
import ToDoMate.ToDoMate.repository.MemoryMemberRepository;
import ToDoMate.ToDoMate.service.AuthenticationService;
import ToDoMate.ToDoMate.service.MemberService;
import ToDoMate.ToDoMate.validator.Validate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    @Bean
    public Validate validate(){
        return new Validate(memberService());
    }

}