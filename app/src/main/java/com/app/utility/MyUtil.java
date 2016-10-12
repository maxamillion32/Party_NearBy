package com.app.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VIJAY on 22-08-2016.
 */
public class MyUtil {

    public static Map<String, String> getCommonApiHeader(){
        Map<String, String> headers = new HashMap<>();
        // Basic Authentication
       // headers.put("Content-Type", "application/json");
        return headers;
    }
}
