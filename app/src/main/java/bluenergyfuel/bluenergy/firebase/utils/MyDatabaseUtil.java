package bluenergyfuel.bluenergy.firebase.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by jockinjc0 on 5/10/17.
 */

public class MyDatabaseUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;

    }
}
