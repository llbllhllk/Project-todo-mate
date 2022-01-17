package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.repository.FriendRepository;
import java.util.List;
import java.util.Optional;

public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Optional<Friend> friendList(String id) throws Exception{
        return friendRepository.getFriendList(id);
    }


}
