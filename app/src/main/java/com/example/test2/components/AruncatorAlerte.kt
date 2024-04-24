package com.example.test2.components

import android.app.AlertDialog
import android.view.WindowManager
import com.example.test2.MainActivity


//AruncaAlerta(alertMessage: String, instanceOfMain: MainActivity)
/*
alertMessage -> mesajul care trebuie aratat
instanceOfMain -> instanta activitatii main(in cazul MainActivity.kt este chiar insanta clasei MainActivity)
 */
abstract class AruncatorAlerte {
    companion object{
        fun AruncaAlerta(alertMessage: String, instanceOfMain: MainActivity) {
            // Disable background input

            instanceOfMain.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val builder = AlertDialog.Builder(instanceOfMain)
            builder.setMessage(alertMessage)
                .setTitle("Alerta")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which ->
                    println("salut")
                    // Re-enable background input
                    instanceOfMain.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                    // Re-enable background input
                    instanceOfMain.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}