package ipca.example.myshoppinglist

data class Item (
    var description : String?,
    var qtd         : Double?
    ){

    fun toHashMap() : java.util.HashMap<String,Any?>{
        return hashMapOf(
            "description" to description,
            "qtd" to qtd,
        )
    }

}

