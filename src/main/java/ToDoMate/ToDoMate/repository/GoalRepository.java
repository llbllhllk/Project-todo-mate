package ToDoMate.ToDoMate.repository;

public interface GoalRepository {

    Boolean addGoal(String memberId, String title, String color, String goalKey) throws Exception;
    Boolean deleteGoal(String goalKey) throws Exception;
    Boolean fixGoal(String memberId, String title, String color, String goalKey) throws Exception;
}
