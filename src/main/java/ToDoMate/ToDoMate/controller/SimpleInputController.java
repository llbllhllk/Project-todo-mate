package ToDoMate.ToDoMate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleInputController {

    @GetMapping("/addSimpleInput")
    @ResponseBody
    public boolean getAddSimpleInput(@RequestParam("title") String title) throws Exception{
        return true;
    }
}
