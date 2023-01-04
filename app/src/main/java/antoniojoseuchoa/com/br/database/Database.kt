package antoniojoseuchoa.com.br.database

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.Uri
import antoniojoseuchoa.com.br.adapter.AdapterAnotacoes
import antoniojoseuchoa.com.br.dialog.DialogLoader
import antoniojoseuchoa.com.br.model.Anotacao
import antoniojoseuchoa.com.br.view.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

abstract class Database {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private val firestore = Firebase.firestore
        private val auth = FirebaseAuth.getInstance()
        private val storage = FirebaseStorage.getInstance()

        private val usuarioCorrente = auth.currentUser!!.uid

        fun saveAnnotation(anotacao: Anotacao): Boolean{
              var validate = false

              val anotacaoMap = mutableMapOf(
                   "id" to anotacao.id,
                   "title" to anotacao.title,
                   "description" to anotacao.description
             )

             val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id)
             reference.set( anotacaoMap ).addOnSuccessListener {
                     validate = true
             }.addOnFailureListener {
                     validate = false
             }

            return validate
        }

        fun updateAnnotation(anotacao: Anotacao): Boolean{
            var validate = false

            val anotacaoMap = mapOf(
                "title" to anotacao.title,
                "description" to anotacao.description
            )

            val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id)
            reference.update( anotacaoMap ).addOnCompleteListener {
                if(it.isSuccessful){
                    validate = true
                }
            }

            return validate
        }


        fun deleteAnnotation(anotacao: Anotacao, adapterAnotacoes: AdapterAnotacoes): Boolean{
            var validate = false

            val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id)
            reference.delete().addOnCompleteListener {
                if(it.isSuccessful){
                    validate = true
                    adapterAnotacoes.notifyDataSetChanged()
                }
            }

            return validate
        }

        fun getAnotacao(list: MutableList<Anotacao>, adapterAnotacoes: AdapterAnotacoes){

              val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").orderBy("title", Query.Direction.ASCENDING).limit(5)
              reference.addSnapshotListener { value, error ->
                  if (error != null){
                      return@addSnapshotListener
                  }else{
                      list.clear()
                      for(item in value!!.documents){
                          val anotacao = item.toObject(Anotacao::class.java)
                          if (anotacao != null) {
                              list.add( anotacao )
                          }

                          adapterAnotacoes.notifyDataSetChanged()
                      }
                  }
              }
        }

        fun consultaAnotacao(textoPesquisa: String, list: MutableList<Anotacao>, adapterAnotacoes: AdapterAnotacoes){

            val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").orderBy("title", Query.Direction.ASCENDING)
            reference.startAt(textoPesquisa).endAt(textoPesquisa+"\uf88f").addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }else{
                    list.clear()
                    for(item in value!!.documents){
                        val anotacao = item.toObject(Anotacao::class.java)
                        if (anotacao != null) {
                            list.add( anotacao )
                        }

                        adapterAnotacoes.notifyDataSetChanged()
                    }
                }
            }

        }

        fun uploadImagemUsuario(uri: Uri, activity: Activity){
                val idUsuarioLogado = auth.currentUser!!.uid
                val nomeImagem = "imagem"+System.currentTimeMillis().toString()
                val reference = storage.getReference().child("imagens/usuario/${idUsuarioLogado}/$nomeImagem")

                reference.putFile(uri).addOnSuccessListener {

                        reference.downloadUrl.addOnSuccessListener{
                            salvarImagemUsuarioBanco(it.toString())
                        }
                }.addOnFailureListener {

                }
        }

        fun salvarImagemUsuarioBanco(url: String){
               val imagemMap = mapOf(
                   "url" to url
               )
               val reference = firestore.collection("foto_usuario").document(usuarioCorrente)
               reference.set(imagemMap).addOnSuccessListener{

               }.addOnFailureListener {

               }
        }

        fun recuperarImagemUsuario(activity: Activity, circleImageView: CircleImageView){

            val reference = firestore.collection("foto_usuario").document(usuarioCorrente)
            reference.addSnapshotListener { value, error ->

                if(error != null){ return@addSnapshotListener}

                val url = value!!.get("url")
                Glide.with(activity).load( url ).into(circleImageView)

            }
            
        }

    }

}