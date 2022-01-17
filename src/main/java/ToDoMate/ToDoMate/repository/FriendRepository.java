package ToDoMate.ToDoMate.repository;



import ToDoMate.ToDoMate.domain.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    Optional<Friend> getFriendList(String id) throws Exception;
}
