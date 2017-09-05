package ph.com.jeffreyvcabrera.stlukesdev.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;

/**
 * Created by Jeffrey on 9/6/2017.
 */

public class VolleyAPI {

    final String TAG = "response";
    AsyncTaskListener callback;
    Context context;
    String url;

    public VolleyAPI(final Context context, AsyncTaskListener cb, String url) {
        this.callback = cb;
        this.context = context;
        this.url = url;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        callback.onTaskComplete(response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onTaskComplete("error");
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to the Internet. Please check your connection.";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again later.";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to the Internet. Please check your connection.";
                } else if (volleyError instanceof ParseError) {
                    message = "Connection Error. Please try again later.";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to the Internet. Please check your connection.";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut. Please check your internet connection.";
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
