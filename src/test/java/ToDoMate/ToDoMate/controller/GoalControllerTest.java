package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.GoalForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        member.setId("dasol199");
        member.setPassword("0723");
        member.setNickname("dori");
        member.setName("강다솔");
        member.setEmail("dasol199@naver.com");

        session.setAttribute("member", member);
    }

    @Test
    public void 목표페이지로이동() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        List<String> result = Arrays.asList();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/editGoal")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표추가() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/addGoal")
                .session(session)
        .param("color", "파랑")
        .param("title", "공부")
        .param("goalKey", "1111"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표수정() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/addGoal")
                .session(session)
                .param("color", "검정")
                .param("title", "게임")
                .param("goalKey", "1111"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표삭제() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/deleteGoal")
                .session(session)
                .param("goalKey", "1111"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}