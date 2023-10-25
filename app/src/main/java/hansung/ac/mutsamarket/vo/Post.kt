package hansung.ac.mutsamarket.vo

import java.io.Serializable

data class Post(
    val image : String,
    val title : String,
    val price: String,
    val writer: String,
    val isSale: Boolean
): Serializable