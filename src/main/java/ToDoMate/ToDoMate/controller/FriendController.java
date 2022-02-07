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
import java.util.*;
import java.util.stream.Stream;

@Controller
@SessionAttributes("member")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    /**
     * 친구페이지로 이동
     * @param member
     * @param model
     * @return 친구, 팔로워 수
     * @throws Exception
     */
    @GetMapping("friend")
    public String viewFriend(@SessionAttribute("member")Member member,
                             Model model) throws Exception {
        //친구수와 요청수 보여주기
        String memberId = member.getId();
        List<Integer> friendNumInfo = friendService.getFriendNumInfo(memberId);
        model.addAttribute("friendNum", friendNumInfo.get(0));
        model.addAttribute("followerNum", friendNumInfo.get(1));
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
                                    @RequestParam("user")String searchNickname,
                                    @RequestParam(value = "lastMem", required = false)String lastMem) throws Exception{
        if (Objects.equals(lastMem, null)){
            lastMem="";
        }

        return friendService.findMember(member.getId(), member.getNickname(),searchNickname, lastMem);
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
        return friendService.requestFriend(member.getId(), member.getNickname(), followNickname);
    }

    /**
     * 친구요청 취소
     * @param member
     * @param cancelNickname
     * @return
     * @throws Exception
     */
    @GetMapping("cancelRequest")
    @ResponseBody
    public Boolean cancelFollow(@SessionAttribute("member")Member member,
                                @RequestParam("followUser")String cancelNickname) throws Exception {
        return friendService.cancelFollow(member.getId(), member.getNickname(), cancelNickname);
    }


    /**
     * 친구목록보여주기
     * @param member
     * @return friendList
     * @throws Exception
     */
    @GetMapping(value="friendList")
    @ResponseBody
    public List<String> viewFriendList(@SessionAttribute(name = "member") Member member,
                                       @RequestParam(value = "lastMem", required = false)String lastMem) throws Exception{
        Optional<Friend> friend = friendService.friendList(member.getId()); //저장된 친구리스트
        List<String> friendList = friend.get().getFriend();
        return friendList;
    }


//    /**
//     * 친구목록에서 친구검색하기
//     * @param member
//     * @param searchName
//     * @return searchResultList
//     * @throws Exception
//     */
//    @GetMapping(value="searchFriend")
//    @ResponseBody
//    public List<String> searchFriendList(@SessionAttribute(name = "member")Member member,
//                                         @RequestParam("friendName")String searchName) throws Exception{
//        List<String> friend = friendService.friendList(member.getId()).get().getFriend();//저장된 친구리스트
//        List<String> searchFriendList = new ArrayList<>();
//        //친구검색
//        for (int i =0; i<friend.size(); i++)
//        {
//            if(friend.get(i).contains(searchName) == true)
//            {
//                searchFriendList.add(friend.get(i));
//            }
//        }
//        return searchFriendList;
//    }


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
        return friendService.deleteFriend(member.getId(), member.getNickname(), deleteName);
    }


//    /**
//     * 나를 친구로 추가한 목록 보여주기
//     * @param member
//     * @return followerList
//     * @throws Exception
//     */
//    @GetMapping(value="followerList")
//    @ResponseBody
//    public List<String> viewFollower(@SessionAttribute("member")Member member) throws Exception{
//
//        Optional<Friend> friend = friendService.friendList(member.getId()); //저장된 친구리스트
//        List<String> followerList = friend.get().getFollower();
//        return followerList;
//    }


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
        return friendService.acceptFriend(member.getId(), member.getNickname(), nickname);
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
        return friendService.refuseFriend(member.getId(), member.getNickname(), nickname);
    }

}