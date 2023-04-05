package com.example.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.paging3.Repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


// HiltViewModel -> If we are using HiltViewModel then  we have to not use the ViewModelfactory
@HiltViewModel
class QuoteViewModel @Inject constructor(val quoteRepository: QuoteRepository): ViewModel() {

    val list = quoteRepository.getQuotes().cachedIn(viewModelScope)

    // viewModelScope -> its working like coroutine
    // we use cached because  it will store the copy of data and we have to not do same work again and again
}