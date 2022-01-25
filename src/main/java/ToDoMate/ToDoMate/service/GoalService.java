package ToDoMate.ToDoMate.service;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.repository.GoalRepository;

import javax.servlet.http.HttpServletRequest;

public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public void plusGoal(String goalType, HttpServletRequest request, Goal goal) throws Exception{
        goalRepository.registerGoal(request,goalType,goal);
    }
}
