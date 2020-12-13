package com.mylektop.awesomeapp.screens.curated

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mylektop.awesomeapp.R
import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import com.mylektop.awesomeapp.models.curated.CuratedPhotoResult
import com.mylektop.awesomeapp.platform.BaseFragment
import com.mylektop.awesomeapp.platform.BaseViewModelFactory
import com.mylektop.awesomeapp.platform.LiveDataWrapper
import kotlinx.android.synthetic.main.fragment_curated_activity.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

/**
 * Curated Fragment.
 * Handles UI.
 * Fires rest api initiation.
 * Created by iddangunawan on 12/13/20
 */
class CuratedActivityFragment : BaseFragment() {

    companion object {
        fun getMainActivityFragment() = CuratedActivityFragment()
    }

    private val TAG = CuratedActivityFragment::class.java.simpleName
    val mCuratedUseCase: CuratedUseCase by inject()
    private val mBaseViewModelFactory: BaseViewModelFactory =
        BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO, mCuratedUseCase)
    lateinit var mRecyclerViewAdapter: CuratedRecyclerViewAdapter

    private val mViewModel: CuratedActivityViewModel by lazy {
        ViewModelProviders.of(this, mBaseViewModelFactory)
            .get(CuratedActivityViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_curated_activity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Start observing the targets
        this.mViewModel.mCuratedPhotoResponse.observe(this, this.mDataObserver)
        this.mViewModel.mLoadingLiveData.observe(this, this.loadingObserver)

        mRecyclerViewAdapter = CuratedRecyclerViewAdapter(activity!!, arrayListOf())
        landingListRecyclerView.adapter = mRecyclerViewAdapter
        landingListRecyclerView.layoutManager = LinearLayoutManager(activity!!)

        this.mViewModel.requestCuratedActivityData(10, 1)
    }

    private val mDataObserver = Observer<LiveDataWrapper<CuratedPhoto>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.RESPONSESTATUS.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.RESPONSESTATUS.ERROR -> {
                // Error for http request
                Log.d(TAG, "LiveDataResult.Status.ERROR = ${result.response}")

                error_holder.visibility = View.VISIBLE
                showToast("Error in getting data")

            }
            LiveDataWrapper.RESPONSESTATUS.SUCCESS -> {
                // Data from API
                Log.d(TAG, "LiveDataResult.Status.SUCCESS = ${result.response}")

                val mainItemReceived = result.response as CuratedPhoto
                val listItems = mainItemReceived.photos as ArrayList<CuratedPhotoResult>

                processData(listItems)
            }
        }
    }

    /**
     * Handle success data
     */
    private fun processData(listItems: ArrayList<CuratedPhotoResult>) {
        Log.d(TAG, "processData called with data ${listItems.size}")
        Log.d(TAG, "processData listItems =  $listItems")

        val refresh = Handler(Looper.getMainLooper())

        refresh.post {
            mRecyclerViewAdapter.updateListItems(listItems)
        }
    }

    /**
     * Hide / show loader
     */
    private val loadingObserver = Observer<Boolean> { visible ->
        // Show hide progress bar
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }
}