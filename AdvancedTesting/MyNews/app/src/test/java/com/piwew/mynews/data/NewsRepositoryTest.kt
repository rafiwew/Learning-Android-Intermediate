package com.piwew.mynews.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.piwew.mynews.data.local.room.NewsDao
import com.piwew.mynews.data.remote.retrofit.ApiService
import com.piwew.mynews.utils.DataDummy
import com.piwew.mynews.utils.MainDispatcherRule
import com.piwew.mynews.utils.getOrAwaitValue
import com.piwew.mynews.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var newsDao: NewsDao
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        newsDao = FakeNewsDao()
        newsRepository = NewsRepository(apiService, newsDao)
    }

    /*
    A. Ketika mengambil data dari internet
    - Memastikan data tidak null.
    - Memastikan jumlah data sesuai dengan yang diharapkan.
    */
    @Test
    fun `when getHeadlineNews should not null`() = runTest {
        val expectedNews = DataDummy.generatedDummyNewsResponse()
        val actualNews = newsRepository.getHeadlineNews()
        actualNews.observeForTesting {
            Assert.assertNotNull(actualNews)
            Assert.assertEquals(
                expectedNews.articles.size,
                (actualNews.value as Result.Success).data.size
            )
        }
    }

    /*
    B. Ketika menyimpan data ke database
    - Data tersebut muncul di getBookmarkedNews.
    - Fungsi isBookmarked bernilai true.
    */
    @Test
    fun `when saveNews should exist in getBookmarkedNews`() = runTest {
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]
        newsDao.saveNews(sampleNews)
        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()
        Assert.assertTrue(actualNews.contains(sampleNews))
        Assert.assertTrue(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }

    /*
    C. Ketika menghapus data dari database
    - Data tersebut tidak muncul di getBookmarkedNews.
    - Fungsi isBookmarked bernilai false.
    */
    @Test
    fun `when deleteNews should not exist in getBookmarkedNews`() = runTest {
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]
        newsRepository.saveNews(sampleNews)
        newsRepository.deleteNews(sampleNews.title)
        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()
        Assert.assertFalse(actualNews.contains(sampleNews))
        Assert.assertFalse(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }
}