package com.mylektop.awesomeapp.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mylektop.awesomeapp.screens.curated.CuratedActivityViewModel
import com.mylektop.awesomeapp.screens.curated.CuratedUseCase
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Base VM Factory for creation of different VM's
 * Created by iddangunawan on 12/13/20
 */
class BaseViewModelFactory constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
    private val useCase: CuratedUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CuratedActivityViewModel::class.java)) {
            CuratedActivityViewModel(mainDispatcher, ioDispatcher, useCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not configured") as Throwable
        }
    }
}