package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MemoryMemberRepository implements MemberRepository {

    private static final String collectionMember = "member";

    @Override
    public String registerMember(Member member) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture=
                dbFirestore.collection(collectionMember).document(member.getId()).set(member);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public Optional<Member> getMemberIdInformation(String id) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(collectionMember).document(id);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        return Optional.ofNullable(documentSnapshot.toObject(Member.class));
    }


    @Override
    public Boolean nicknameDuplicateCheck(String nickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents) {
            if(document.toObject(Member.class).getNickname().equals(nickname)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean emailDuplicateCheck(String email) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection("member").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents) {
            if(document.toObject(Member.class).getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }

    /**
     * 데이터베이스에 컬렉션 및 다큐먼트 추가코드
     */

//    HashMap<String,Boolean> map = new HashMap<>();
//        map.put(goalType,false);
//        map.put("basketball",false);
//    HttpSession session = request.getSession();
//    Member member = (Member)session.getAttribute("member");
//    Firestore dbFirestore = FirestoreClient.getFirestore();
//    ApiFuture<WriteResult> collectionsApiFuture=
//            dbFirestore.collection(collectionGoal).document(member.getNickname()).set(map);
//        return collectionsApiFuture.get().getUpdateTime().toString();

}