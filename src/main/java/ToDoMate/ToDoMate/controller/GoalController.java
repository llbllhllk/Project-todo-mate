package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.service.GoalService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("member")
@RequiredArgsConstructor
public class GoalController {

    @Autowired
    private final GoalService goalService;

    @GetMapping("editGoal")
    public String viewGoal() throws Exception {
        return "editGoal";
    }

    @GetMapping("addGoal")
    @ResponseBody
    public Boolean addGoal(@SessionAttribute("member")Member member,
                           @RequestParam("title")String title,
                           @RequestParam("color")String color,
                           @RequestParam("goalKey")String goalKey) throws Exception {
        return goalService.addGoal(member.getId(), title, color, goalKey);
    }

    @GetMapping("deleteGoal")
    @ResponseBody
    public Boolean deleteGoal(@SessionAttribute("member")Member member,
                           @RequestParam("goalKey")String goalKey) throws Exception {
        return goalService.deleteGoal(goalKey);
    }

    @GetMapping("fixGoal")
    @ResponseBody
    public Boolean fixGoal(@SessionAttribute("member")Member member,
                           @RequestParam("goalKey")String goalKey,
                           @RequestParam("color")String color,
                           @RequestParam("title")String title) throws Exception {
        return goalService.fixGoal(member.getId(), title, color, goalKey);
    }

}
