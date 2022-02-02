package ToDoMate.ToDoMate.repository;



import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FriendRepository {
    Optional<Friend> getFriendList(String id) throws Exception;

    List<String> getMemberNicknameList(String area, String search) throws Exception;

    Boolean requestFriend(String memberId, String memberNickname, String addId, String addNickname) throws Exception;
    Boolean cancelFollow(String memberId, String memberNickname, String cancelId, String cancelNickname) throws Exception;

    List<String> deleteFriend(String memberId, String memberNickname, String deleteId, String deleteNickname) throws Exception;

    List<String> acceptFollower(String memberId, String memberNickname, String followerId, String followerNickname) throws Exception;

    List<String> refuseFollower(String memberId, String memberNickname, String refuseId, String refuseNickname) throws Exception;

    Optional<String> findMemberIdByNickname(String nickname) throws Exception;
    Optional<String> findMemberNicknameById(String memberId) throws Exception;
}
