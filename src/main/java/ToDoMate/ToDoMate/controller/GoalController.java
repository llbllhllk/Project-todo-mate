package ToDoMate.ToDoMate.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoalController {

    @GetMapping("/edit-goal")
    public String viewEditGoal(){
        return "edit-goal";
    }

    @GetMapping("/simple-input")
    public String viewSimpleInput(){
        return "simple-input";
    }


}
