class Book (
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

    override fun toString() = """
        $authorName
        $title
        inventoryNumber=$inventoryNumber
    """.trimIndent()

    operator fun component1() = inventoryNumber
    operator fun component2() = title
    operator fun component3() = authorName

}
