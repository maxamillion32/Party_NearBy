package com.app.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.app.interfaces.WebServiceInterface;
import com.app.partynearby.MyApplication;
import com.app.partynearby.R;
import com.app.utility.AppLog;
import com.app.utility.Constant;
import com.app.utility.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ram on 21/05/16.
 */
public class AuthorizationWebServices {
    private Context ctx;
    private ProgressDialog dialog;
    private WebServiceInterface mdelegate;
    private int mStatusCode = 0;

    public AuthorizationWebServices(Context ctx) {
        mdelegate = (WebServiceInterface) ctx;
        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);

    }


    public AuthorizationWebServices(Context ctx, Fragment fragment) {
        mdelegate = (WebServiceInterface) fragment;
        this.ctx = ctx;
        this.dialog = new ProgressDialog(ctx);
    }



 //TODO USER MANUAL REGISTRATION
    public void UserRegistrationRequest(String fname, String lname, String mobNo,
                                    String email, String password,
                                        String dob, String annivDate, String viaGoogle, String viaFb, boolean isTearms) {

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Constant.KeyConstant.FIRST_NAME, fname);
            reqObj.put(Constant.KeyConstant.LAST_NAME, lname );
            reqObj.put(Constant.KeyConstant.MOBILE_NUMBER, mobNo);
            reqObj.put(Constant.KeyConstant.EMAIL_ID, email );
            reqObj.put(Constant.KeyConstant.PASSWORD, password);
            reqObj.put(Constant.KeyConstant.DEVICE_ID, "password");
            reqObj.put(Constant.KeyConstant.DOB, dob);
            reqObj.put(Constant.KeyConstant.ANNIVERSARY_DATE, annivDate );
            reqObj.put(Constant.KeyConstant.VIA_FB, viaGoogle );
            reqObj.put(Constant.KeyConstant.VIA_GOOGLE, viaFb );
            reqObj.put(Constant.KeyConstant.TERMS, isTearms );

            AppLog.Log("Register_Req: ", reqObj.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

       RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.REGISTER, reqObj,
                regSuccessListener(Constant.ServiceCodeAccess.REGISTRATION), regErrorListener(Constant.ServiceCodeAccess.REGISTRATION));

       /* {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };*/

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    //TODO USER LOGIN VIA FACEBOOK AND GOOGLE

    public void UserLoginRequest(String fname, String lname, String dob, int fbStatus, int googleStatus,  String email, String password
                                 , boolean terms) {

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Constant.KeyConstant.FIRST_NAME, fname );
            reqObj.put(Constant.KeyConstant.LAST_NAME, lname );
            reqObj.put(Constant.KeyConstant.EMAIL_ID, email );
            reqObj.put(Constant.KeyConstant.DOB, dob );
            reqObj.put(Constant.KeyConstant.PASSWORD, password);
            reqObj.put(Constant.KeyConstant.DEVICE_ID, Constant.KeyConstant.DEVICE_VALUE);
            reqObj.put(Constant.KeyConstant.VIA_FB, fbStatus);
            reqObj.put(Constant.KeyConstant.VIA_GOOGLE, googleStatus);
            reqObj.put(Constant.KeyConstant.TERMS, terms);

            AppLog.Log("Login_Req: ", reqObj.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Constant.ServiceType.LOGIN, reqObj,
                    regSuccessListener(Constant.ServiceCodeAccess.LOGIN), regErrorListener(Constant.ServiceCodeAccess.LOGIN));
           /* {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return MyUtil.getCommonApiHeader();
                }
            };*/

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
    }

    public void UserMannualLoginRequest(String fname, String lname, String dob, int fbStatus, int googleStatus,  String email, String password
            , boolean terms) {

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Constant.KeyConstant.FIRST_NAME, fname );
            reqObj.put(Constant.KeyConstant.LAST_NAME, lname );
            reqObj.put(Constant.KeyConstant.EMAIL_ID, email );
            reqObj.put(Constant.KeyConstant.DOB, dob );
            //reqObj.put(Constant.KeyConstant.PASSWORD, password);
            reqObj.put(Constant.KeyConstant.DEVICE_ID, Constant.KeyConstant.DEVICE_VALUE);
            reqObj.put(Constant.KeyConstant.VIA_FB, fbStatus);
            reqObj.put(Constant.KeyConstant.VIA_GOOGLE, googleStatus);
            reqObj.put(Constant.KeyConstant.TERMS, terms);

            AppLog.Log("Login_Req: ", reqObj.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.LOGIN, reqObj,
                regSuccessListener(Constant.ServiceCodeAccess.LOGIN), regErrorListener(Constant.ServiceCodeAccess.LOGIN));
           /* {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return MyUtil.getCommonApiHeader();
                }
            };*/

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    //TODO USER FORGOT PASSWORD

    public void ForgotPasswordService(String email) {
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put(Constant.KeyConstant.EMAIL_ID, email );
            reqObj.put(Constant.KeyConstant.DEVICE_ID, Constant.KeyConstant.DEVICE_VALUE);

            AppLog.Log("Forgot_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.FORGOT_PASSWORD, reqObj,
                regSuccessListener(Constant.ServiceCodeAccess.FORGOT_PASSWORD), regErrorListener(Constant.ServiceCodeAccess.FORGOT_PASSWORD)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    //TODO Event details

    public void EventDetailsService(int input) {

       // dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
       // dialog.show();
        String tag_reg = "json_obj_req";
        String url = Constant.ServiceType.EVENT_DETAILS + input+ ".json";
        AppLog.Log("URL>>> ", url);
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null,
                regSuccessListener(Constant.ServiceCodeAccess.EVENT_DETAILS), regErrorListener(Constant.ServiceCodeAccess.EVENT_DETAILS)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }


    //TODO Event Ratings

    public void EventRatingServie(int input) {

       // dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        String tag_reg = "json_obj_req";
        String url = Constant.ServiceType.GET_RATINGS + input+ ".json";
        AppLog.Log("URL>>> ", url);
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                regSuccessListener(Constant.ServiceCodeAccess.EVENT_RATING), regErrorListener(Constant.ServiceCodeAccess.EVENT_RATING)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }




    //TODO USER FORGOT PASSWORD

    public void EventListService(String input) {
        AppLog.Log("URL: ", Constant.ServiceType.EVENT_LIST + input);

       // dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.ServiceType.EVENT_LIST + input, null,
                regSuccessListener(Constant.ServiceCodeAccess.EVENT_LIST), regErrorListener(Constant.ServiceCodeAccess.EVENT_LIST)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }




    public void GETFacebookProfileImage(String url) {
        AppLog.Log("url: ", url);

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null,
                regSuccessListener(Constant.ServiceCodeAccess.FACEBOOK_PROFILE), regErrorListener(Constant.ServiceCodeAccess.FACEBOOK_PROFILE)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }



    private Response.Listener<JSONObject> regSuccessListener(final int serviceCode) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mdelegate.requestCompleted(response.toString(), serviceCode);
                // mTvResult.setText(response.getString("one"));

                if(dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }

            }


        };

    }


    private Response.Listener<String> regSuccessListenerString(final int serviceCode) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // mTvResult.setText(response.getString("one"));
                mdelegate.requestCompleted(response.toString(), serviceCode);
                if(dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }

            }
        };
    }

    private Response.Listener<JSONArray> regSuccessListenerArray(final int serviceCode) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // mTvResult.setText(response.getString("one"));
                AppLog.Log("SuccessServiceResponse : ", response.toString());
                mdelegate.requestCompleted(response.toString(), serviceCode);
                if(dialog.isShowing())
                    dialog.cancel();

            }
        };
    }

    private Response.ErrorListener regErrorListener(final int serviceCode) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError newerror = new VolleyError(new String(error.networkResponse.data));
                    error = newerror;

                }
                VolleyLog.d("ErrorServiceResponse: ", "Error: " + error.getMessage());
                mdelegate.requestEndedWithError(error, serviceCode);
                if(dialog.isShowing())
                    dialog.cancel();
            }
        };
    }



    /*
    STRING REQUEST
     */

    void StringReqest() {
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        String url = "http://google.com";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.Log("onResponse", response);
                AppLog.Log("statusCode", String.valueOf(mStatusCode));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Log("onErrorResponse", error.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }
        };



        queue.add(request);
    }



    public void postReg(String endpoint, String email, String password)
            throws IOException {

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches (false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Constant.KeyConstant.EMAIL_ID, email );
            reqObj.put(Constant.KeyConstant.PASSWORD, password);
            reqObj.put(Constant.KeyConstant.DEVICE_ID, "password");

            AppLog.Log("Login_Req: ", reqObj.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        wr.writeBytes(reqObj.toString());

        wr.flush();
        wr.close();

        int status = connection.getResponseCode();
        AppLog.Log("StatusCode", status +"");

        /*URL url;

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.KeyConstant.EMAIL_ID, email);
        params.put(Constant.KeyConstant.PASSWORD, password);
        params.put(Constant.KeyConstant.DEVICE_ID, "password");

        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        AppLog.Log("PARAM", "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type",
             //       "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Content-Type","application/json");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            AppLog.Log("StatusCode", status +"");
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }*/
    }



    //TODO BOOK EVENT


    public void BookEventService(String uId, String eventId, String cEc, String fEc, String mEc) {
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put(Constant.KeyConstant.USER_ID, uId);
            reqObj.put(Constant.KeyConstant.EVENT_ID, eventId);
            reqObj.put(Constant.KeyConstant.CEC, cEc);
            reqObj.put(Constant.KeyConstant.FEC, fEc);
            reqObj.put(Constant.KeyConstant.MEC, mEc);

            AppLog.Log("EventBook_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.BOOK_EVENT, reqObj,
                regSuccessListener(Constant.ServiceCodeAccess.BOOK_EVENT), regErrorListener(Constant.ServiceCodeAccess.BOOK_EVENT)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    public void EventHistoryService(String uId) {
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put(Constant.KeyConstant.USER_ID, uId);

            AppLog.Log("EventHistory_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        String tag_reg = "json_obj_req";

        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.BOOKING_HISTORY, reqObj,
                regSuccessListener(Constant.ServiceCodeAccess.BOOK_HISTORY), regErrorListener(Constant.ServiceCodeAccess.BOOK_HISTORY)) {
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }



}
