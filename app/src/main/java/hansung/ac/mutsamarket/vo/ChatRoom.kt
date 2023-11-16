package hansung.ac.mutsamarket.vo

import java.util.UUID

data class ChatRoom(
    val writer: String,
    val lastMessage: String
) {
    // 적절한 ID 생성 로직 추가
    val roomId: String = generateRandomRoomId()

    private fun generateRandomRoomId(): String {
        // 적절한 고유한 ID 생성 로직 작성 (예: UUID 사용)
        // 이 부분은 앱의 요구 사항에 따라 고유한 ID를 생성하는 로직을 작성하면 됩니다.
        return UUID.randomUUID().toString()
    }
}

