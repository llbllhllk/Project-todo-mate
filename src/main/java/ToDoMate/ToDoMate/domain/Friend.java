package ToDoMate.ToDoMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Friend {
    private List<String> friend;

    private List<String> follower;
}
