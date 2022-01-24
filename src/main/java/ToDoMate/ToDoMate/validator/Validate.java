package ToDoMate.ToDoMate.validator;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.service.MemberService;

import java.util.regex.Pattern;

public class Validate {

    private final MemberService memberService;

    public Validate(MemberService memberService) {
        this.memberService = memberService;
    }

    public boolean validateId(String id) throws Exception{
        if(id==null){
            return false;
        }
        else{
            Member member = new Member();
            member.setId(id);
            if(memberService.uniqueId(member)==1){  // 아이디가 중복
                return false;
            }
            else{
                return true;
            }
        }

    }

    public boolean validatePassword(String password){
        if(password==null){
            return false;
        }
        else{
            String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
            if(Pattern.matches(regex,password)){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean validateCheckPassword(String password, String checkPassword){
        if(password==null || checkPassword==null){
            return false;
        }
        else{
            if(password.equals(checkPassword)){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean validateNickname(String nickname) throws Exception{
        if(nickname==null){
            return false;
        }
        else{
            if(memberService.uniqueNickname(nickname)==1){  // 닉네임 중복
                return false;
            }
            else{
                return true;
            }
        }

    }


    public int validateEmail(String email) throws Exception{
        if(email==null){
            return 1;   // 이메일칸에 아무것도 입력하지 않았을 떄
        }
        else{
            String regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
            if(Pattern.matches(regex,email) && memberService.uniqueEmail(email)==0){   //이메일이 중복이 아니고 규격에 맞을 때
                return 0;
            }
            else if(memberService.uniqueEmail(email)==1){  //이메일이 중복일 때, 규격이 틀릴 수 가 없음
                return 2;
            }
            else{   //규격만 틀릴 때, 중복일 수 가 없음
                return 3;
            }
        }
    }



}