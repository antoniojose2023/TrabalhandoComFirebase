package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.adapter.AdapterAnotacoes
import antoniojoseuchoa.com.br.database.Database
import antoniojoseuchoa.com.br.databinding.ActivityTelaPrincipalBinding
import com.google.firebase.auth.FirebaseAuth

class TelaPrincipalActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityTelaPrincipalBinding.inflate(layoutInflater) }

    private lateinit var adapter : AdapterAnotacoes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //configurar toolbar
        supportActionBar!!.title = "Home"

        binding.btFloatingAdd.setOnClickListener {
                showScreenAddAnnotation()
        }

        val list = Database.getAnotacao()
        Toast.makeText(this, "${list.size}", Toast.LENGTH_LONG).show()

        binding.rvAnnotation.layoutManager = LinearLayoutManager(this)
        adapter = AdapterAnotacoes(this, list)
        binding.rvAnnotation.adapter = adapter
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
        startActivity(Intent(this, MainActivity::class.java ))
        finish()
    }

    fun showScreenAddAnnotation(){
        startActivity(Intent(this, AdicionarAnotacaoActivity::class.java ))
    }
}