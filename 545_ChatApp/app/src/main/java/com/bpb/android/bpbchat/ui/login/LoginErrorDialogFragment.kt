package com.bpb.android.bpbchat.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bpb.android.bpbchat.R

class LoginErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.login_error_msg)
            .setPositiveButton(R.string.retry,
                DialogInterface.OnClickListener { dialog, id ->
                    // FIRE ZE MISSILES!
                })
            .setNegativeButton(R.string.exit,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        return builder.create()
    }
}