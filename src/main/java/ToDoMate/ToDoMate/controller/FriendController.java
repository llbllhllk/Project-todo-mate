package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.service.FriendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

//    @GetMapping("friendList")
//    public void friendList() throws Exception {
//        Optional<Friend> friend = friendService.friendList("dlrlxo99");
//        System.out.println(friend.get().getFriend());
//    }

    @GetMapping("/friend")
    public String viewFriend(Member member, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        member = (Member) session.getAttribute("member");
//        Optional<Friend> friend = friendService.friendList(member.getId());
//        System.out.println(friend.get().getFriend());
        return "friend";
    }

    @RequestMapping(value="/friendList",method = RequestMethod.POST)
    @ResponseBody
    public List<String> viewFriendList(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        Optional<Friend> friend = friendService.friendList(member.getId());
        List<String> listName = new ArrayList<>();
        for (int i =0; i<friend.get().getFriend().size(); i++)
        {
            if(friend.get().getFriend().get(i).contains(request.getParameter("friendName")) == true)
            {
                listName.add(friend.get().getFriend().get(i));
            }
            else if(friend.get().getFriend().get(i).contains(request.getParameter("friendName")) == true)
            {
                listName.add(friend.get().getFriend().get(i));
            }
        }
        return listName;
    }

}