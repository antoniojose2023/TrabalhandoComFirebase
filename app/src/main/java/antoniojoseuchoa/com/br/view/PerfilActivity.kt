package antoniojoseuchoa.com.br.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.database.Database
import antoniojoseuchoa.com.br.databinding.ActivityPerfilBinding
import antoniojoseuchoa.com.br.dialog.DialogLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class PerfilActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPerfilBinding.inflate(layoutInflater) }
    private val auth = FirebaseAuth.getInstance()
    private lateinit var dialogLoader : DialogLoader


    val fotoImagemUsuario = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
               if(uri != null){
                binding.ivProfile.setImageURI( uri )
                Database.uploadImagemUsuario(uri, this)
                dialogLoader.showDialog()
                Handler(Looper.getMainLooper()).postDelayed({
                    dialogLoader.closeDialog()
                }, 4000)
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialogLoader = DialogLoader(this)
        supportActionBar!!.title = "Perfil"

        if(auth.currentUser != null){
             binding.run {
                 tvNomeUsuario.text = auth.currentUser!!.displayName.toString()
                 tvEmailUsuario.text = auth.currentUser!!.email
             }
        }

        binding.ivGaleria.setOnClickListener {
            fotoImagemUsuario.launch("image/*")
        }

        binding.btSairUsuario.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    fun recuperarImagemUsuario(activity: Activity, circleImageView: CircleImageView){
          Database.recuperarImagemUsuario(activity, circleImageView)
    }

    override fun onStart() {
        super.onStart()
        recuperarImagemUsuario(this, binding.ivProfile)
    }


}