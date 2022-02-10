package ToDoMate.ToDoMate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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
