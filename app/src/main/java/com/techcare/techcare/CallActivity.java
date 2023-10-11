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
        /* Important information */
        long appID = 434501726;
        String appSign = "ea3bef30fb6d3b29b11f2c6840e8923059bab0b8b6615e77b489ed320ddfb6f5";
        String callID = "privateCallRoom101";
        // Generate a random username
        Random random = new Random();
        String userID = String.valueOf(random.nextInt(90000) + 10000);
        String userName = "User " + userID;

        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();
        /* New Code */
        config.bottomMenuBarConfig.buttons = Arrays.asList(ZegoMenuBarButtonName.HANG_UP_BUTTON);
        config.bottomMenuBarConfig.hideByClick = false;
        config.bottomMenuBarConfig.maxCount = 1;
        config.bottomMenuBarConfig.hideAutomatically = false;

        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(
                appID, appSign, callID, userID, userName,config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}