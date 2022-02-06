package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.*;
import ToDoMate.ToDoMate.service.AuthenticationService;
import ToDoMate.ToDoMate.service.MemberService;
import ToDoMate.ToDoMate.validator.Validate;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    private final MemberService memberService;
    private final Validate validate;
    private final AuthenticationService authenticationService;

    public LoginController(MemberService memberService, Validate validate, AuthenticationService authenticationService) {
        this.memberService = memberService;
        this.validate = validate;
        this.authenticationService = authenticationService;
    }


    @GetMapping("/sign-up")
    public String viewJoin(HttpServletRequest request){
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if(member!=null){
            return "main";
        }
        return "sign-up";
    }

    @GetMapping("/login")
    public String viewLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if(member!=null){
            return "main";
        }
        return "login";
    }

    @PostMapping("/login")
    public String memberLogin(Model model, LoginForm loginForm, HttpServletRequest request) throws Exception{
        Member member = new Member();
        if(loginForm.getId().equals("") || loginForm.getPassword().equals("")){
            model.addAttribute("loginFlag",1);  // 아이디나 비밀번호 중 하나라도 입력이 안되었을 때
            return "login";
        }
        else{
            member.setId(loginForm.getId());
            member.setPassword(loginForm.getPassword());
            Optional<Member> loginMember = memberService.findMember(member.getId());
            if (loginMember.isPresent()) {
                if (loginMember.get().getPassword().equals(member.getPassword())) { // 로그인 성공
                    HttpSession session = request.getSession();
                    session.setAttribute("member",loginMember.get());   //세션에 로그인한 멤버 객체 삽입
                    System.out.println(session.getAttribute("member")); //로그인에 성공한 후 세션에 멤버 객체가 잘들어갔는지 테스트
                    return "main";
                }
                else{
                    model.addAttribute("loginFlag",2);  // 아이디나 비밀번호가 틀릴 때
                }
            }
            else{
                model.addAttribute("loginFlag",2);  // 아이디나 비밀번호가 틀릴 때
            }
        }

        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if(member==null){
            System.out.println("로그인 된 정보가 없습니다. 로그인 후 이용해주세요.");
            return "login";
        }

        else{
            session.invalidate();
            return "start";
        }
    }


    @GetMapping("accountWithdraw")
    public String accountWithdraw(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        String target = member.getId();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("member").document(target).delete();
        System.out.println("계정 삭제가 완료되었습니다.");
        return "start";
    }

    @GetMapping("/user-edit")
    public String viewUserEdit(){
        return "user-edit";
    }

    @GetMapping("/reset-nickname")
    public String viewResetNickname(){
        return "reset-nickname";
    }


    /**
     * 회원가입시에 아이디 중복체크
     */

    @PostMapping("validSignUpIdDuplicate")
    @ResponseBody
    public boolean postValidSignUpIdDuplicate(@RequestBody String id) throws Exception{
        //{"id":"dlrlxo999"}
        String userId = id.substring(7,id.length()-2);
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getId().equals(userId)){
                return false;
            }
        }
        return true;
    }

    /**
     * 회원가입시에 닉네임 중복체크
     */

    @PostMapping("validSignUpNicknameDuplicate")
    @ResponseBody
    public boolean postValidSignUpNicknameDuplicate(@RequestBody String nickname) throws Exception{
        //{"nickname":"경주불주먹"}
        String userNickname = nickname.substring(13,nickname.length()-2);
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getNickname().equals(userNickname)){
                return false;
            }
        }
        return true;
    }

    /**
     * 회원가입시에 이메일 중복체크
     */

    @PostMapping("validSignUpEmailDuplicate")
    @ResponseBody
    public boolean postValidSignUpEmailDuplicate(@RequestBody String email) throws Exception{
        //{"email":"dlrlxo999@naver.com"}
        String userEmail = email.substring(10,email.length()-2);
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getEmail().equals(userEmail)){
                return false;
            }
        }
        return true;
    }

    /**
     * 회원정보들 넘어오면 회원가입 완료하고 데이터베이스에 정보들 넣기
     */

    @PostMapping("/postSignUp")
    @ResponseBody
    public void postSignUp(@RequestBody String userInfo) throws Exception{
        //{"id": "dlrlxo999", "password": "@@aa0332601", "name" : "이기태" , "nickname" : "경주불주먹", "email" : "dlrlxo999@naver.com"} 데이터 넘어오는 형식
        String[] info = userInfo.split(",");
        String id = info[0];    //{"id":"dlrlxo999"
        id=id.substring(7,id.length()-1);
        String password = info[1];  //"password":"@@aa0332601"
        password=password.substring(12,password.length()-1);
        String name = info[2];  //"name":"이기태"
        name=name.substring(8,name.length()-1);
        String nickname = info[3];  //"nickname":"경주불주먹"
        nickname=nickname.substring(12,nickname.length()-1);
        String email = info[4]; //"email":"dlrlxo999@naver.com"}
        email=email.substring(9,email.length()-2);
        Member member = new Member();
        member.setId(id);
        member.setPassword(password);
        member.setName(name);
        member.setNickname(nickname);
        member.setEmail(email);
        memberService.join(member);
        memberService.joinFriend(member);
        memberService.joinGoal(member);
        System.out.println("회원가입이 완료되었습니다.");
        System.out.println("friend 컬렉션과 goal 컬렉션에 member 데이터가 추가되었습니다.");
    }
}