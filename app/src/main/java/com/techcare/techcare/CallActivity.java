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
        long appID = 434501726;
        String appSign = "ea3bef30fb6d3b29b11f2c6840e8923059bab0b8b6615e77b489ed320ddfb6f5";
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
