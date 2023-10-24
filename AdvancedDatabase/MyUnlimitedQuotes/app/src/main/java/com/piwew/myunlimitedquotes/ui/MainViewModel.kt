package com.piwew.myunlimitedquotes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piwew.myunlimitedquotes.data.QuoteRepository
import com.piwew.myunlimitedquotes.network.QuoteResponseItem
import kotlinx.coroutines.launch

class MainViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {
    private val _quote = MutableLiveData<List<QuoteResponseItem>>()
    var quote: LiveData<List<QuoteResponseItem>> = _quote

    fun getQuote() {
        viewModelScope.launch {
            _quote.postValue(quoteRepository.getQuote())
        }
    }
}