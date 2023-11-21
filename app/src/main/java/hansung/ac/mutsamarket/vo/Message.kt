package hansung.ac.mutsamarket.vo

data class Message(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val chatRoomId: String = "" // chatRoomId 필드 추가
)
