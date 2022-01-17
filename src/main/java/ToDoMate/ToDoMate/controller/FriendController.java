package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.service.FriendService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("friendList")
    public void friendList() throws Exception {
        Optional<Friend> friend = friendService.friendList("dlrlxo99");
        System.out.println(friend.get().getFriend());
    }

    @GetMapping("/friend")
    public String viewFriend() {
        return "friend";
    }
}
