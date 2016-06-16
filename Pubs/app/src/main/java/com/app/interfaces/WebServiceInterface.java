package com.app.interfaces;

import com.android.volley.VolleyError;

import java.util.Objects;

/**
 * Created by Vijay on 11-03-2016.
 */
public interface WebServiceInterface {
    public void requestCompleted(String obj, int serviceCode);
    public void requestEndedWithError(VolleyError error, int errorCode);
}
