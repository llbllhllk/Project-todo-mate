package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Goal;

import java.util.List;
import java.util.Map;

public interface GoalRepository {

    List<Map<String, String>> getGoalList(String memberId) throws Exception;
    Boolean addGoal(String memberId, String title, String color, String goalKey) throws Exception;
    Boolean deleteGoal(String goalKey) throws Exception;
    Boolean fixGoal(String memberId, String title, String color, String goalKey) throws Exception;
}
