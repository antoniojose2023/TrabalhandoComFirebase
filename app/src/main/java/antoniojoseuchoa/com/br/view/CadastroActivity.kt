package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy{ActivityCadastroBinding.inflate(layoutInflater)}
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btCadastro.setOnClickListener {
            cadastrarUsuario()
        }


    }

    fun cadastrarUsuario(){
        if(binding.editEmailCadastro.text.toString().isEmpty() || binding.editSenhaCadastro.text.toString().isEmpty()){
                Toast.makeText(this, "Campo vázio", Toast.LENGTH_LONG).show()
        }else{
            val email = binding.editEmailCadastro.text.toString()
            val senha =  binding.editSenhaCadastro.text.toString()

            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, TelaPrincipalActivity::class.java))
                }else{
                    Toast.makeText(this, "Erro ao cadastrado usuário", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}