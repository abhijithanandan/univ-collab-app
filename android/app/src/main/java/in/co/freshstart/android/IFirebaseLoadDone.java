package in.co.freshstart.android;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<Posts> postsList);
    void onFirebaseLoadFailed(String message);
}
