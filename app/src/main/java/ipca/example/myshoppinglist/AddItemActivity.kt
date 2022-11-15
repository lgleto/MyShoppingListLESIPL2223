package ipca.example.myshoppinglist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ipca.example.myshoppinglist.databinding.ActivityAddItemBinding


class AddItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            val description: String = binding.editTextDescription.text.toString()
            val qtd: Double = binding.editTextQtd.text.toString().toDouble()
            val intent = Intent()
            intent.putExtra("description", description)
            intent.putExtra("qtd", qtd)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}