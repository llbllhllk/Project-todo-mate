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

    @Autowired
    private JavaMailSender javaMailSender;

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

    @GetMapping("find-id")
    public String viewFindId() throws Exception{
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference documentReference = firestore.collection("member").document("dlrlxo999").;
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        DocumentSnapshot documentSnapshot = future.get();
//        if(documentSnapshot.exists()){
//            System.out.println("Document data : " + documentSnapshot.getData());
//        }
//        else{
//            System.out.println("No such Document!");
//        }

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            System.out.println(document.getId() + " =>" + document.toObject(Member.class).getEmail());
        }
        return "find-id";
    }

    @PostMapping("find-id")
    public String findId(EmailForm emailForm) throws MessagingException, UnsupportedEncodingException{

        String userEmail;   // 회원 이메일

        if(emailForm.getEmail().equals("")){    // 이메일을 아무것도 입력안할 때
            System.out.println("이메일을 입력하지 않으셨습니다. 이메일을 입력해주세요.");
            return "find-id";
        }
//        else if(){  //이메일을 입력했으나 DB에 없는 이메일일 때
//            return "find-id";
//        }
        else{   //DB에 있는 이메일을 잘 입력했을 때
            userEmail = emailForm.getEmail();
        }

        String to = userEmail;
        String from = "kitaecoding999@gmail.com";
        String subject = "To Do Mate 이메일 인증 관련 메일";
        String validationString = authenticationService.generateRandomNumber();

        StringBuilder body = new StringBuilder();
        body.append("<html><body><h3>안녕하세요. To Do Mate 관리자입니다. 이메일 인증번호 보내드립니다.<br>");
        body.append("인증번호는 " +validationString+"입니다.<br>");
        body.append("To Do Mate 사이트에 가셔서 인증번호를 올바르게 입력해주시기 바랍니다.</h3></body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(from,"To Do Mate Administrator");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);


        javaMailSender.send(message);
        return "find-id";
    }

    @GetMapping("find-pw")
    public String viewFindPassword(){
        return "find-pw";
    }

    @PostMapping("find-pw")
    public String findPw(EmailForm emailForm) throws MessagingException, UnsupportedEncodingException{
        String to = emailForm.getEmail();
        String from = "kitaecoding999@gmail.com";
        String subject = "To Do Mate 이메일 인증 관련 메일";
        String validationString = authenticationService.generateRandomNumber();

        StringBuilder body = new StringBuilder();
        body.append("<html><body><h3>안녕하세요. To Do Mate 관리자입니다. 이메일 인증번호 보내드립니다.<br>");
        body.append("인증번호는 " +validationString+"입니다.<br>");
        body.append("To Do Mate 사이트에 가셔서 인증번호를 올바르게 입력해주시기 바랍니다.</h3></body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(from,"To Do Mate Administrator");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);


        javaMailSender.send(message);
        return "find-pw";
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

    @PostMapping("/reset-nickname-check")
    public String resetNicknameCheck(HttpServletRequest request, NicknameForm nicknameForm,Model model) throws Exception{
        HttpSession session = request.getSession();
        boolean nicknameDuplicateCondition=true;
        Member member = (Member)session.getAttribute("member");
        if(nicknameForm.getNickname().equals("")){
            System.out.println("닉네임을 입력해주세요");
            model.addAttribute("nicknameFlag",2);
            return "reset-nickname";
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            if(document.toObject(Member.class).getNickname().equals(nicknameForm.getNickname())){
                System.out.println("닉네임이 중복되었습니다.");
                nicknameDuplicateCondition=false;
                model.addAttribute("nicknameFlag",1);
            }
        }
        if(nicknameDuplicateCondition){
            model.addAttribute("nicknameFlag",0);
            model.addAttribute("nicknameValue",nicknameForm.getNickname());
        }
        return "reset-nickname";
    }

    @PostMapping("/reset-nickname-submit")
    public String resetNicknameSubmit(NicknameForm nicknameForm, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("member").document(member.getId());
        ApiFuture<WriteResult> future = documentReference.update("nickname",nicknameForm.getNickname());
        System.out.println("닉네임이 변경되었습니다.");
        return "reset-nickname";
    }

    @GetMapping("/reset-pw")
    public String viewResetPw(){
        return "reset-pw";
    }

    @PostMapping("/reset-pw")
    @ResponseBody
    public int resetPw(HttpServletRequest request, PasswordForm passwordForm){
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if(validate.validatePassword(passwordForm.getPassword())){
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = firestore.collection("member").document(member.getId());
            ApiFuture<WriteResult> future = documentReference.update("password",passwordForm.getPassword());
            System.out.println("비밀번호가 변경되었습니다.");
            return 0;
        }
        else{
            if(passwordForm.getPassword().equals("") || passwordForm.getCheckPassword().equals("")){
                System.out.println("비밀번호를 입력해주세요.");
                return 1;
            }
            else if(!passwordForm.getPassword().equals(passwordForm.getCheckPassword())){
                System.out.println("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                return 2;
            }
            else{
                System.out.println("비밀번호 유효성을 충족하지 못했습니다.");
                return 3;
            }
        }
    }


}