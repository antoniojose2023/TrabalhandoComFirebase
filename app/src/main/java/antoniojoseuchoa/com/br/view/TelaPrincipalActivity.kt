package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.database.Database
import antoniojoseuchoa.com.br.databinding.ActivityTelaPrincipalBinding
import com.google.firebase.auth.FirebaseAuth

class TelaPrincipalActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityTelaPrincipalBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Log.i("TAG", "Tamanho lista: ${Database.getAnotacao().size}")

    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
                R.id.menu_sair -> {
                     auth.signOut()
                     showScreenMain()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showScreenMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity( intent )
        finish()
    }
}