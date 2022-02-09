package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryGoalRepository implements GoalRepository{

    @Override
    public Boolean addGoal(String memberId, String title, String color, String goalKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, String> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("color", color);
        docData.put("memberId", memberId);
        ApiFuture<WriteResult> setGoal = firestore.collection("goal").document(goalKey).set(docData);
        Timestamp updateTime = setGoal.get().getUpdateTime();
        if (updateTime.equals(null)) return false;
        else return true;
    }

    @Override
    public Boolean deleteGoal(String goalKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection("goal").document(goalKey).delete();
        Timestamp updateTime = delete.get().getUpdateTime();
        if (updateTime.equals(null)) return false;
        else return true;
    }

    @Override
    public Boolean fixGoal(String memberId, String title, String color, String goalKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        HashMap<String, String> chageData = new HashMap<>();
        chageData.put("title", title);
        chageData.put("color", color);
        chageData.put("goalKey", goalKey);
        ApiFuture<WriteResult> updateGoal = firestore.collection("goal").document(goalKey).set(chageData, SetOptions.merge());
        Timestamp updateTime = updateGoal.get().getUpdateTime();
        if (updateTime.equals(null)) return false;
        else return true;
    }

}
