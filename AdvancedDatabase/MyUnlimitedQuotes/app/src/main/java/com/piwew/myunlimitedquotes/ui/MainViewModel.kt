package com.piwew.myunlimitedquotes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.piwew.myunlimitedquotes.data.QuoteRepository
import com.piwew.myunlimitedquotes.network.QuoteResponseItem

class MainViewModel(quoteRepository: QuoteRepository) : ViewModel() {

    val quote: LiveData<PagingData<QuoteResponseItem>> =
        quoteRepository.getQuote().cachedIn(viewModelScope)
}