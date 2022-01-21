package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.service.FriendService;
import com.google.firebase.database.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@SessionAttributes("member")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    HttpSession session;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    @GetMapping("/friend")
    public String viewFriend(Member member, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        member = (Member) session.getAttribute("member");
//        Optional<Friend> friend = friendService.friendList(member.getId());
//        System.out.println(friend.get().getFriend());
//        return "friend";
        HttpSession session = request.getSession();
        Member sessionMember = (Member)session.getAttribute("member");
        if(sessionMember!=null){
            return "friend";
        }
        return "login";
    }

    /**
     * 회원 닉네임으로 검색
     * param Search-user-nickname
     * @return findMemberList
     * @throws Exception
     */
    @PostMapping("/searchMember")
    @ResponseBody
    @ModelAttribute("member")
    public List<String> findMember(@SessionAttribute("member")Member member,
                                   @RequestParam("search-user")String searchNickname) throws Exception{
        String searchArea = member.getId();
        return friendService.findMember(searchArea, searchNickname);

        //목록 떴을때 친구추가를 위한 기능 (세션에 추가해서 넘김?) -> 다시 데이터를 받는 방법?
    }


    /**
     * 친구목록보여주기
     * @param member
     * @return friendList
     * @throws Exception
     */
    @GetMapping(value="/friendList")
    @ResponseBody
    public List<String> viewFriendListByDasol(@SessionAttribute("member")Member member) throws Exception{
        Optional<Friend> friend = friendService.friendList(member.getId()); //저장된 친구리스트
        List<String> friendList = friend.get().getFriend();
        return friendList;
    }


    /**
     * 친구목록에서 친구검색하기
     * @param member
     * @param searchName
     * @return searchResultList
     * @throws Exception
     */
    @PostMapping(value="/friendList")
    @ResponseBody
    public List<String> searchFriendListByDasol(@SessionAttribute("member")Member member,
                                                @RequestParam("friendName")String searchName) throws Exception{
        List<String> friend = friendService.friendList(member.getId()).get().getFriend();//저장된 친구리스트
        List<String> searchFriendList = new ArrayList<>();
        //친구검색
        for (int i =0; i<friend.size(); i++)
        {
            if(friend.get(i).contains(searchName) == true)
            {
                searchFriendList.add(friend.get(i));
            }
        }
        return searchFriendList;
    }



    //친구목록에서 친구 삭제


    /**
     * 나를 친구로 추가한 목록 보여주기
     * @param member
     * @return followerList
     * @throws Exception
     */
    @GetMapping(value="followerList")
    @ResponseBody
    public List<String> viewFollower(@SessionAttribute("member")Member member) throws Exception{

        Optional<Friend> friend = friendService.friendList(member.getId()); //저장된 친구리스트
        List<String> followerList = friend.get().getFollower();
        return followerList;
    }


//    //친구요청창에서 친구 받아주기/거절하기
//    @PostMapping("/acceptOrRefuse")
//    public String acceptOfRefuse(@SessionAttribute("member") Member member, HttpServletRequest request) throws Exception{
//
//    }








}