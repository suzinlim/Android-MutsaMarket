package hansung.ac.mutsamarket.vo

import java.io.Serializable

data class Post(
    val image : String,
    val title : String,
    val price: Int,
    val writer:String,
    val isSale:String
): Serializable