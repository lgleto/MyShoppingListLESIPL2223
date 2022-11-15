package ipca.example.myshoppinglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        binding.buttonSave.setOnClickListener {

            val description: String = binding.editTextDescription.text.toString()
            val qtd: Double = binding.editTextQtd.text.toString().toDouble()
            val item = Item(description,qtd)

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
                }
        }
    }
    companion object{
        const val TAG = "AddItemActivity"
    }
}