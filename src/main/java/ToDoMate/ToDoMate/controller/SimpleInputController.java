package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.domain.SimpleInput;
import ToDoMate.ToDoMate.repository.SimpleInputRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
                                    @RequestParam("simpleInputKey") String simpleInputKey,
                                     @RequestParam("goalKey") String goalKey, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        String memberId = member.getId();
        simpleInputRepository.registerSimpleInput(title,startDate,endDate,day,simpleInputKey,goalKey,memberId);
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

    @GetMapping("/simpleInput")
    public String viewSimpleInput(Model model) throws Exception{
        ArrayList<SimpleInput> simpleInputs = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("simpleInput").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            simpleInputs.add(document.toObject(SimpleInput.class));
            System.out.println(document.toObject(SimpleInput.class).getTitle());
        }
        model.addAttribute("simpleInputList",simpleInputs);
        return "simpleInput";
    }
}
