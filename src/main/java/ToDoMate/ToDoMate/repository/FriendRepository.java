package ToDoMate.ToDoMate.repository;



import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    Optional<Friend> getFriendList(String id) throws Exception;

    List<String> getMemberNicknameList(String area, String search) throws Exception;

    List<String> refuseFollower(String memberId, String nickname) throws Exception;

    List<String> acceptFollower(String memberId, String memberNickname, String followerId, String followerNickname) throws Exception;

    Optional<String> findMemberIdByNickname(String nickname) throws Exception;
    Optional<String> findMemberNicknameById(String memberId) throws Exception;
}
