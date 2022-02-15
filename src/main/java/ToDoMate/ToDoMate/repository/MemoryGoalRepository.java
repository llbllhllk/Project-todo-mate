package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Goal;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryGoalRepository implements GoalRepository{

    @Override
    public List<Map<String, String>> getGoalList(String memberId) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        Query query = firestore.collection("goal").whereEqualTo("memberId", memberId);
        QuerySnapshot queryDocSnapshot = query.get().get();
        List<Map<String, String>> goalList = new ArrayList<>();
        if (queryDocSnapshot.size()!=0){
            for (Goal goal : queryDocSnapshot.toObjects(Goal.class)) {
                HashMap<String, String> temp = new HashMap<>();
                temp.put("viewId", goal.getViewId());
                temp.put("title", goal.getTitle());
                temp.put("color", goal.getColor());
                goalList.add(temp);
            }
        }
        return goalList;
    }

    @Override
    public Boolean addGoal(String memberId, String title, String color, String goalKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, String> docData = new HashMap<>();
        docData.put("title", title);
        docData.put("color", color);
        docData.put("memberId", memberId);
        docData.put("viewId", goalKey);
        ApiFuture<WriteResult> setGoal = firestore.collection("goal").document(String.valueOf(goalKey)).set(docData);
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
        ApiFuture<WriteResult> updateGoal = firestore.collection("goal").document(String.valueOf(goalKey))
                .set(chageData, SetOptions.merge());
        Timestamp updateTime = updateGoal.get().getUpdateTime();
        if (updateTime.equals(null)) return false;
        else return true;
    }

}
