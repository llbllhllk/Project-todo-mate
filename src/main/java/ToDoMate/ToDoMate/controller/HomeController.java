package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewStart(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if(member==null){   // 로그인 안되어있고 처음 시작하는 상태
            return "start";
        }
        else{   // 이미 로그인 되어있는 상태
            return "main";
        }
    }

    @GetMapping("main")
    public String viewMain(){
        return "main";
    }

}
