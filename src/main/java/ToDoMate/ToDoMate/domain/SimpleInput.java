package ToDoMate.ToDoMate.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleInput {
    private String title;   // 제목
    private String startDate;   //  시작날짜
    private String endDate; //  종료날짜
    private String day; // 요일
    private String goalKey; // 목표랑 연관지을 목표키
    private String simpleInputKey;  // 간편입력 키
    private String memberId;    // 멤버 아이디
}
