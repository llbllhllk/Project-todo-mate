package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;

import javax.servlet.http.HttpServletRequest;

public interface GoalRepository {
    String registerGoal(HttpServletRequest request, String goalType, Goal goal) throws Exception;
}
