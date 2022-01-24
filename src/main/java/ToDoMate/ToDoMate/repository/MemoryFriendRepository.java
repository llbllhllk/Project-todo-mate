package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.text.html.Option;
import java.util.*;

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


    @Override
    public List<String> refuseFollower(String memberId, String nickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference document = firestore.collection(collectionName).document(memberId);
        Friend friendDoc = document.get().get().toObject(Friend.class);
        friendDoc.getFollower().remove(nickname);
        System.out.println("document.get().get().toObject(Friend.class = " + document.get().get().toObject(Friend.class).getFollower());
        return document.get().get().toObject(Friend.class).getFollower();
    }



}
