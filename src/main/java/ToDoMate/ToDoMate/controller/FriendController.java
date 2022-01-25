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

//    HttpSession session;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    @GetMapping("friend")
    public String viewFriend(Member member, HttpServletRequest request) throws Exception {
        return "friend";
    }

    /**
     * 회원 닉네임으로 검색
     * param Search-user-nickname
     * @return findMemberList
     * @throws Exception
     */
    @GetMapping("searchMember")
    @ResponseBody
    public Optional<Map> findMember(@SessionAttribute("member")Member member,
                                   @RequestParam("user")String searchNickname) throws Exception{
        String searchArea = member.getId();
        return friendService.findMember(searchArea, searchNickname);
    }


    /**
     * 친구 요청
     * @param member
     * @param followNickname
     * @return 성공시 true, 실패시 false
     * @throws Exception
     */
    @GetMapping("requestFriend")
    @ResponseBody
    public Boolean requestFriend(@SessionAttribute("member")Member member,
                                      @RequestParam("followUser")String followNickname) throws Exception{
        return friendService.requestFriend(member.getId(), followNickname);
    }


    /**
     * 친구목록보여주기
     * @param member
     * @return friendList
     * @throws Exception
     */
    @GetMapping(value="friendList")
    @ResponseBody
    public List<String> viewFriendList(@SessionAttribute(name = "member") Member member) throws Exception{
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
    @GetMapping(value="searchFriend")
    @ResponseBody
    public List<String> searchFriendList(@SessionAttribute(name = "member")Member member,
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


    /**
     * 친구삭제
     * @param member
     * @param deleteName
     * @return 삭제 후 친구 리스트
     * @throws Exception
     */
    @GetMapping("deleteFriend")
    @ResponseBody
    public List<String> deleteFriend(@SessionAttribute("member") Member member,
                                     @RequestParam("friendName")String deleteName) throws Exception {
        return friendService.deleteFriend(member.getId(), deleteName);
    }


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


    /**
     * 친구 요청 받아주기
     * @param member
     * @param nickname
     * @return 수락 후 남아있는 팔로워목록
     * @throws Exception
     */
    @GetMapping("acceptFollower")
    @ResponseBody
    public List<String> acceptFollower(@SessionAttribute("member") Member member,
                                       @RequestParam("follower") String nickname) throws Exception{
        return friendService.acceptFriend(member.getId(), nickname);
    }


    /**
     * 팔로워목록에서 친구 거절하기
     * @param member
     * @param nickname
     * @return 거절한 후 남아있는 팔로워목록
     * @throws Exception
     */
    @GetMapping("refuseFollower")
    @ResponseBody
    public List<String> refuseFollower(@SessionAttribute("member") Member member,
                                       @RequestParam("follower") String nickname) throws Exception {
        return friendService.refuseFriend(member.getId(), nickname);
    }

}