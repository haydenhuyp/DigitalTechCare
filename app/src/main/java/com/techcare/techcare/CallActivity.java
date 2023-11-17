package com.techcare.techcare;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;

import java.util.Arrays;
import java.util.Random;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        addCallFragment();
    }

    public void addCallFragment() {
        long appID = 2123839746;
        String appSign = "19d9efe898b88872ae29dc185adae5520ddb87bd29b717d08db5913d17ea1968";
        // Generate a random username
        String userID = generateUserID();
        String userName = userID + "_Name";
        String callID = "test_call1";

        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();

        config.bottomMenuBarConfig.buttons = Arrays.asList(ZegoMenuBarButtonName.HANG_UP_BUTTON);
        config.bottomMenuBarConfig.hideByClick = false;
        config.bottomMenuBarConfig.maxCount = 1;
        config.bottomMenuBarConfig.hideAutomatically = false;

        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(appID, appSign, userID,
                userName, callID, config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
    private String generateUserID() {
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
