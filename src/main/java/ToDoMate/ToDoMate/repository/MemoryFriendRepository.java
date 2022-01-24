package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Friend;
import ToDoMate.ToDoMate.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public List<String> getMemberNicknameList(String area, String search) throws Exception {
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
    public List<String> acceptFollower(String memberId, String memberNickname, String followerId, String followerNickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollRef = firestore.collection(collectionName);
        DocumentReference memDoc = friendCollRef.document(memberId);
        DocumentReference followerDoc = friendCollRef.document(followerId);
        Friend memberFriendDoc = memDoc.get().get().toObject(Friend.class);
        Friend followerFriendDoc = followerDoc.get().get().toObject(Friend.class);

        ArrayList<String> updateFollower = new ArrayList<>();
        Map<String, Object> memberFriendUpdate = new HashMap<>();
        Map<String, Object> followerFriendUpdate = new HashMap<>();
        for (String f : memberFriendDoc.getFollower()) {
            if (Objects.equals(f, followerNickname)){
                continue;
            }
            updateFollower.add(f);
        }
        List<String> memFriend= memberFriendDoc.getFriend();
        List<String> follFriend = followerFriendDoc.getFriend();
        memFriend.add(followerNickname);
        memberFriendUpdate.put("friend", memFriend);
        memberFriendUpdate.put("follower", updateFollower);

        follFriend.add(memberNickname);
        followerFriendUpdate.put("friend", follFriend);
        followerFriendUpdate.put("follower", followerFriendDoc.getFollower());
        memDoc.update(memberFriendUpdate);
        followerDoc.update(followerFriendUpdate);

        return updateFollower;
    }



    @Override
    public List<String> refuseFollower(String memberId, String nickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference document = firestore.collection(collectionName).document(memberId);
        Friend friendDoc = document.get().get().toObject(Friend.class);
        ArrayList<String> updateFollower = new ArrayList<>();
        for (String f : friendDoc.getFollower()) {
            if (Objects.equals(f, nickname)){
                continue;
            }
            updateFollower.add(f);
        }

        Map<String, Object> friendUpdate = new HashMap<>();
        friendUpdate.put("friend", friendDoc.getFriend());
        friendUpdate.put("follower", updateFollower);
        document.update(friendUpdate);
        return updateFollower;
    }


    @Override
    public Optional<String> findMemberIdByNickname(String nickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> allMember = firestore.collection("member").get(); //전체문서
        List<QueryDocumentSnapshot> allMemberList = allMember.get().getDocuments();
        String result="";
        for (QueryDocumentSnapshot document : allMemberList) {
            Member member = document.toObject(Member.class);
            if (Objects.equals(member.getNickname(), nickname)){
                result = member.getId();
            }
        }
        return Optional.ofNullable(result);
    }


    @Override
    public Optional<String> findMemberNicknameById(String memberId) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> allMember = firestore.collection("member").get(); //전체문서
        List<QueryDocumentSnapshot> allMemberList = allMember.get().getDocuments();
        String result="";
        for (QueryDocumentSnapshot document : allMemberList) {
            Member member = document.toObject(Member.class);
            if (Objects.equals(member.getId(), memberId)){
                result = member.getNickname();
            }
        }
        return Optional.ofNullable(result);
    }



}
