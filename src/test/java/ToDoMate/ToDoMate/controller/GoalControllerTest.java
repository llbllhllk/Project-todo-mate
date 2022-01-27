package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.GoalForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class GoalControllerTest {

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

    @Test
    public void 목표추가() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        GoalForm goalForm = new GoalForm();
        goalForm.setGoal("컨트롤러만들기");
        mockMvc.perform(MockMvcRequestBuilders.post("/goalPlusTest")
                .session(session)
                .flashAttr("goalForm", goalForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}