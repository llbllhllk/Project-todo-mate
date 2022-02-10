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

    public List<Integer> getFriendNumInfo(String memberId) throws Exception {
        return friendRepository.getFriendNumInfo(memberId);
    }


//  친구추가위한 찾기
    public Optional<Map> findMember(String memberId, String memberNickname, String search, String lastMem) throws Exception {
        Optional<Friend> friendInfoList = friendRepository.getFriendList(memberId);
        List<String> friendList = friendInfoList.get().getFriend();
        List<String> followerList = friendInfoList.get().getFollower();
        List<String> followeeList = friendInfoList.get().getFollowee();
        search = search.toUpperCase();
        List<String> findList=new ArrayList<>();
        int loopNum=0;
        int wantNum=5;
        while (true){
            loopNum++;
            //페이징 처리된 nicknameList받아오기
            List<String> nicknameList = friendRepository.getMemberNicknameList(lastMem);
            loopNum++;
            for (String nick : nicknameList) {
                String temp = nick.toUpperCase(Locale.ROOT);
                if (findList.size()<wantNum){
                    if (temp.contains(search) && !Objects.equals(nick, memberNickname) && friendList.contains(nick)==false) {
                        findList.add(nick);
                    }
                } else break;
            }
            if (!findList.isEmpty()) lastMem=findList.get(findList.size()-1);
            if (findList.size()>=wantNum || loopNum>3) break;
        }

        Map<String, Integer> result = new HashMap<>();
        for (int i=0; i<findList.size();i++){
            if (followerList.contains(findList.get(i))==true){
                result.put(findList.get(i), 2);
            }
            else if (followeeList.contains(findList.get(i))==true){
                result.put(findList.get(i), 1);
            }
            else {
                result.put(findList.get(i), 0);
            }
        }
        return Optional.ofNullable(result);
    }


    //친구 추가 요청시 친구신청목록에 넣기
    public Boolean requestFriend(String memberId, String memberNickname, String addNickname) throws Exception {
        String addId = friendRepository.findMemberIdByNickname(addNickname).get();
        return friendRepository.requestFriend(memberId, memberNickname, addId, addNickname);
    }

    public Boolean cancelFollow(String memberId,String memberNickname, String cancelNickname) throws Exception {
        String cancelId = friendRepository.findMemberIdByNickname(cancelNickname).get();
        return friendRepository.cancelFollow(memberId, memberNickname, cancelId, cancelNickname);
    }


    public List<String> deleteFriend(String memberId, String memberNickname, String deleteNickname) throws Exception {
        String deleteId = friendRepository.findMemberIdByNickname(deleteNickname).get();
        //양쪽 friendList에서 각자를 없애고 로그인한 회원의 friend리스트 반환
        return friendRepository.deleteFriend(memberId, memberNickname, deleteId, deleteNickname);
    }


    public List<String> acceptFriend(String memberId, String memberNickname, String nickname) throws Exception{
        String acceptId = friendRepository.findMemberIdByNickname(nickname).get();
        return friendRepository.acceptFollower(memberId, memberNickname, acceptId, nickname);
    }


    public List<String> refuseFriend(String memberId, String memberNickname, String nickname) throws Exception{
        String refuseId = friendRepository.findMemberIdByNickname(nickname).get();
        return friendRepository.refuseFollower(memberId, memberNickname, refuseId, nickname);
    }

}
