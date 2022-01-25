package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.EmailForm;
import ToDoMate.ToDoMate.form.JoinForm;
import ToDoMate.ToDoMate.form.LoginForm;
import ToDoMate.ToDoMate.service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class LoginControllerTest {
    protected MockHttpSession session=new MockHttpSession();
    protected MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;


    public void setSession() throws Exception {
        Member member = new Member();
        member.setId("dasol");
        member.setPassword("0723");
        member.setNickname("dyori");
        member.setName("kang");
        member.setEmail("dasol199@naver.com");

        session.setAttribute("member", member);
    }


    public JoinForm setJoinForm() throws Exception {
        JoinForm joinForm = new JoinForm();
        joinForm.setId("joinTestId");
        joinForm.setPassword("joinpassword");
        joinForm.setCheckPassword("joinpassword");
        joinForm.setEmail("joinTest@naver.com");
        joinForm.setName("joinTestName");
        joinForm.setNickname("joinTestNickname");
        return joinForm;
    }

    @Test
    public void 회원가입() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                .flashAttr("joinForm", setJoinForm()))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 로그인() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        LoginForm loginForm = new LoginForm();
        loginForm.setId("dasol");
        loginForm.setPassword("0723");
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .flashAttr("loginForm", loginForm))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("main"));
    }

    @Test
    public void 로그아웃() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/logout")
        .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("start"));
    }

    @Test
    public void 계정삭제() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Member member = new Member();
        member.setId("delMem");
        member.setPassword("123");
        member.setNickname("delete");
        member.setName("del");
        member.setEmail("del@naver.com");

        session.setAttribute("member", member);

        mockMvc.perform(MockMvcRequestBuilders.post("/accountWithdraw")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("start"));
    }



}