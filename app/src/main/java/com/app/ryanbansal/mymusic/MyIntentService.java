package com.app.ryanbansal.mymusic;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by RyanBansal on 6/30/17.
 */

public class MyIntentService extends IntentService {
    /**
     Creates an IntentService.
     Invoked by your subclass's constructor.
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationUtils.clearAllNotifications(MyIntentService.this);
    }
}
