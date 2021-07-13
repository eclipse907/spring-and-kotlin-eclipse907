data class Book (
    val inventoryNumber: Long,
    val title: String,
    val authorName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Book
        if (inventoryNumber != other.inventoryNumber) {
            return false
        }
        return true
    }

    override fun hashCode() = inventoryNumber.hashCode()

}
