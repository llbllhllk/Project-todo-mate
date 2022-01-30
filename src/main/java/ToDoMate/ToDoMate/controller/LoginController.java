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

    @RequestMapping(value="sign-up",method = RequestMethod.POST)
    @ResponseBody
    public List<String> memberJoin(Model model, JoinForm joinForm) throws Exception{
        boolean idValidation=false;
        boolean passwordValidation=false;
        boolean checkPasswordValidation=false;
        boolean nicknameValidation=false;
        boolean emailValidation=false;

        Member member = new Member();
        ArrayList<String> returnValues = new ArrayList<String>();

        if(joinForm.getId().equals("") || joinForm.getPassword().equals("") || joinForm.getNickname().equals("") || joinForm.getName().equals("") || joinForm.getEmail().equals("") || joinForm.getCheckPassword().equals("")) {
            returnValues.add("not_entered");
        }

        if(validate.validateId(joinForm.getId())){
            member.setId(joinForm.getId());
            idValidation=true;
        }
        else{
            System.out.println("중복 아이디");
            returnValues.add("id_duplicate");
        }

        if(validate.validatePassword(joinForm.getPassword())){
            member.setPassword(joinForm.getPassword());
            passwordValidation=true;
        }
        else{
            System.out.println("비밀번호 유효성 틀림");
            returnValues.add("password_availability");
        }

        if(validate.validateCheckPassword(member.getPassword(),joinForm.getCheckPassword())){
            checkPasswordValidation=true;
        }
        else{
            System.out.println("비밀번호 확인 유효성 틀림");
            returnValues.add("password_check_availability");
        }

        member.setName(joinForm.getName());

        if(validate.validateNickname(joinForm.getNickname())){
            member.setNickname(joinForm.getNickname());
            nicknameValidation=true;
        }
        else{
            System.out.println("닉네임 중복");
            returnValues.add("nickname_duplicate");
        }
//        System.out.println(nickname.getNickname());

        if(validate.validateEmail(joinForm.getEmail())==0){
            member.setEmail(joinForm.getEmail());
            emailValidation=true;
        }
        else if(validate.validateEmail(joinForm.getEmail())==2){
            System.out.println("이메일이 중복되었습니다. 다시 입력해주세요.");
            returnValues.add("email_duplicate");
        }
        else if(validate.validateEmail(joinForm.getEmail())==3){
            System.out.println("이메일이 규격에 맞지 않습니다. 다시 입력해주세요.");
            returnValues.add("email_availability");
        }

        if(idValidation&&passwordValidation&&checkPasswordValidation&&nicknameValidation&&emailValidation){ // 모든 회원가입 유효성 충족
            memberService.join(member);
            System.out.println("회원가입이 완료되었습니다.");
            returnValues.add("sign-up_complete");
        }
        else{
            model.addAttribute("signupFlag", 1);
            returnValues.add("sign-up_not_complete");
        }
        return returnValues;

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

//    @PostMapping("/reset-nickname-check")
//    public String resetNicknameCheck(HttpServletRequest request, NicknameForm nicknameForm,Model model) throws Exception{
//        HttpSession session = request.getSession();
//        boolean nicknameDuplicateCondition=true;
//        Member member = (Member)session.getAttribute("member");
//        if(nicknameForm.getNickname().equals("")){
//            System.out.println("닉네임을 입력해주세요");
//            model.addAttribute("nicknameFlag",2);
//            return "reset-nickname";
//        }
//        Firestore firestore = FirestoreClient.getFirestore();
//        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//        for(QueryDocumentSnapshot document : documents){
//            if(document.toObject(Member.class).getNickname().equals(nicknameForm.getNickname())){
//                System.out.println("닉네임이 중복되었습니다.");
//                nicknameDuplicateCondition=false;
//                model.addAttribute("nicknameFlag",1);
//            }
//        }
//        if(nicknameDuplicateCondition){
//            model.addAttribute("nicknameFlag",0);
//            model.addAttribute("nicknameValue",nicknameForm.getNickname());
//        }
//        return "reset-nickname";
//    }
//
//    @PostMapping("/reset-nickname-submit")
//    public String resetNicknameSubmit(NicknameForm nicknameForm, HttpServletRequest request) throws Exception{
//        HttpSession session = request.getSession();
//        Member member = (Member)session.getAttribute("member");
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference documentReference = firestore.collection("member").document(member.getId());
//        ApiFuture<WriteResult> future = documentReference.update("nickname",nicknameForm.getNickname());
//        System.out.println("닉네임이 변경되었습니다.");
//        return "reset-nickname";
//    }


    /**
     * 회원가입시에 아이디 중복체크
     */

    @PostMapping("validSignUpIdDuplicate")
    @ResponseBody
    public boolean postValidSignUpIdDuplicate(@RequestParam("id") String id) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * 회원가입시에 닉네임 중복체크
     */

    @PostMapping("validSignUpNicknameDuplicate")
    @ResponseBody
    public boolean postValidSignUpNicknameDuplicate(@RequestParam("nickname") String nickname) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getNickname().equals(nickname)){
                return true;
            }
        }
        return false;
    }

    /**
     * 회원가입시에 이메일 중복체크
     */

    @PostMapping("validSignUpEmailDuplicate")
    @ResponseBody
    public boolean postValidSignUpEmailDuplicate(@RequestParam("email") String email) throws Exception{
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
}