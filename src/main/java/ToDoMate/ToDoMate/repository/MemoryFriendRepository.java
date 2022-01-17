package ToDoMate.ToDoMate.repository;

import ToDoMate.ToDoMate.domain.Friend;
import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

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
}
