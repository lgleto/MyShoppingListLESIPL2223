package ipca.example.myshoppinglist

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.example.myshoppinglist.databinding.ActivityAddItemBinding


class AddItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddItemBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser

        val items = arrayListOf<String>()
        db.collection("users")
            .document(currentUser!!.uid)
            .collection("shoppingList")
            .whereEqualTo("done", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = Item.fromDoc(document)
                    item.description?.let { items.add(it) }
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    R.layout.simple_dropdown_item_1line, items
                )

                val textView = binding.editTextDescription as AutoCompleteTextView
                textView.setAdapter(adapter)

            }




        binding.buttonSave.setOnClickListener {

            val description: String = binding.editTextDescription.text.toString()
            val qtd: Double = binding.editTextQtd.text.toString().toDouble()
            val item = Item(description,qtd, true,false,"")


            db.collection("users")
                .document(currentUser?.uid!!)
                .collection("shoppingList")
                .whereEqualTo("description", description)
                .get()
                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {
                        val itemToUpdate = Item.fromDoc(documents.first())
                        itemToUpdate.qtd = qtd
                        itemToUpdate.done = false
                        itemToUpdate.active = true

                        db.collection("users")
                            .document(currentUser?.uid!!)
                            .collection("shoppingList")
                            .document(itemToUpdate.uid)
                            .update(itemToUpdate.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(this@AddItemActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
                            }

                    }else{
                        db.collection("users")
                            .document(currentUser?.uid!!)
                            .collection("shoppingList")
                            .add(item.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(this@AddItemActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
                            }
                    }


                }


        }
    }
    companion object{
        const val TAG = "AddItemActivity"
    }
}