package bluenergyfuel.bluenergy.extendables;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by jockinjc0 on 5/16/17.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
