package ToDoMate.ToDoMate.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Goal {

    String title;
    String color;
    String memberId;

    public Goal(String title, String color, String memberId) {
        this.title = title;
        this.color = color;
        this.memberId = memberId;
    }
}
