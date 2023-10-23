package com.piwew.mynews.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.piwew.mynews.utils.DataDummy
import com.piwew.mynews.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NewsDatabase
    private lateinit var dao: NewsDao
    private val sampleNews = DataDummy.generateDummyNewsEntity()[0]

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).build()
        dao = database.newsDao()
    }

    @After
    fun closeDb() = database.close()

    /*
    * A. Ketika menyimpan data berhasil
    * - Sama dengan data ketika getBookmarkedNews.
    * - isNewsBookmarked bernilai true.
    * */
    @Test
    fun saveNews_Success() = runTest {
        dao.saveNews(sampleNews)
        val actualNews = dao.getBookmarkedNews().getOrAwaitValue()
        Assert.assertEquals(sampleNews.title, actualNews[0].title)
        Assert.assertTrue(dao.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }

    /*
    * B. Ketika menghapus data berhasil
    * - Data getBookmarkedNews kosong.
    * - isNewsBookmarked bernilai false.
    * */
    @Test
    fun deleteNews_Success() = runTest {
        dao.saveNews(sampleNews)
        dao.deleteNews(sampleNews.title)
        val actualNews = dao.getBookmarkedNews().getOrAwaitValue()
        Assert.assertTrue(actualNews.isEmpty())
        Assert.assertFalse(dao.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }
}