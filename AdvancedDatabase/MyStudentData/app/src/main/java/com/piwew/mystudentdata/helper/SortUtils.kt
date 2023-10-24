package com.piwew.mystudentdata.helper

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SortUtils {
    fun getSortedQuery(sortType: SortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM student ")
        when (sortType) {
            SortType.ASCENDING -> {
                simpleQuery.append("ORDER BY name ASC")
            }

            SortType.DESCENDING -> {
                simpleQuery.append("ORDER BY name DESC")
            }

            SortType.RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}