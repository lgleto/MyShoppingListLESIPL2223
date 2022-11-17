package ipca.example.myshoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.example.myshoppinglist.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {

    var items = arrayListOf<Item>()
    var itemsAll = arrayListOf<Item>()

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ItemsAdapter()

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    private var viewAll = false

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

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(),AddItemActivity::class.java))
        }



        db.collection("users")
            .document(currentUser!!.uid)
            .collection("shoppingList")
            //.whereEqualTo("done", false)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    return@addSnapshotListener
                }

                items.clear()
                itemsAll.clear()

                for (doc in value!!){
                    val item = Item.fromDoc(doc)
                    if(item.done == false)
                        items.add(item)
                    itemsAll.add(item)
                }
                adapter.notifyDataSetChanged()
            }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_all -> {
                        // clearCompletedTasks()
                        viewAll = !viewAll

                        if (!viewAll){
                            items = itemsAll.filter {
                                it.done == false
                            } as ArrayList<Item>
                        }else{
                            items = itemsAll
                        }

                        adapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

                var value = items[position].qtd!! - 1.0

                if (value <= 0)  {
                    db.collection("users")
                        .document(currentUser!!.uid)
                        .collection("shoppingList")
                        .document( items[position].uid)
                        .update("done",true)

                }

                db.collection("users")
                    .document(currentUser!!.uid)
                    .collection("shoppingList")
                    .document( items[position].uid)
                    .update("qtd",value)


            }

            return rootView
        }

    }
}