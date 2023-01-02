package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.database.Database
import antoniojoseuchoa.com.br.databinding.ActivityAtualizarAnotacaoBinding
import antoniojoseuchoa.com.br.model.Anotacao

class AtualizarAnotacaoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAtualizarAnotacaoBinding.inflate(layoutInflater) }
    private lateinit var anotacao: Anotacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        popularCampos()

        binding.buttonUpdate.setOnClickListener {
                atualizar()
        }

    }

    fun popularCampos(){
          anotacao = intent.getSerializableExtra("Anotacao") as Anotacao
          anotacao.let {
              binding.editTitleUpdate.setText(it.title)
              binding.editDescritpionUpdate.setText(it.description)
          }
    }

    fun atualizar(){
        if(binding.editTitleUpdate.text.toString().isEmpty() ||  binding.editDescritpionUpdate.text.toString().isEmpty()){
                Toast.makeText(this, "Campos vázios", Toast.LENGTH_LONG).show()
        }else{
            anotacao.title = binding.editTitleUpdate.text.toString()
            anotacao.description to binding.editDescritpionUpdate.text.toString()

            Database.updateAnnotation(anotacao)
            Handler(Looper.getMainLooper()).postDelayed({
                redirecionarTelaPrincipal()
            }, 3000)

        }
    }

    fun redirecionarTelaPrincipal(){
        startActivity(Intent(this, TelaPrincipalActivity::class.java))
    }

}