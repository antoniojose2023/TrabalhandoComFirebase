package antoniojoseuchoa.com.br.dialog

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import antoniojoseuchoa.com.br.databinding.DialogLoaderBinding

class DialogLoader(val activity: Activity) {

    private val layoutInflater = LayoutInflater.from(activity)
    private val binding = DialogLoaderBinding.inflate( layoutInflater )

    private val alertDialog = AlertDialog.Builder(activity)
        .setView(binding.root)
        .setCancelable(false)
        .create()

    fun showDialog(){
        alertDialog.show()
    }

    fun closeDialog(){
        alertDialog.dismiss()
    }

}