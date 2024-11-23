package dataTypes

enum class QueryTypes(private val type: String) {
    SELECT("select [params] from"),
    INSERT("insert into"),
    UPDATE("update"),
    DELETE("delete from");
    override fun toString(): String {
        return type
    }
}
