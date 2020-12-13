package com.mylektop.awesomeapp.screens.curated

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import com.mylektop.awesomeapp.platform.LiveDataWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

/**
 * Curated View Model.
 * Handles connecting with corresponding Use Case.
 * Getting back data to View.
 * Created by iddangunawan on 12/13/20
 */
class CuratedActivityViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: CuratedUseCase
) : ViewModel(), KoinComponent {

    private val job = SupervisorJob()

    var mCuratedPhotoResponse = MutableLiveData<LiveDataWrapper<CuratedPhoto>>()
    val mLoadingLiveData = MutableLiveData<Boolean>()
    val mUiScope = CoroutineScope(mainDispatcher + job)
    val mIoScope = CoroutineScope(ioDispatcher + job)

    init {
        // call reset to reset all VM data as required
        resetValues()
    }

    // Reset any member as per flow
    fun resetValues() {

    }

    // can be called from View explicitly if required
    // Keep default scope
    fun requestCuratedActivityData(perPage: Int, page: Int) {
        if (mCuratedPhotoResponse.value == null) {
            mUiScope.launch {
                mCuratedPhotoResponse.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = mIoScope.async {
                        return@async useCase.processCuratedUseCase(perPage, page)
                    }.await()

                    try {
                        mCuratedPhotoResponse.value = LiveDataWrapper.success(data)
                    } catch (e: Exception) {
                    }

                    setLoadingVisibility(false)
                } catch (e: Exception) {
                    setLoadingVisibility(false)
                    mCuratedPhotoResponse.value = LiveDataWrapper.error(e)
                }
            }
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        mLoadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }
}
