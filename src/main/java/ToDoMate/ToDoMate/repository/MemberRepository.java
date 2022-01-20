package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.domain.Nickname;

import java.util.Optional;

public interface MemberRepository{

    String registerMember(Member member) throws Exception;
    Optional<Member> getMemberIdInformation(String id) throws Exception;
    Optional<Nickname> getMemberNicknameInformation(String nickname) throws Exception;
    String registerNickname(Nickname nickname) throws Exception;

}