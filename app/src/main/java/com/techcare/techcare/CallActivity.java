package com.techcare.techcare;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CallActivity extends AppCompatActivity {

    private final long APP_ID = 2123839746;
    private final String APP_SIGN = "19d9efe898b88872ae29dc185adae5520ddb87bd29b717d08db5913d17ea1968";
    private String userID = generateUserID();
    private String userName = userID + "_Name";
    private  String callID = "test_call1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        /*
        String targetUserID = "the_cto"; // The ID of the user you want to call.
        String targetUserName = "the_cto"; // The username of the user you want to call.
        ZegoSendCallInvitationButton btnCall = new ZegoSendCallInvitationButton(this);
        btnCall.setIsVideoCall(true);
        btnCall.setResourceID("zego_uikit_call");
        btnCall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));
        */
        addCallFragment();
    }

    public void addCallFragment() {
        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();

        config.bottomMenuBarConfig.buttons = Arrays.asList(ZegoMenuBarButtonName.HANG_UP_BUTTON);
        config.bottomMenuBarConfig.hideByClick = false;
        config.bottomMenuBarConfig.maxCount = 1;
        config.bottomMenuBarConfig.hideAutomatically = false;

        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(APP_ID, APP_SIGN, userID,
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }


}
