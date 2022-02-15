package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.repository.GoalRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoalService {

    @Autowired
    private final GoalRepository goalRepository;

    public List<Map<String, String>> getGoalList(String memberId) throws Exception {
        return goalRepository.getGoalList(memberId);
    }

    public Boolean addGoal(String memberId, String title, String color, Integer viewId) throws Exception {
        return goalRepository.addGoal(memberId, title, color, String.valueOf(viewId));
    }

    public Boolean deleteGoal(Integer viewId) throws Exception {
        return goalRepository.deleteGoal(String.valueOf(viewId));
    }

    public Boolean fixGoal(String memberId, String title, String color, Integer viewId) throws Exception {
        return goalRepository.fixGoal(memberId, title, color, String.valueOf(viewId));
    }
}
