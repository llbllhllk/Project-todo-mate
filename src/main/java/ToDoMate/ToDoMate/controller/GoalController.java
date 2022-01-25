package ToDoMate.ToDoMate.controller;


import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.form.GoalForm;
import ToDoMate.ToDoMate.service.GoalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class GoalController {

    private static GoalService goalService;

    public GoalController(GoalService goalService){
        this.goalService=goalService;
    }

    @GetMapping("/edit-goal")
    public String viewEditGoal(){
        return "edit-goal";
    }

    @GetMapping("/simple-input")
    public String viewSimpleInput(){
        return "simple-input";
    }

    @GetMapping("goalPlusTest")
    public String viewGoalPlus(){
        return "goalPlusTest";
    }

    @PostMapping("goalPlusTest")
    public String goalPlus(HttpServletRequest request, GoalForm goalForm) throws Exception{
        Goal goal = new Goal();
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        System.out.println(goalForm.getGoal());
        goalService.plusGoal(goalForm.getGoal(),request,goal);
        return "goalPlusTest";
    }
}
