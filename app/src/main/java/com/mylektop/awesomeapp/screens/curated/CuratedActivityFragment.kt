package com.mylektop.awesomeapp.screens.curated

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mylektop.awesomeapp.MainActivity
import com.mylektop.awesomeapp.R
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER_URL
import com.mylektop.awesomeapp.constants.SRC_PHOTO
import com.mylektop.awesomeapp.constants.VIEW_TYPE_RECYCLERVIEW_LIST
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
class CuratedActivityFragment : BaseFragment(), CuratedOnItemClickListener {

    private val TAG = CuratedActivityFragment::class.java.simpleName
    private val mCuratedUseCase: CuratedUseCase by inject()
    private val mBaseViewModelFactory: BaseViewModelFactory =
        BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO, mCuratedUseCase)
    lateinit var mRecyclerViewAdapter: CuratedRecyclerViewAdapter

    private val mViewModel: CuratedActivityViewModel by lazy {
        ViewModelProviders.of(this, mBaseViewModelFactory).get(CuratedActivityViewModel::class.java)
    }

    private var listItems: ArrayList<CuratedPhotoResult> = arrayListOf()

    // initialise loading state
    private var mIsLoading = false
    private var mIsLastPage = false

    // amount of items you want to load per page
    private var pageSize = 15
    private var mCurrentPage = 1

    private var isFirstPage = false

    override fun getLayoutId(): Int = R.layout.fragment_curated_activity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Start observing the targets
        this.mViewModel.mCuratedPhotoResponse.observe(viewLifecycleOwner, this.mDataObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)

        val bundle = arguments
        val viewTypeRecyclerViewList = bundle?.getBoolean(VIEW_TYPE_RECYCLERVIEW_LIST) ?: true

        mRecyclerViewAdapter =
            CuratedRecyclerViewAdapter(activity!!, arrayListOf(), viewTypeRecyclerViewList, this)
        landingListRecyclerView.adapter = mRecyclerViewAdapter
        landingListRecyclerView.layoutManager =
            if (viewTypeRecyclerViewList)
                LinearLayoutManager(activity!!)
            else
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        landingListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (viewTypeRecyclerViewList) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    // number of visible items
                    val visibleItemCount: Int = linearLayoutManager.childCount
                    Log.d("wkwkkw", "visibleItemCount $visibleItemCount")
                    // number of items in layout
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    Log.d("wkwkkw", "totalItemCount $totalItemCount")
                    // the position of first visible item
                    val firstVisibleItemPosition: Int =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    Log.d("wkwkkw", "firstVisibleItemPosition $firstVisibleItemPosition")

                    val isNotLoadingAndNotLastPage: Boolean = !mIsLoading && !mIsLastPage
                    Log.d("wkwkkw", "isNotLoadingAndNotLastPage $isNotLoadingAndNotLastPage")
                    // flag if number of visible items is at the last
                    val isAtLastItem: Boolean =
                        firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    Log.d("wkwkkw", "isAtLastItem $isAtLastItem")
                    // validate non negative values
                    val isValidFirstItem: Boolean = firstVisibleItemPosition >= 0
                    Log.d("wkwkkw", "isValidFirstItem $isValidFirstItem")
                    // validate total items are more than possible visible items
                    val totalIsMoreThanVisible: Boolean = totalItemCount >= pageSize
                    Log.d("wkwkkw", "totalIsMoreThanVisible $totalIsMoreThanVisible")
                    // flag to know whether to load more
                    val shouldLoadMore: Boolean =
                        isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage
                    Log.d("wkwkkw", "shouldLoadMore $shouldLoadMore")

                    if (shouldLoadMore) loadMoreItems(false)
                } else {
                    val staggeredGridLayoutManager =
                        recyclerView.layoutManager as StaggeredGridLayoutManager?
                }
            }
        })

        // load the first page
        loadMoreItems(true)
    }

    private fun loadMoreItems(isFirstPage: Boolean) {
        this.isFirstPage = isFirstPage
        // change loading state
        mIsLoading = true
        mCurrentPage = mCurrentPage + 1

        this.mViewModel.requestCuratedActivityData(pageSize, mCurrentPage)
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
                listItems = mainItemReceived.photos as ArrayList<CuratedPhotoResult>
                val refresh = Handler(Looper.getMainLooper())

                if (!isFirstPage) {
                    refresh.post {
                        mRecyclerViewAdapter.addListItems(listItems)
                    }
                } else {
                    refresh.post {
                        mRecyclerViewAdapter.updateListItems(listItems)
                    }
                }

                mIsLoading = false
                mIsLastPage = mCurrentPage == mainItemReceived.total_results
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

    override fun onItemClick(curatedPhotoResult: CuratedPhotoResult) {
        val intent = Intent(activity?.baseContext, MainActivity::class.java)
        intent.putExtra(SRC_PHOTO, curatedPhotoResult.src.original)
        intent.putExtra(PHOTOGRAPHER, curatedPhotoResult.photographer)
        intent.putExtra(PHOTOGRAPHER_URL, curatedPhotoResult.photographer_url)
        activity?.startActivity(intent)
    }
}