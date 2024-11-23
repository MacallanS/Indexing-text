package tests

import dataTypes.QueryTypes
import org.jetbrains.annotations.TestOnly
import utils.QueryBuilder
import kotlin.test.assertEquals

class QueryBuilderTest {
    @TestOnly
    fun testBuilder() {
        var builder = QueryBuilder.Builder(QueryTypes.SELECT, "table")
            .withSelect(listOf("column1", "column2"))
            .withWhere(listOf("column1", "column2"), listOf("value1", "value2"))

        assertEquals(
            "select (column1, column2) from table where column1 = 'value1' AND column2 = 'value2'",
            builder.build().toString()
        )

        builder = QueryBuilder.Builder(QueryTypes.INSERT, "table")
            .withInsert("")
            .withValues(listOf("value1", "value2"))

        assertEquals(
            "insert into table values value1, value2",
            builder.build().toString()
        )
    }
}
