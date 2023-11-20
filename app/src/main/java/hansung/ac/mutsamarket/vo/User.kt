package hansung.ac.mutsamarket.vo

data class User(
    var name: String,
    var birth: String,
    var email: String,
    var uid: String
) {
    constructor(): this("", "", "", "")
}
