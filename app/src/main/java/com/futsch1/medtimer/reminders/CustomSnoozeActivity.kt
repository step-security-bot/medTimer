package com.futsch1.medtimer.reminders

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.futsch1.medtimer.R

class CustomSnoozeActivity : Activity() {
    override fun onResume() {
        super.onResume()
        val entries: Array<String> = getSnoozeEntries()
        val values: IntArray = this.resources.getIntArray(R.array.snooze_duration_values)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.snooze_duration)
        builder.setItems(entries) { _: DialogInterface?, which: Int ->
            finish()
        }
        builder.setCancelable(false)
        builder.show()

    }

    private fun getSnoozeEntries(): Array<String> {
        val snoozeDuration: Array<String> =
            this.resources.getStringArray(R.array.snooze_duration)

        return snoozeDuration.sliceArray(IntRange(0, snoozeDuration.size - 2))
    }
}