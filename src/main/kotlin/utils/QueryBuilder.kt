package utils

import dataTypes.QueryTypes

class QueryBuilder(
    private var table: String,
    private var data: String = "",
    private var where: String? = null,
    private var values: String? = null
) {
    private constructor(builder: Builder) : this(builder.table, builder.data, builder.where, builder.values)

    class Builder(private val type: QueryTypes, val table: String) {
        var data: String = "select * from"
        var where: String? = null
        var values: String? = null

        fun withSelect(columns: List<String>) = apply {
            data = "(" + columns.joinToString(", ") + ")"
            data = type.toString().replace("[params]", data)
        }

        fun withInsert(column: String) = apply {
            data = type.toString().replace("[params]", column)
        }

        fun withWhere(columns: List<String>, values: List<String>) = apply {
            where = "where " + columns.zip(values).joinToString(" AND ") { "${it.first} = '${it.second}'" }
        }

        fun withValues(values: List<String>) = apply {
            this.values = "values " + values.joinToString(", ")
        }

        fun build() = QueryBuilder(this)
    }

    override fun toString(): String {
        return if (values == null) "$data $table $where" else "$data $table $values"
    }
}


