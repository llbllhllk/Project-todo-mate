package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.MemberRepository;

import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public String join(Member member) throws Exception{
        memberRepository.registerMember(member);
        return member.getId();
    }

    public int uniqueId(Member member) throws Exception{
        Optional<Member> result = memberRepository.getMemberIdInformation(member.getId());
        if(result.isPresent()){
            return 1;
        }
        else{
            return 0;
        }
    }

    public Optional<Member> findMember(String id) throws Exception{
        return memberRepository.getMemberIdInformation(id);
    }

    public int uniqueNickname(String nickname) throws Exception{
        if(!memberRepository.nicknameDuplicateCheck(nickname)){
            return 1;
        }
        else{
            return 0;
        }
    }

    public int uniqueEmail(String email) throws Exception{
        if(!memberRepository.emailDuplicateCheck(email)){
            return 1;
        }
        else{
            return 0;
        }
    }


}