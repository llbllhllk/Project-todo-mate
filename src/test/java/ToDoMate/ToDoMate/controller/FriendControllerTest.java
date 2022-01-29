package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.MemberRepository;
import ToDoMate.ToDoMate.service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.*;


@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class FriendControllerTest {

//    private Member member=new Member();
    protected MockHttpSession session=new MockHttpSession();
    protected MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    FriendService friendService;

    @BeforeEach
    public void 테스트전_회원세션저장() throws Exception {
        Member member = new Member();
        member.setId("dasol");
        member.setPassword("0723");
        member.setNickname("dyori");
        member.setName("kang");
        member.setEmail("dasol199@naver.com");

        session.setAttribute("member", member);

//        request = new MockHttpServletRequest();
//        request.setSession(session);
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

/*
    @AfterEach
    public void 테스트후세션클리어() {
        session.clearAttributes();
        session=null;
    }*/

    @Test
    public void 친구추가위한회원검색() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/searchMember")
                .session(session)
                .param("user",""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 친구추가요청() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/requestFriend")
                .session(session)
                .param("followUser","request"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 친구목록확인() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/friendList")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 친구목록에서검색() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/searchFriend")
                .session(session)
                .param("friendName","a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 친구삭제() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/deleteFriend")
                .session(session)
                .param("friendName","qwer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 팔로워목록확인() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/followerList")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 팔로워수락하기() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/acceptFollower")
                .session(session)
                .param("follower", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 팔로워거절하기() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/refuseFollower")
                .session(session)
                .param("follower", "refuse"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void 친구가아닌멤버추가() throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollection = firestore.collection("friend");
        CollectionReference memberCollection = firestore.collection("member");

        for (int i=0;i<3;i++){
            HashMap<String, String> memberMap = new HashMap<>();
            HashMap<String, Object> friendMap = new HashMap<>();
            String random = getRandomString(6);
            memberMap.put("id",random);
            memberMap.put("password",random);
            memberMap.put("name",random);
            memberMap.put("nickname",random);
            memberMap.put("email",random+"@naver.com");

            friendMap.put("friend", Collections.EMPTY_LIST);
            friendMap.put("follower", Collections.EMPTY_LIST);
            friendMap.put("followee", Collections.EMPTY_LIST);

            ApiFuture<WriteResult> future1 = memberCollection.document(random).set(memberMap);
            ApiFuture<WriteResult> future2 = friendCollection.document(random).set(friendMap);
            System.out.println("future1 = " + future1.get().getUpdateTime());
            System.out.println("future2 = " + future2.get().getUpdateTime());
        }
    }

    @Test
    public void 나를팔로워하는멤버추가() throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollection = firestore.collection("friend");
        CollectionReference memberCollection = firestore.collection("member");
        Friend friend = friendCollection.document("admin").get().get().toObject(Friend.class);

        for (int i=0;i<3;i++){
            HashMap<String, String> memberMap = new HashMap<>();
            HashMap<String, Object> memFriendMap = new HashMap<>();
            HashMap<String, Object> ranFriendMap = new HashMap<>();
            String random = getRandomString(6);
            memberMap.put("id",random);
            memberMap.put("password",random);
            memberMap.put("name",random);
            memberMap.put("nickname",random);
            memberMap.put("email",random+"@naver.com");

            List<String> memFollowerList = friend.getFollower();
            ArrayList<String> ranFolloweeList = new ArrayList<>();
            memFollowerList.add(random);
            ranFolloweeList.add("admin");

            memFriendMap.put("friend", friend.getFriend());
            memFriendMap.put("follower", memFollowerList);
            memFriendMap.put("followee", friend.getFollowee());

            ranFriendMap.put("friend", Collections.EMPTY_LIST);
            ranFriendMap.put("follower", Collections.EMPTY_LIST);
            ranFriendMap.put("followee", ranFolloweeList);

            ApiFuture<WriteResult> future1 = memberCollection.document(random).set(memberMap);
            ApiFuture<WriteResult> future2 = friendCollection.document(random).set(ranFriendMap);
            ApiFuture<WriteResult> future3 = friendCollection.document("admin").set(memFriendMap, SetOptions.merge());
            System.out.println("future1 = " + future1.get().getUpdateTime());
            System.out.println("future2 = " + future2.get().getUpdateTime());
            System.out.println("future3 = " + future3.get().getUpdateTime());
        }
    }

    @Test
    public void 내가팔로우하는멤버추가() throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollection = firestore.collection("friend");
        CollectionReference memberCollection = firestore.collection("member");
        Friend friend = friendCollection.document("admin").get().get().toObject(Friend.class);

        for (int i=0;i<3;i++){
            HashMap<String, String> memberMap = new HashMap<>();
            HashMap<String, Object> memFriendMap = new HashMap<>();
            HashMap<String, Object> ranFriendMap = new HashMap<>();
            String random = getRandomString(6);
            memberMap.put("id",random);
            memberMap.put("password",random);
            memberMap.put("name",random);
            memberMap.put("nickname",random);
            memberMap.put("email",random+"@naver.com");

            List<String> memFolloweeList = friend.getFollowee();
            ArrayList<String> ranFollowerList = new ArrayList<>();
            memFolloweeList.add(random);
            ranFollowerList.add("admin");

            memFriendMap.put("friend", friend.getFriend());
            memFriendMap.put("follower", friend.getFollower());
            memFriendMap.put("followee", memFolloweeList);

            ranFriendMap.put("friend", Collections.EMPTY_LIST);
            ranFriendMap.put("follower", ranFollowerList);
            ranFriendMap.put("followee", Collections.EMPTY_LIST);

            ApiFuture<WriteResult> future1 = memberCollection.document(random).set(memberMap);
            ApiFuture<WriteResult> future2 = friendCollection.document(random).set(ranFriendMap);
            ApiFuture<WriteResult> future3 = friendCollection.document("admin").set(memFriendMap, SetOptions.merge());
            System.out.println("future1 = " + future1.get().getUpdateTime());
            System.out.println("future2 = " + future2.get().getUpdateTime());
            System.out.println("future3 = " + future3.get().getUpdateTime());
        }
    }

    @Test
    public void 나와친구인멤버추가() throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollection = firestore.collection("friend");
        CollectionReference memberCollection = firestore.collection("member");
        Friend friend = friendCollection.document("admin").get().get().toObject(Friend.class);

        for (int i=0;i<3;i++){
            HashMap<String, String> memberMap = new HashMap<>();
            HashMap<String, Object> memFriendMap = new HashMap<>();
            HashMap<String, Object> ranFriendMap = new HashMap<>();
            String random = getRandomString(6);
            memberMap.put("id",random);
            memberMap.put("password",random);
            memberMap.put("name",random);
            memberMap.put("nickname",random);
            memberMap.put("email",random+"@naver.com");

            List<String> memFriendList = friend.getFriend();
            ArrayList<String> ranFriendList = new ArrayList<>();
            memFriendList.add(random);
            ranFriendList.add("admin");

            memFriendMap.put("friend", memFriendList);
            memFriendMap.put("follower", friend.getFollower());
            memFriendMap.put("followee", friend.getFollowee());

            ranFriendMap.put("friend", ranFriendList);
            ranFriendMap.put("follower", Collections.EMPTY_LIST);
            ranFriendMap.put("followee", Collections.EMPTY_LIST);

            ApiFuture<WriteResult> future1 = memberCollection.document(random).set(memberMap);
            ApiFuture<WriteResult> future2 = friendCollection.document(random).set(ranFriendMap);
            ApiFuture<WriteResult> future3 = friendCollection.document("admin").set(memFriendMap, SetOptions.merge());
            System.out.println("future1 = " + future1.get().getUpdateTime());
            System.out.println("future2 = " + future2.get().getUpdateTime());
            System.out.println("future3 = " + future3.get().getUpdateTime());
        }
    }


    static String getRandomString(int i){
        String randomInfo;
        StringBuilder builder;
        randomInfo="ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789";
        builder=new StringBuilder();
        for (int j=0;j<i;j++){
            int index=(int)(randomInfo.length()*Math.random());
            builder.append(randomInfo.charAt(index));
        }
        return builder.toString();
    }


}