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
    String viewId;

}
