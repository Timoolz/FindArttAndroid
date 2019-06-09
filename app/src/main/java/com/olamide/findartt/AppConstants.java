package com.olamide.findartt;

public class AppConstants {
    //GOOGLE APP ENGINE IS EXPENSIVE FOR NOW!!!!
    //public static final String FINDARTT_BASE_URL = "https://findartt.appspot.com/api/";
    public static final String FINDARTT_BASE_URL = "https://findartt.herokuapp.com/api/";
    public static final String DATABASE_NAME = "FindArtt";

    public static final String GOOGLE_WEB_CLIENT_ID = BuildConfig.GOOGLE_WEB_CLIENT_ID;
    public static final String GOOGLE_SERVER_CLIENT_ID = BuildConfig.GOOGLE_SERVER_CLIENT_ID;
    public final static int RC_SIGN_IN = 9001;
    public final static String TYPE_STRING = "typeString";

    public static final String ACCESS_TOKEN_STRING = "accessToken";
    public static final String USEREMAIL_STRING = "userName";
    public static final String USERPASSWORD_STRING = "userPassword";

    public static final String CURRENT_USER = "currentUser";

    public final static String ARTWORK_STRING = "artwork";

    public static final String BUNDLE = "bundle";

    public static final String EXO_PLAYER_USER_AGENT = BuildConfig.APPLICATION_ID;
    //    public static final int EXO_PLAYER_VIDEO_CACHE_DURATION = 10 * 1024 * 1024;
    public static final int EXO_PLAYER_VIDEO_CACHE_DURATION = 0;


    public static final String CURRENT_POSITION_KEY = "exo_current_position";
    public static final String EXO_PREF_NAME = "exo_shared_pref";

    public static final int GLIDE_DEFAULT_TIMEOUT = 60;

    public static final String INTENT_ACTION_STRING = "Complete action using";

    public static final int RC_IMAGE_PICKER = 21;
    public static final int RC_GALLERY = 211;
    public static final int RC_CAMERA = 212;

    public static final String IMAGE_URI_PATH = "path";

    public static final String FIREBASE_USER_PATH = "users";
    public static final String FIREBASE_ARTWORK_PATH = "artworks";
    public static final String FIREBASE_USER_IMAGE_PATH =FIREBASE_USER_PATH + "/images";
    public static final String FIREBASE_ARTWORK_IMAGE_PATH =FIREBASE_ARTWORK_PATH + "/images";
    public static final String FIREBASE_ARTWORK_VIDEO_PATH =FIREBASE_ARTWORK_PATH + "/videos";

}
