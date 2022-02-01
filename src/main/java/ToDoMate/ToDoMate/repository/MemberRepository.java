package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Member;

import java.util.Optional;

public interface MemberRepository{

    String registerMember(Member member) throws Exception;
    Optional<Member> getMemberIdInformation(String id) throws Exception;
    Boolean nicknameDuplicateCheck(String nickname) throws Exception;
    Boolean emailDuplicateCheck(String email) throws Exception;
    String registerMemberToFriend(Member member) throws Exception;
    String registerMemberToGoal(Member member) throws Exception;
}