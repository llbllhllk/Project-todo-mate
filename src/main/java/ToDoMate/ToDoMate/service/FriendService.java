package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.FriendRepository;
import ToDoMate.ToDoMate.repository.MemberRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

//@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Optional<Friend> friendList(String id) throws Exception{
        return friendRepository.getFriendList(id);
    }


    //친구추가위한 찾기
    public Optional<Map> findMember(String area, String search) throws Exception {
        List<String> nicknameList = friendRepository.getMemberNicknameList(area, search);
        List<String> friendList = friendRepository.getFriendList(area).get().getFriend();
        List<String> followerList = friendRepository.getFriendList(area).get().getFollower();
        String myNickname = friendRepository.findMemberNicknameById(area).get();
        List<String> findList=new ArrayList<>();
        HashMap<String, Boolean> result = new HashMap<>();

        for (int i =0; i<nicknameList.size(); i++)
        {
            String nickname = nicknameList.get(i);
            if(nickname.contains(search) == true && friendList.contains(nickname)==false && !Objects.equals(nickname, myNickname))
            {
                findList.add(nickname);
            }
        }

        for (int i=0; i<findList.size();i++){
            if (followerList.contains(findList.get(i))==true){
                result.put(findList.get(i), true);
            }
            else {
                result.put(findList.get(i), false);
            }
        }
        return Optional.ofNullable(result);
    }


    //친구 추가 요청시 친구신청목록에 넣기
//    public Member addFriend(Member member, String addMember) throws Exception {
//        return friendRepository.addFriend(member, addMember);
//    }
    //친구목록 보여주기
    //친구목록에서 친구 찾기
    //친구신청목록보여주기


    public List<String> deleteFriend(String memberId, String deleteNickname) throws Exception {
        String deleteId = friendRepository.findMemberIdByNickname(deleteNickname).get();
        String memberNickname=friendRepository.findMemberNicknameById(memberId).get();
        //양쪽 friendList에서 각자를 없애고 로그인한 회원의 friend리스트 반환
        return friendRepository.deleteFriend(memberId, memberNickname, deleteId, deleteNickname);
    }


    public List<String> acceptFriend(String memberId, String nickname) throws Exception{
        String acceptId = friendRepository.findMemberIdByNickname(nickname).get();
        String memberNickname=friendRepository.findMemberNicknameById(memberId).get();
        return friendRepository.acceptFollower(memberId, memberNickname, acceptId, nickname);
    }


    public List<String> refuseFriend(String memberId, String nickname) throws Exception{
        String refuseId = friendRepository.findMemberIdByNickname(nickname).get();
        String memberNickname=friendRepository.findMemberNicknameById(memberId).get();
        return friendRepository.refuseFollower(memberId, memberNickname, refuseId, nickname);
    }

}
