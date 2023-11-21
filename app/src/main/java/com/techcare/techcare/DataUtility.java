package com.techcare.techcare;

import java.util.Random;

public class DataUtility {
    protected static String currentUserID = "the_cto1";
    protected static String currentUserName = "The CTO";
    protected static String targetUserID = "the_ceo1";
    protected static String targetUserName = "The CEO";
    protected static String call_ID = "the_ceo1_the_cto1";
    protected static final long APP_ID = 2123839746;
    protected static final String APP_SIGN = "19d9efe898b88872ae29dc185adae5520ddb87bd29b717d08db5913d17ea1968";

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
