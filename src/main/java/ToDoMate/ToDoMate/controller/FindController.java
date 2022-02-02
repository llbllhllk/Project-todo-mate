package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.PasswordForm;
import ToDoMate.ToDoMate.service.AuthenticationService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class FindController {

    private final AuthenticationService authenticationService;

    public FindController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 다솔님 코드 참고하려고 가져온 코드
     * 프론트에서 데이터 받을 땐 @RequestParam()
     * 백에서 프론트로 보낼 땐 url check & @responsebody 꼭 붙일걸
     */

//    @GetMapping("acceptFollower")
//    @ResponseBody
//    public List<String> acceptFollower(@SessionAttribute("member") Member member,
//                                       @RequestParam("follower") String nickname) throws Exception{
//        return friendService.acceptFriend(member.getId(), nickname);
//    }

    /**
     * 인증번호 보내기 Server to Client
     */

    @PostMapping("/validEmail")
    @ResponseBody
    public boolean postValidEmail(@RequestBody String email, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
//            System.out.println(document.toObject(Member.class).getEmail()+" compare to " + email);
            if(document.toObject(Member.class).getEmail().equals(email.substring(10,email.length()-2))){
                String to = document.toObject(Member.class).getEmail();
                String from = "kitaecoding999@gmail.com";
                String subject = "To Do Mate 이메일 인증 관련 메일";
                String validationString = authenticationService.generateRandomNumber();
                session.setAttribute("certification",validationString);   //이메일 인증을 위한 인증번호 세션에 저장
                session.setAttribute("id",document.toObject(Member.class).getId()); //이메일에 해당하는 멤버 아이디 세션에 저장
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
                return true;
            }
        }
        return false;
    }

    /**
     * 인증번호 확인 Server to Client
     */

    @PostMapping("validCertification")
    @ResponseBody
    public String postValidCertification(@RequestBody String certification, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String userCertification = (String) session.getAttribute("certification");
        certification=certification.substring(18,certification.length()-2);
        if(certification.equals(userCertification)){
            String id = (String)session.getAttribute("id");
            return id;
        }
        else{
            return "";
        }
    }

    @PostMapping("timeoutCertification")
    @ResponseBody
    public boolean postTimeoutCertification(@RequestBody String timeout,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        timeout=timeout.substring(11,timeout.length()-1);
        String validationString = authenticationService.generateRandomNumber();
        if(timeout.equals("true")) {    //timeout이 true일 때, 즉 시간초과가 났을 때
            session.setAttribute("certification", validationString);   //이메일 인증을 위한 인증번호 세션에 저장
            return true;
        }
        return false;
    }

    @PostMapping("validId")
    @ResponseBody
    public boolean postValidId(@RequestBody String id) throws Exception{
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

    @PostMapping("/changePw")
    @ResponseBody
    public void postChangePw(@RequestBody String password, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("member").document(member.getId());
        ApiFuture<WriteResult> future = documentReference.update("password",password);
    }

    @PostMapping("/checkNickname")
    @ResponseBody
    public boolean postCheckNickname(@RequestBody String nickname) throws Exception{
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

    @PostMapping("/resetNickname")
    @ResponseBody
    public void postResetNickname(@RequestBody String nickname, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("member").document(member.getId());
        ApiFuture<WriteResult> future = documentReference.update("nickname",nickname);
    }


    @GetMapping("find-id")
    public String viewFindId() throws Exception{
        return "find-id";
    }

    @GetMapping("find-pw")
    public String viewFindPassword(){
        return "find-pw";
    }

    @GetMapping("/reset-pw")
    public String viewResetPw(){
        return "reset-pw";
    }
}
