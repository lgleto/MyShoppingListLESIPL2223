package ipca.example.myshoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.example.myshoppinglist.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {

    var items = arrayListOf<Item>()

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ItemsAdapter()

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listViewItems.adapter = adapter

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                //val data: Intent? = it.data
                //val description = data?.getStringExtra("description")
                //val qtd = data?.getDoubleExtra("qtd", 0.0)
                //items.add(Item(description,qtd, ))
                //adapter.notifyDataSetChanged()
            }
        }

        binding.fabAdd.setOnClickListener {
            resultLauncher.launch(Intent(requireContext(),AddItemActivity::class.java))
        }

        val currentUser = Firebase.auth.currentUser

        db.collection("users")
            .document(currentUser!!.uid)
            .collection("shoppingList")
            .addSnapshotListener { value, error ->
                if(error != null) {
                    return@addSnapshotListener
                }

                for (doc in value!!){
                    val item = Item.fromDoc(doc)
                    items.add(item)
                }
                adapter.notifyDataSetChanged()
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ItemsAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return items.count()
        }

        override fun getItem(position: Int): Any {
            return items[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(position: Int, parente: View?, viewGroup: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_item, viewGroup, false)

            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewDescription)
            val textViewQtd = rootView.findViewById<TextView>(R.id.textViewQtd)
            val buttonPlus = rootView.findViewById<Button>(R.id.buttonPlus)
            val buttonMinus = rootView.findViewById<Button>(R.id.buttonMinus)

            textViewDescription.text = items[position].description
            textViewQtd.text = "${items[position].qtd}"

            buttonPlus.setOnClickListener {

            }
            buttonMinus.setOnClickListener {

            }

            return rootView
        }

    }
}