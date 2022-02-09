package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.SimpleInput;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

public class MemorySimpleInputRepository implements SimpleInputRepository{

    private static final String collectionSimpleInput = "simpleInput";

    /**
     * 간편입력 등록 메소드
     */

    @Override
    public String registerSimpleInput(SimpleInput simpleInput) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture=
                dbFirestore.collection(collectionSimpleInput).document(simpleInput.getSimpleInputKey()).set(simpleInput);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    /**
     * 간편입력 제목 변경 메소드
     */

    @Override
    public void modifySimpleInputTitle(String title, String goalKey, String simpleInputKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("simpleInput").document(simpleInputKey);
        ApiFuture<WriteResult> future = documentReference.update("title",title);
    }

    /**
     * 간편입력 시작날짜 변경 메소드
     */

    @Override
    public void modifySimpleInputStartDate(String startDate, String goalKey, String simpleInputKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("simpleInput").document(simpleInputKey);
        ApiFuture<WriteResult> future = documentReference.update("startDate",startDate);
    }

    /**
     * 간편입력 종료날짜 변경 메소드
     */

    @Override
    public void modifySimpleInputEndDate(String endDate, String goalKey, String simpleInputKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("simpleInput").document(simpleInputKey);
        ApiFuture<WriteResult> future = documentReference.update("endDate",endDate);
    }

    /**
     * 간편입력 요일변경 메소드
     */

    @Override
    public void modifySimpleInputDay(String day, String goalKey, String simpleInputKey) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("simpleInput").document(simpleInputKey);
        ApiFuture<WriteResult> future = documentReference.update("day",day);
    }

    /**
     * 간편입력 삭제 메소드
     */

    @Override
    public void deleteSimpleInput(String goalKey, String simpleInputKey) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("simpleInput").document(simpleInputKey).delete();
    }
}
