package ToDoMate.ToDoMate.repository;



import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    Optional<Friend> getFriendList(String id) throws Exception;

    List<String> findMemberList(String area, String search) throws Exception;


}
