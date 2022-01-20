package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.MemberRepository;
import ToDoMate.ToDoMate.service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
class FriendControllerTest {

    private Member member=new Member();
    protected MockHttpSession session;
    protected MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    FriendService friendService;

    @BeforeEach
    public void 테스트전_회원세션저장() throws Exception {
        //Member member = new Member();
        member.setId("dasol");
        member.setPassword("0723");
        member.setNickname("dyori");
        member.setName("kang");
        member.setEmail("dasol199@naver.com");

        session = new MockHttpSession();
        session.setAttribute("member", member);

        request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

/*
    @AfterEach
    public void 테스트후세션클리어() {
        session.clearAttributes();
        session=null;
    }*/


    @Test
    public void 친구목록확인() throws Exception {
        List<String> check = friendService.friendList(member.getId()).get().getFriend();
        Assertions.assertThat(check).contains("aaa", "bbb", "ccc");
    }


    @Test
    public void 팔로워목록확인() throws Exception {
        List<String> check = friendService.friendList(member.getId()).get().getFollower();
        Assertions.assertThat(check).contains("qwer", "asdf", "zxcv");
    }

}