package com.futsch1.medtimer.reminders;

import static com.futsch1.medtimer.ActivityCodes.EXTRA_NOTIFICATION_ID;
import static com.futsch1.medtimer.ActivityCodes.EXTRA_REMINDER_EVENT_ID;
import static com.futsch1.medtimer.ActivityCodes.EXTRA_REMINDER_ID;
import static com.futsch1.medtimer.ActivityCodes.EXTRA_SNOOZE_TIME;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.futsch1.medtimer.LogTags;

import java.time.Instant;

public class SnoozeWork extends RescheduleWork {

    public SnoozeWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();

        int snoozeTime = inputData.getInt(EXTRA_SNOOZE_TIME, 15);
        if (snoozeTime == 0) {
            Intent startActivityIntent = new Intent(context, CustomSnoozeActivity.class);
            startActivityIntent.putExtra(EXTRA_REMINDER_ID, inputData.getInt(EXTRA_REMINDER_ID, 0));
            startActivityIntent.putExtra(EXTRA_REMINDER_EVENT_ID, inputData.getInt(EXTRA_REMINDER_EVENT_ID, 0));
            startActivityIntent.putExtra(EXTRA_NOTIFICATION_ID, inputData.getInt(EXTRA_NOTIFICATION_ID, 0));
            startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(startActivityIntent);
        } else {
            snooze(snoozeTime);
        }

        return Result.success();
    }

    private void snooze(int snoozeTime) {
        Data inputData = getInputData();

        int reminderId = inputData.getInt(EXTRA_REMINDER_ID, 0);
        int reminderEventId = inputData.getInt(EXTRA_REMINDER_EVENT_ID, 0);
        int notificationId = inputData.getInt(EXTRA_NOTIFICATION_ID, 0);
        Instant remindTime = Instant.now().plusSeconds(snoozeTime * 60L);

        schedule(remindTime, reminderId, "snooze", notificationId, reminderEventId);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        if (notificationId != 0) {
            Log.d(LogTags.REMINDER, String.format("Snoozing notification %d", notificationId));
            notificationManager.cancel(notificationId);
        }
    }
}
