package antoniojoseuchoa.com.br.database

import antoniojoseuchoa.com.br.model.Anotacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

abstract class Database {
    companion object{
        private val firestore = Firebase.firestore
        private val auth = FirebaseAuth.getInstance()

        private val usuarioCorrente = auth.currentUser!!.uid

        fun saveAnnotation(anotacao: Anotacao): Boolean{
              var validate = false
              anotacao.id = UUID.randomUUID().toString().toLong()

              val anotacaoMap = mapOf(
                   "id" to anotacao.id,
                   "nome" to anotacao.title,
                   "description" to anotacao.description
             )

             val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id.toString())
             reference.set( anotacaoMap ).addOnCompleteListener {
                   if(it.isSuccessful){
                        validate = true
                   }
             }

            return validate
        }

        fun updateAnnotation(anotacao: Anotacao): Boolean{
            var validate = false

            val anotacaoMap = mapOf(
                "nome" to anotacao.title,
                "description" to anotacao.description
            )

            val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id.toString())
            reference.update( anotacaoMap ).addOnCompleteListener {
                if(it.isSuccessful){
                    validate = true
                }
            }

            return validate
        }


        fun deleteAnnotation(anotacao: Anotacao): Boolean{
            var validate = false

            val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao").document(anotacao.id.toString())
            reference.delete().addOnCompleteListener {
                if(it.isSuccessful){
                    validate = true
                }
            }

            return validate
        }

        fun getAnotacao(): List<Anotacao>{
              var list: MutableList<Anotacao> = mutableListOf()

              val reference = firestore.collection("Anotação_usuários").document(usuarioCorrente).collection("anotacao")
              reference.addSnapshotListener { value, error ->
                  if (error != null){
                      return@addSnapshotListener
                  }else{
                      for(item in value!!.documents){
                          val anotacao = item.toObject(Anotacao::class.java)
                          if (anotacao != null) {
                              list.add( anotacao )
                          }
                      }
                  }
              }

             return list
        }


    }

}