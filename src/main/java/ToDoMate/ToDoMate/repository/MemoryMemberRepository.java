package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Member;
import ToDoMate.ToDoMate.domain.Nickname;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.Optional;

public class MemoryMemberRepository implements MemberRepository {

    private static final String collectionMember = "member";
    private static final String collectionNickname = "member_nickname";

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
    public Optional<Nickname> getMemberNicknameInformation(String nickname) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(collectionNickname).document(nickname);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        return Optional.ofNullable(documentSnapshot.toObject(Nickname.class));
    }

    @Override
    public String registerNickname(Nickname nickname) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture=
                dbFirestore.collection(collectionNickname).document(nickname.getNickname()).set(nickname);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }


}