package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.database.Database
import antoniojoseuchoa.com.br.databinding.ActivityAdicionarAnotacaoBinding
import antoniojoseuchoa.com.br.dialog.DialogLoader
import antoniojoseuchoa.com.br.model.Anotacao
import java.util.UUID

class AdicionarAnotacaoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAdicionarAnotacaoBinding.inflate(layoutInflater) }
    private lateinit var dialogLoader: DialogLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialogLoader = DialogLoader(this)

        supportActionBar!!.title = "Anotações"

        binding.buttonSalvar.setOnClickListener{
            save()
        }

    }

    fun save(){
        dialogLoader.showDialog()
        if(binding.editTitle.text.toString().isEmpty() || binding.editDescritpion.text.toString().isEmpty()){
                showMessage("Campos vázios")
                dialogLoader.closeDialog()
        }else{
            val anotacao = Anotacao()
            binding.run {
                anotacao.id = UUID.randomUUID().toString()
                anotacao.title = editTitle.text.toString()
                anotacao.description = editDescritpion.text.toString()
            }

           val response = Database.saveAnnotation( anotacao )
           Handler(Looper.getMainLooper()).postDelayed({
                redirecionarTelaPrincipal()
                dialogLoader.closeDialog()
           }, 3000)


        }
    }

    fun showMessage(mensagem: String){
            Toast.makeText(this, "$mensagem", Toast.LENGTH_LONG).show()
    }

    fun redirecionarTelaPrincipal(){
        startActivity(Intent(this, TelaPrincipalActivity::class.java))
    }

}