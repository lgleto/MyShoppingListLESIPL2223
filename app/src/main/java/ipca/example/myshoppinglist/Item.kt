package ipca.example.myshoppinglist

import com.google.firebase.firestore.DocumentSnapshot

data class Item (
    var description : String?,
    var qtd         : Double?,
    var active      : Boolean?,
    var done        : Boolean?,
    var uid         : String = ""
    ){

    fun toHashMap() : java.util.HashMap<String,Any?>{
        return hashMapOf(
            "description" to description,
            "qtd" to qtd,
            "active" to active,
            "done" to done
        )
    }

    companion object {

        fun fromDoc( documentSnapshot: DocumentSnapshot) : Item{
            return Item(
                documentSnapshot.getString("description"),
                documentSnapshot.getDouble("qtd"),
                documentSnapshot.getBoolean("active"),
                documentSnapshot.getBoolean("done"),
                documentSnapshot.id
            )
        }
    }

}

