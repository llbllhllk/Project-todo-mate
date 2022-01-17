package ToDoMate.ToDoMate.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinForm {

    private String id;
    private String password;
    private String checkPassword;
    private String name;
    private String nickname;
    private String email;

}