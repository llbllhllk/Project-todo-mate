package ToDoMate.ToDoMate.validator;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.domain.Nickname;
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
            Nickname nickname1 = new Nickname();
            nickname1.setNickname(nickname);
            if(memberService.uniqueNickname(nickname1)==1){  // 아이디가 중복
                return false;
            }
            else{
                return true;
            }
        }

    }


    public boolean validateEmail(String email){
        if(email==null){
            return false;
        }
        else{
            String regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
            if(Pattern.matches(regex,email)){
                return true;
            }
            else{
                return false;
            }
        }
    }



}