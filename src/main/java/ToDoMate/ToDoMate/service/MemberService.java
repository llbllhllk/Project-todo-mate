package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.domain.Nickname;
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

    public String joinNickname(Nickname nickname) throws Exception{
        memberRepository.registerNickname(nickname);
        return nickname.getNickname();
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

    public int uniqueNickname(Nickname nickname) throws Exception{
        Optional<Nickname> result = memberRepository.getMemberNicknameInformation(nickname.getNickname());
        if(result.isPresent()){
            return 1;
        }
        else{
            return 0;
        }
    }

}