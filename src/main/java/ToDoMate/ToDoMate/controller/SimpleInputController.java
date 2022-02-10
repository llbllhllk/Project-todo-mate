package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.repository.SimpleInputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleInputController {

    @Autowired
    private final SimpleInputRepository simpleInputRepository;

    public SimpleInputController(SimpleInputRepository simpleInputRepository) {
        this.simpleInputRepository = simpleInputRepository;
    }

    @GetMapping("/addSimpleInput")
    @ResponseBody
    public boolean getAddSimpleInput(@RequestParam("title") String title,@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,@RequestParam("day") String day,
                                    @RequestParam("simpleInputKey") String simpleInputKey, @RequestParam("goalKey") String goalKey) throws Exception{
        simpleInputRepository.registerSimpleInput(title,startDate,endDate,day,simpleInputKey,goalKey);
        return true;
    }

    @GetMapping("/fixStartDateSimpleInput")
    @ResponseBody
    public boolean getFixStartDateSimpleInput(@RequestParam("startDate") String startDate, @RequestParam("goalKey") String goalKey, @RequestParam("simpleInputKey") String simpleInputKey) throws Exception{
        simpleInputRepository.modifySimpleInputStartDate(startDate,goalKey,simpleInputKey);
        return true;
    }

    @GetMapping("/fixEndDateSimpleInput")
    @ResponseBody
    public boolean getFixEndDateSimpleInput(@RequestParam("endDate") String endDate, @RequestParam("goalKey") String goalKey, @RequestParam("simpleInputKey") String simpleInputKey) throws Exception{
        simpleInputRepository.modifySimpleInputEndDate(endDate,goalKey,simpleInputKey);
        return true;
    }

    @GetMapping("/fixDateSimpleInput")
    @ResponseBody
    public boolean getFixDateSimpleInput(@RequestParam("day") String day, @RequestParam("goalKey") String goalKey, @RequestParam("simpleInputKey") String simpleInputKey) throws Exception{
        simpleInputRepository.modifySimpleInputDay(day,goalKey,simpleInputKey);
        return true;
    }

    @GetMapping("/deleteSimpleInput")
    @ResponseBody
    public boolean getDeleteSimpleInput(@RequestParam("simpleInputKey") String simpleInputKey) throws Exception{
        simpleInputRepository.deleteSimpleInput(simpleInputKey);
        return true;
    }

    @GetMapping("/simple-input")
    public String viewSimpleInput() throws Exception{
        return "simple-input";
    }
}
