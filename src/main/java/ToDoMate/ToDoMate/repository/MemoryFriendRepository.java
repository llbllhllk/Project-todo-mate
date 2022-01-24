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
import java.sql.Array;
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
    public List<String> deleteFriend(String memberId, String memberNickname, String deleteId, String deleteNickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference memDocRef = firestore.collection(collectionName).document(memberId);
        DocumentReference delDocRef = firestore.collection(collectionName).document(deleteId);
        Friend memDoc = memDocRef.get().get().toObject(Friend.class);
        Friend deleteDoc = delDocRef.get().get().toObject(Friend.class);
        ArrayList<String> afterMemDelete = new ArrayList<>();
        ArrayList<String> afterdelDelete = new ArrayList<>();
        for (String f : memDoc.getFriend()) {
            if (Objects.equals(f, deleteNickname)){
                continue;
            }
            afterMemDelete.add(f);
        }

        for (String f : deleteDoc.getFriend()) {
            if (Objects.equals(f, memberNickname)){
                continue;
            }
            afterdelDelete.add(f);
        }

        Map<String, Object> delUpdate = new HashMap<>();
        Map<String, Object> memUpdate = new HashMap<>();
        memUpdate.put("friend", afterMemDelete);
        memUpdate.put("follower", memDoc.getFollower());
        memUpdate.put("followee", memDoc.getFollowee());
        memDocRef.update(memUpdate);

        delUpdate.put("friend", afterdelDelete);
        delUpdate.put("follower", deleteDoc.getFollower());
        delUpdate.put("followee", deleteDoc.getFollowee());
        delDocRef.update(delUpdate);
        return afterMemDelete;
    }


    @Override
    public List<String> acceptFollower(String memberId, String memberNickname, String followerId, String followerNickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference friendCollRef = firestore.collection(collectionName);
        DocumentReference memDoc = friendCollRef.document(memberId);
        DocumentReference followerDoc = friendCollRef.document(followerId);
        Friend memberFriendDoc = memDoc.get().get().toObject(Friend.class);
        Friend followerFriendDoc = followerDoc.get().get().toObject(Friend.class);

        ArrayList<String> updateFollower = new ArrayList<>();
        ArrayList<String> updateFollowee = new ArrayList<>();
        Map<String, Object> memberFriendUpdate = new HashMap<>();
        Map<String, Object> followerFriendUpdate = new HashMap<>();
        for (String f : memberFriendDoc.getFollower()) {
            if (Objects.equals(f, followerNickname)){
                continue;
            }
            updateFollower.add(f);
        }
        for (String s : followerFriendDoc.getFollowee()) {
            if (Objects.equals(s, memberNickname)){
                continue;
            }
            updateFollowee.add(s);
        }
        List<String> memFriend= memberFriendDoc.getFriend();
        List<String> follFriend = followerFriendDoc.getFriend();
        memFriend.add(followerNickname);
        memberFriendUpdate.put("friend", memFriend);
        memberFriendUpdate.put("follower", updateFollower);
        memberFriendUpdate.put("followee", memberFriendDoc.getFollowee());

        follFriend.add(memberNickname);
        followerFriendUpdate.put("friend", follFriend);
        followerFriendUpdate.put("follower", followerFriendDoc.getFollower());
        followerFriendUpdate.put("followee", updateFollowee);
        memDoc.update(memberFriendUpdate);
        followerDoc.update(followerFriendUpdate);

        return updateFollower;
    }



    @Override
    public List<String> refuseFollower(String memberId, String memberNickname, String refuseId, String refuseNickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collection = firestore.collection(collectionName);
        DocumentReference memdDoc = collection.document(memberId);
        DocumentReference refuseDoc = collection.document(refuseId);
        Friend memFriend = memdDoc.get().get().toObject(Friend.class);
        Friend refuseFriend = refuseDoc.get().get().toObject(Friend.class);

        ArrayList<String> updateFollower = new ArrayList<>();
        ArrayList<String> updateFollowee = new ArrayList<>();
        for (String f : memFriend.getFollower()) {
            if (Objects.equals(f, refuseNickname)){
                continue;
            }
            updateFollower.add(f);
        }

        for (String s : refuseFriend.getFollowee()) {
            if (Objects.equals(s, memberNickname)){
                continue;
            }
            updateFollowee.add(s);
        }

        Map<String, Object> memUpdate = new HashMap<>();
        Map<String, Object> refuseUpdate = new HashMap<>();
        memUpdate.put("friend", memFriend.getFriend());
        memUpdate.put("follower", updateFollower);
        memUpdate.put("followee", memFriend.getFollowee());
        memdDoc.update(memUpdate);

        refuseUpdate.put("friend", refuseFriend.getFriend());
        refuseUpdate.put("follower", refuseFriend.getFollower());
        refuseUpdate.put("followee", updateFollowee);
        refuseDoc.update(refuseUpdate);

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
