package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class MemoryGoalRepository implements GoalRepository{

    private static final String collectionGoal = "goal";

    @Override
    public String registerGoal(HttpServletRequest request,String goalType,Goal goal) throws Exception {
        HashMap<String,Boolean> map = new HashMap<>();
        map.put(goalType,false);
        map.put("basketball",false);
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture=
                dbFirestore.collection(collectionGoal).document(member.getNickname()).set(map);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }
}
