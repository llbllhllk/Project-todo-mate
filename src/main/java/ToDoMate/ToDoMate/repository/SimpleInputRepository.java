package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.SimpleInput;

public interface SimpleInputRepository {

    String registerSimpleInput(String title, String startDate, String endDate, String day, String simpleInputKey, String goalKey) throws Exception;
    void modifySimpleInputTitle(String title, String goalKey, String simpleInputKey) throws Exception;
    void modifySimpleInputStartDate(String startDate, String goalKey, String simpleInputKey) throws Exception;
    void modifySimpleInputEndDate(String endDate, String goalKey, String simpleInputKey) throws Exception;
    void modifySimpleInputDay(String day, String goalKey, String simpleInputKey) throws Exception;
    void  deleteSimpleInput(String simpleInputKey) throws Exception;
}