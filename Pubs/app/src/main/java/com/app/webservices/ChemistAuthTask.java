/*
package com.app.webservices;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.thinksys.interfaces.WebServiceInterface;
import com.thinksys.justpharma.R;
import com.thinksys.utils.AppLog;
import com.thinksys.utils.Const;
import com.thinksys.utils.UtilitySingleton;
import com.thinksys.volley.VolleyHandler;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

*/
/**
 * Created by thinksysuser on 10-03-2016.
 *//*

public class ChemistAuthTask {
    private Context ctx;
    private ProgressDialog dialog;
    private static WebServiceInterface mdelegate;

    public ChemistAuthTask(Context ctx) {
        mdelegate = (WebServiceInterface) ctx;
        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);

    }

    public void registrationRequest(int userType, String shopName, boolean isChild,  String address,  String dlNo,  String personName,
                           String mobileNo,  String pinCode,  String landlineNo) {

        JSONObject reqObj = new JSONObject();
        JSONObject merged = new JSONObject();

        try {
            reqObj.put(Const.RegistrationParamsConstantKey.USER_TYPE, userType);
            reqObj.put(Const.RegistrationParamsConstantKey.CHILD_USER, isChild );
            reqObj.put(Const.RegistrationParamsConstantKey.USER_NAME, JSONObject.NULL );
            reqObj.put(Const.RegistrationParamsConstantKey.FULL_NAME, personName );
            reqObj.put(Const.RegistrationParamsConstantKey.PASSWORD, UtilitySingleton.getInstance(ctx).ANDROID_DEVICE_ID );
            reqObj.put(Const.RegistrationParamsConstantKey.CONFIRM_PASSWORD, UtilitySingleton.getInstance(ctx).ANDROID_DEVICE_ID );
            reqObj.put(Const.RegistrationParamsConstantKey.COMPANY_ID, Const.DEFAULT_INTEGER_VALUE );
            reqObj.put(Const.RegistrationParamsConstantKey.SHOP_NAME, shopName );
            reqObj.put(Const.RegistrationParamsConstantKey.DL_NUMBER, dlNo );
            reqObj.put(Const.RegistrationParamsConstantKey.CONTECT_PERSON, personName );
            reqObj.put(Const.RegistrationParamsConstantKey.MOBILE_NUMBER, mobileNo );
            reqObj.put(Const.RegistrationParamsConstantKey.LANDLINE_NUMBER, landlineNo );
            reqObj.put(Const.RegistrationParamsConstantKey.EMAIL_ID, Const.DEFAULT_EMAIL_VALUE);
            reqObj.put(Const.RegistrationParamsConstantKey.DESIGNATION, Const.DEFAULT_INTEGER_VALUE );
            reqObj.put(Const.RegistrationParamsConstantKey.EMP_CODE, JSONObject.NULL);
            reqObj.put(Const.DEVICE_ID, UtilitySingleton.getInstance(ctx).ANDROID_DEVICE_ID );

            AppLog.Log("ChemistUser_Req: ", reqObj.toString());

            JSONObject reqObj2 = new JSONObject();
            reqObj2.put(Const.RegistrationParamsConstantKey.ADDRESS_LINE, address );
            reqObj2.put(Const.RegistrationParamsConstantKey.STREET, JSONObject.NULL );
            reqObj2.put(Const.RegistrationParamsConstantKey.AREA, JSONObject.NULL );
            reqObj2.put(Const.RegistrationParamsConstantKey.SUB_DISTRICT, JSONObject.NULL );
            reqObj2.put(Const.RegistrationParamsConstantKey.DISTRICT, JSONObject.NULL);
            reqObj2.put(Const.RegistrationParamsConstantKey.STATE, JSONObject.NULL );
            reqObj2.put(Const.RegistrationParamsConstantKey.COUNTRY, JSONObject.NULL);
            reqObj2.put(Const.RegistrationParamsConstantKey.POSTAL_CODE, pinCode );

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(reqObj2);

            JSONObject addressObj = new JSONObject();

            addressObj.put(Const.RegistrationParamsConstantKey.USER_ADDRESS, jsonArray);

            AppLog.Log("Address_Req: ", addressObj.toString());

            JSONObject[] objs = new JSONObject[] {reqObj , addressObj};
            for (JSONObject obj : objs) {
                Iterator it = obj.keys();
                while (it.hasNext()) {
                    String key = (String)it.next();
                    merged.put(key, obj.get(key));
                }
            }


            AppLog.Log("TotalData_Req: ", merged.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.COMMON_REGISTER, merged,
                regSuccessListener(), regErrorListener());
        AppLog.Log("jsonObjectRequest: ", jsonObjectRequest.toString());
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }


    private Response.Listener<JSONObject> regSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // mTvResult.setText(response.getString("one"));
                mdelegate.requestCompleted(response.toString());
                if(dialog.isShowing())
                dialog.cancel();

            }
        };
    }

    private Response.Listener<JSONArray> regSuccessListenerArray() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // mTvResult.setText(response.getString("one"));
                mdelegate.requestCompleted(response.toString());
                if(dialog.isShowing())
                    dialog.cancel();

            }
        };
    }

    private Response.ErrorListener regErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mdelegate.requestEndedWithError(error);
                if(dialog.isShowing())
                    dialog.cancel();
            }
        };
    }

    */
/*
       CHEMIST_REGISTRATION_SERVICE_METHOD
     *//*



    public void chemistSignIn(String dlNo, int userType) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.RegistrationParamsConstantKey.USER_TYPE, userType);
            jsonObject.put(Const.RegistrationParamsConstantKey.USER_NAME, dlNo);
            jsonObject.put(Const.RegistrationParamsConstantKey.PASSWORD, UtilitySingleton.getInstance(ctx).ANDROID_DEVICE_ID);
            jsonObject.put(Const.RegistrationParamsConstantKey.GRANT_TYPE, Const.GRANT_TYPE_VALUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLog.Log("LOGIN_REQ: ", jsonObject.toString());

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.setCancelable(false);
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.COMMON_LOGIN, jsonObject,
                regSuccessListener(), regErrorListener());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }




    public void confirmChemistOTP(String mobNo, String userId, String otpCode, String userType, String userMob) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.OTP.ID, userId); //send User Id here after registration
            jsonObject.put(Const.RegistrationParamsConstantKey.USER_TYPE, userType);
            jsonObject.put(Const.OTP.OTP_CODE, otpCode);
            jsonObject.put(Const.RegistrationParamsConstantKey.MOBILE_NUMBER, userMob); //null in case of Admin
            jsonObject.put(Const.RegistrationParamsConstantKey.CONFIRM_MOBILE_NUMBER, mobNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLog.Log("OTP_REQ: " , jsonObject.toString());

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.OTP_CONFIRM, jsonObject,
                regSuccessListener(), regErrorListener());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    public void reConfirmChemistOTP(final String mobNo, final String userId, final int userType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.OTP.ID, userId); //send User Id here after registration
            jsonObject.put(Const.RegistrationParamsConstantKey.USER_TYPE, userType);
            jsonObject.put(Const.OTP.OTP_CODE, JSONObject.NULL);
            jsonObject.put(Const.RegistrationParamsConstantKey.MOBILE_NUMBER, mobNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLog.Log("OTP_REQ: " , jsonObject.toString());

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.OTP_RE_CONFIRM, jsonObject,
                regSuccessListener(), regErrorListener());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }





    */
/*public void deleteProfile(String userType, String uId) {

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Const.RegistrationParamsConstantKey.USER_TYPE, userType);
            reqObj.put(Const.RegistrationParamsConstantKey.USER_ID, uId );

            AppLog.Log("Delete_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.DELETE_ACCOUNT, reqObj,
                regSuccessListener(), regErrorListener());
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }*//*



    public void companyQueryResults(String accessToken, String url) {

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                regSuccessListenerArray(), regErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UtilitySingleton.getInstance(ctx).ACCESS_TOKEN);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }



    public void companyGetTeamMembers(String accessToken, String url) {

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                regSuccessListenerArray(), regErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UtilitySingleton.getInstance(ctx).ACCESS_TOKEN);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }


    public void getConsumersOrders(String url) {

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                regSuccessListenerArray(), regErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UtilitySingleton.getInstance(ctx).ACCESS_TOKEN);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }


    */
/*
    UPDATE ORDER STATUS
     *//*


    public void updateOrderStatus(String orderId, String statusId, String comment) {

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Const.RegistrationParamsConstantKey.ORDER_ID, orderId);
            reqObj.put(Const.RegistrationParamsConstantKey.STATUS_ID, statusId );
            reqObj.put(Const.RegistrationParamsConstantKey.COMMENT, comment );

            AppLog.Log("Update_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        dialog.show();
        RequestQueue queue = VolleyHandler.mGetRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Const.ServiceType.DELETE_ACCOUNT, reqObj,
                regSuccessListener(), regErrorListener());
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Const.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }






}
*/
