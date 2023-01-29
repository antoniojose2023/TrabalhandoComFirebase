package antoniojoseuchoa.com.br.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import antoniojoseuchoa.com.br.R
import antoniojoseuchoa.com.br.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val auth = FirebaseAuth.getInstance()
    lateinit var googleSigningClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btEntrarLogin.setOnClickListener {
            loginComEmailSenha()
        }

        binding.tvCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        if(auth.currentUser != null){
             showScreenMain()
        }

        callbackManager = CallbackManager.Factory.create()

        googleSigninOptions()

        binding.btGoogle.setOnClickListener {
            requestGoogleIntent()
        }

        //login com o facebook
        binding.btFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(baseContext, "Login efetuado com sucesso.", Toast.LENGTH_LONG).show()
                showScreenMain()
            }

            override fun onCancel() {
                Toast.makeText(baseContext, "Erro ao autenticar", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(baseContext, "Erro ao autenticar", Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
           super.onActivityResult(requestCode, resultCode, data)
           callbackManager.onActivityResult(resultCode, resultCode,data)

    }

    //metodo login com o google
    //passo 1
    fun googleSigninOptions(){
        val googleSigninOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSigningClient = GoogleSignIn.getClient(this, googleSigninOptions)
    }

    //passo 2
    fun requestGoogleIntent(){
        val requestIntent = googleSigningClient.signInIntent
        responseLoginGoole.launch( requestIntent )
    }

    //passo 3
    val responseLoginGoole = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == requestedOrientation){
            val tarefa = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try{
                val conta = tarefa.getResult(ApiException::class.java)
                authentication(conta.idToken!!)
            }catch (ex: ApiException){

            }

        }
    }

    //passo 4
    fun authentication(token: String){
         val credential = GoogleAuthProvider.getCredential(token, null)
         auth.signInWithCredential(credential).addOnCompleteListener(this){
             if(it.isSuccessful){
                  Toast.makeText(this, "Login efetuado com sucesso.", Toast.LENGTH_LONG).show()
                  showScreenMain()
             }else{
                 Toast.makeText(this, "Erro ao autenticar", Toast.LENGTH_LONG).show()
             }
         }.addOnFailureListener {
             Toast.makeText(this, "erro.${it.message}", Toast.LENGTH_LONG).show()
         }
    }


    //Login com email e senha
    fun loginComEmailSenha(){
        val email = binding.editEmailLogin.text.toString()
        val senha = binding.editSenhaLogin.text.toString()

        if(email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Campo vázio", Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,senha).addOnSuccessListener {
                Toast.makeText(this, "Login efetuado com sucesso.", Toast.LENGTH_LONG).show()
                showScreenMain()
            }.addOnFailureListener{
                Toast.makeText(this, "erro.${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun showScreenMain(){
        val intent = Intent(this, TelaPrincipalActivity::class.java)
        startActivity( intent )
        finish()
    }


}