package hansung.ac.mutsamarket.vo

import java.util.UUID

data class ChatRoom(
    val writer: String,
    val email: String
) {
    val chatRoomId: String = generateRandomRoomId() // 채팅방 ID 추가

    private fun generateRandomRoomId(): String {
        return UUID.randomUUID().toString()
    }
}
