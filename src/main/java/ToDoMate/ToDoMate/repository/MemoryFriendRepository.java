package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryFriendRepository implements FriendRepository {

    private static final String collectionName = "friend";

    @Override
    public Optional<Friend> getFriendList(String id) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(collectionName).document(id);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        return Optional.ofNullable(documentSnapshot.toObject(Friend.class));
    }


//    @Override
//    public List<String> findMemberList(String area, String search) throws Exception {
//        Firestore firestore = FirestoreClient.getFirestore();
//        ApiFuture<QuerySnapshot> allMember = firestore.collection("member").get(); //전체문서
//        List<QueryDocumentSnapshot> allMemberList = allMember.get().getDocuments();
//        List<String> allMemberIdList=new ArrayList<>();
//        for (QueryDocumentSnapshot document : allMemberList) {
//            allMemberIdList.add(document.getId());
//        }
//        return allMemberIdList;
//    }


    @Override
    public List<String> findMemberList(String area, String search) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> allMember = firestore.collection("member").get(); //전체문서
        List<QueryDocumentSnapshot> allMemberList = allMember.get().getDocuments();

        List<String> allMemberNicknameList=new ArrayList<>();
        for (QueryDocumentSnapshot document : allMemberList) {
            Member member = document.toObject(Member.class);
            allMemberNicknameList.add(member.getNickname());
        }
        return allMemberNicknameList;
    }



//    @Override
//    public Member addFriend(Member member, String addMember) throws Exception {
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference documentReference = firestore.collection("friend").document(member.getId()).set(addMember);
//        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
//        DocumentSnapshot documentSnapshot = apiFuture.get();
//        return member;
//    }





    /*
    @Override
    public Member findMemberByEmail(Member member, String search) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(collectionName).document(member.getId());
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        return Optional.ofNullable(documentSnapshot.toObject(Friend.class));
    }

    @Override
    public Member findMemberByNickname(Member member, String search) {
        return null;
    }
*/

}
