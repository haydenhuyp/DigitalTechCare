package com.techcare.techcare;

import android.util.Log;

import java.util.Random;

public class DataUtility {
    protected static String currentUser = "resident";
    protected static String currentUserID = currentUser.equals("resident") ? "the_resident1" : "the_staff1";
    protected static String currentUserName = currentUser.equals("resident") ? "Resident" : "Staff";
    protected static String targetUserID = currentUser.equals("resident") ? "the_staff1" : "the_resident1";
    protected static String targetUserName = currentUser.equals("resident") ? "Staff" : "Resident";
    protected static String call_ID = "the_residence_staff";
    protected static final long APP_ID = 1283402199;
    protected static final String APP_SIGN = "8023cb1b92249d56467313a4e8d1ccd34c278b6e02f7fc550a8985a955eab013";

    private static String generateUserID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 5) {
            int nextInt = random.nextInt(10);
            if (builder.length() == 0 && nextInt == 0) {
                continue;
            }
            builder.append(nextInt);
        }
        return builder.toString();
    }
}
