package com.app.utility;

/**
 * Created by ram on 21/05/16.
 */
public class Constant {

    public static final String TAG = "PUBS NEARBY";
    public static final int MY_SOCKET_TIMEOUT_MS = 30000;


    public static int COUPLE_ENTRY = 1500;
    public static int MALE_ENTRY = 1000;
    public static int FEMALE_ENTRY = 500;


    public class ServiceCodeAccess {
        public static final int FACEBOOK_LOGIN = 1;
        public static final int GOOGLE_LOGIN = 2;
        public static final int MANUAL_LOGIN = 3;
        public static final int FACEBOOK_PROFILE = 4;
        public static final int LOGIN = 5;
        public static final int REGISTRATION = 6;
        public static final int EVENT_LIST = 7;
        public static final int FORGOT_PASSWORD = 8;
        public static final int EVENT_DETAILS = 9;
        public static final int EVENT_RATING = 10;
        public static final int BOOK_EVENT = 11;
        public static final int BOOK_HISTORY = 12;
    }

    public class ServiceType {

        private static final String HOST_URL = "http://partynearby.com/phones";

        /*
            ACCOUNTS AUTH
         */
        private static final String BASE_URL = HOST_URL + "/Users";
        public static final String LOGIN = BASE_URL + "/login.json";
        public static final String REGISTER = BASE_URL + "/add.json";
        public static final String OTP_CONFIRM = BASE_URL + "/Account/ConfirmMobile"; //Confirm mobile after click
        public static final String OTP_RE_CONFIRM = BASE_URL + "/Account/ResendOTP"; //Get otp if not received
        public static final String FORGOT_PASSWORD = BASE_URL + "/forgot_password.json";
        public static final String EDIT_USER_INFO = BASE_URL + "/edit/1.json";

        public static final String EVENT_LIST = HOST_URL + "/Events/event_list/"; //http://partynearby.com/phones/Events/event_list.json
        public static final String EVENT_DETAILS = HOST_URL + "/Events/event_details/"; //http://partynearby.com/phones/Events/event_details/1.json
        public static final String ADD_RATING = HOST_URL + "/Events/add_rating.json"; //http://partynearby.com/phones/Events/add_rating.json method post
        public static final String IMAGE_BASE_URL = "http://partynearby.com/appAdmin/img/events/original/";
        public static final String GET_RATINGS = HOST_URL + "/Events/rating/"; //http://partynearby.com/phones/Events/rating/1.json method get 1 is event id
        public static final String BOOK_EVENT = "http://partynearby.com/phones/Events/booking_add.json";
        public static final String BOOKING_HISTORY = "http://partynearby.com/phones/Events/booking_history.json";
        public static final String PROFILE_UPDATE = "http://partynearby.com/phones/Users/edit/{user_id}.json";

        public static final String NEAR_BY = "http://partynearby.com/phones/Events/event_locate.json";
        public static final String SEARCH_EVENT = "http://partynearby.com/phones/Events/search/";
    }


    public class KeyConstant{
        public static final String EMAIL_ID = "email_id";
        public static final String PASSWORD = "password";
        public static final String DEVICE_ID = "device_id";

        public static final String USER_ID = "user_id";
        public static final String EVENT_ID = "event_id";
        public static final String CEC = "couple_entry_count";
        public static final String MEC = "male_entry_count";
        public static final String FEC = "female_entry_count";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String PROFILE_PIC = "profile_pic";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String MOBILE_NUMBER = "mobile_no";
        public static final String DOB = "dob";
        public static final String ANNIVERSARY_DATE = "anniversary_date";
        public static final String OTP_CODE = "otp_code";
        public static final String VIA_FB = "via_fb";
        public static final String VIA_GOOGLE = "via_google";
        public static final String TERMS = "term_status";

        public static final String MESSAGE = "message";

        public static final String DEVICE_VALUE = "password";

        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";


    }

    }
