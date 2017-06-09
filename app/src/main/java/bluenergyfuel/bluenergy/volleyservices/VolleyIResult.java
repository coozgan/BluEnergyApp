package bluenergyfuel.bluenergy.volleyservices;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by jockinjc on 2/25/2016.
 */
public interface VolleyIResult {
    public void notifySuccess(String requestType, JSONObject response);
    public void notifyError(String requestType, VolleyError error);
}
