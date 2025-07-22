package com.madhavsteel.utils;

import com.madhavsteel.model.Sizes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Constant {

    public static String baseURL = "https://madhavsteel.in/API/V1/api.php/";
    public static String imageBaseURL = "https://madhavsteel.in/admin/";
    public static String credentials = "admin:123";

    //User api's
    public static String register = baseURL + "register";
    public static String login = baseURL + "login";
    public static String resendOTP = baseURL + "resendOTP";
    public static String verifyOTP = baseURL + "verifyOTP";
    public static String generatePin = baseURL + "generatePin";
    public static String forgotPin = baseURL + "forgotPin";
    public static String changePin = baseURL + "changePin";
    public static String getHomeScreenData = baseURL + "getHomeScreenData";
    public static String getCategorySize = baseURL + "getCategorySize";
    public static String calculatePrice = baseURL + "calculatePrice";
    public static String orderInquiry = baseURL + "orderInquiry";
    public static String getAllNotifications = baseURL + "getAllNotifications";
    public static String getMyOrders = baseURL + "getMyOrders";
    public static String updateProfile = baseURL + "updateProfile";
    public static String notificationCount = baseURL + "notificationCount";
    public static String clearNotifications = baseURL + "clearNotifications";
    public static String clearNotification = baseURL + "clearNotification";


    public static JSONArray productArray = null;
    public static JSONArray orderArray = null;
    public static JSONArray notificationArray = null;

    public static JSONObject priceResult = null;

    public static ArrayList<String> selectedSizeList = new ArrayList<>();
    public static ArrayList<String> measurementList = new ArrayList<>();
    public static ArrayList<String> quantityList = new ArrayList<>();
    public static ArrayList<Sizes> sizeList = new ArrayList<>();

    public static boolean selectionChange = false;

    public static int sizeStatus = 1;
    public static int notificationCounts = 0;

    public static String messageEvent = "messageEvent";
    public static String catId = "";
    public static String selectedSize = "";
    public static String selectedQuantity = "";
    public static String selectedMeasurement = "";
}
