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
//        Member member = new Member();
//        member.setId("dasol199");
//        member.setPassword("0723");
//        member.setNickname("dori");
//        member.setName("강다솔");
//        member.setEmail("dasol199@naver.com");
        Member member = new Member();
        member.setId("admin");
        member.setPassword("admin");
        member.setNickname("admin");
        member.setName("admin");
        member.setEmail("dlrlxo999@naver.com");

        session.setAttribute("member", member);
    }

    @Test
    public void 목표페이지로이동() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/editGoal")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표추가() throws Exception {
        //전달할 값들
        Integer viewId=1111;
        String color="red";
        String title="game";

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/addGoal?goalKey="+viewId)
                .session(session)
                .param("color",color)
                .param("title", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표수정() throws Exception {
        //전달할 값들
        Integer viewId=1111;
        String color="노랑";
        String title="놀기";

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/addGoal?goalKey="+viewId)
                .session(session)
                .param("color", color)
                .param("title", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 목표삭제() throws Exception {
        //전달할 값
        Integer viewId=1111;

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/deleteGoal?goalKey="+viewId)
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}