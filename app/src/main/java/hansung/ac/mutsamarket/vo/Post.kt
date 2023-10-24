package hansung.ac.mutsamarket.vo

data class Post(
    val image : String,
    val title : String,
    val price: Int,
    val writer:String,
    val isSale:String
)