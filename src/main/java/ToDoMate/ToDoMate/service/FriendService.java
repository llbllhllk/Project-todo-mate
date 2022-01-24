package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.repository.MemberRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Optional<Friend> friendList(String id) throws Exception{
        return friendRepository.getFriendList(id);
    }


    //DASOL 추가
    //친구추가위한 찾기
    public List<String> findMember(String area, String search) throws Exception {
        List<String> nicknameList = friendRepository.getMemberNicknameList(area, search);
        List<String> findList=new ArrayList<>();

        for (int i =0; i<nicknameList.size(); i++)
        {
            if(nicknameList.get(i).contains(search) == true)
            {
                findList.add(nicknameList.get(i));
            }
        }
        return findList;
    }


    //친구 추가 요청시 친구신청목록에 넣기
//    public Member addFriend(Member member, String addMember) throws Exception {
//        return friendRepository.addFriend(member, addMember);
//    }
    //친구목록 보여주기
    //친구목록에서 친구 찾기
    //친구신청목록보여주기
    public List<String> acceptFriend(String memberId, String nickname) throws Exception{
        String acceptId = friendRepository.findMemberIdByNickname(nickname).get();
        String memberNickname=friendRepository.findMemberNicknameById(memberId).get();
        return friendRepository.acceptFollower(memberId, memberNickname, acceptId, nickname);
    }


    public List<String> refuseFriend(String memberId, String nickname) throws Exception{
        return friendRepository.refuseFollower(memberId, nickname);
    }

}
