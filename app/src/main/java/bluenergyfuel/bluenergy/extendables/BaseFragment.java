package bluenergyfuel.bluenergy.extendables;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by jockinjc0 on 4/12/17.
 */

public class BaseFragment extends Fragment{

    public void makeToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    public void buttonEnabled(Button button){
        button.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            final Drawable originalDrawable = button.getBackground();
            final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(Color.parseColor("#00ACED")));
            button.setBackground(wrappedDrawable);
        }else {
            button.getBackground().setAlpha(255);
        }
    }

    public void buttonDisabled(Button button){
        button.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            final Drawable originalDrawable = button.getBackground();
            final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(Color.parseColor("#5000ACED")));
            button.setBackground(wrappedDrawable);
        }else {
            button.getBackground().setAlpha(128);
        }
    }
}
