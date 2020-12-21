package com.mylektop.awesomeapp.screens.curated

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.mylektop.awesomeapp.platform.SharedPreferenceHelper
import kotlinx.android.synthetic.main.fragment_curated_activity.*
import kotlinx.coroutines.Dispatchers
import okhttp3.internal.notify
import org.koin.android.ext.android.inject

/**
 * Curated Fragment.
 * Handles UI.
 * Fires rest api initiation.
 * Created by iddangunawan on 12/13/20
 */
class CuratedActivityFragment : BaseFragment(), CuratedOnItemClickListener {

    companion object {
        fun getCuratedActivityFragment() = CuratedActivityFragment()
    }

    private val TAG = CuratedActivityFragment::class.java.simpleName
    private val mCuratedUseCase: CuratedUseCase by inject()
    private val mBaseViewModelFactory: BaseViewModelFactory =
        BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO, mCuratedUseCase)
    lateinit var mRecyclerViewAdapter: CuratedRecyclerViewAdapter

    private val mViewModel: CuratedActivityViewModel by lazy {
        ViewModelProviders.of(this, mBaseViewModelFactory).get(CuratedActivityViewModel::class.java)
    }

    private var listItems: ArrayList<CuratedPhotoResult> = arrayListOf()

    // amount of items you want to load per page
    private var pageSize = 15
    private var currentPage = 1
    private var viewTypeRecyclerViewList: Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_curated_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.inflateMenu(R.menu.menu)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        //Start observing the targets
        this.mViewModel.mCuratedPhotoResponse.observe(viewLifecycleOwner, this.mDataObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)

        viewTypeRecyclerViewList =
            SharedPreferenceHelper(context!!).getBooleanData(VIEW_TYPE_RECYCLERVIEW_LIST)

        mRecyclerViewAdapter =
            CuratedRecyclerViewAdapter(activity!!, arrayListOf(), viewTypeRecyclerViewList, this)
        landingListRecyclerView.adapter = mRecyclerViewAdapter
        landingListRecyclerView.layoutManager =
            if (viewTypeRecyclerViewList)
                LinearLayoutManager(activity!!)
            else
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // load the first page
        this.mViewModel.requestCuratedActivityData(pageSize, currentPage)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as AppCompatActivity).menuInflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_grid) {
            viewTypeRecyclerViewList = false
            SharedPreferenceHelper(context!!).setBooleanData(
                VIEW_TYPE_RECYCLERVIEW_LIST,
                viewTypeRecyclerViewList
            )
            changeAdapterLayout(viewTypeRecyclerViewList)
            return true
        }
        if (id == R.id.action_list) {
            viewTypeRecyclerViewList = true
            SharedPreferenceHelper(context!!).setBooleanData(
                VIEW_TYPE_RECYCLERVIEW_LIST,
                viewTypeRecyclerViewList
            )
            changeAdapterLayout(viewTypeRecyclerViewList)
            return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun onItemClick(curatedPhotoResult: CuratedPhotoResult) {
        val intent = Intent(activity?.baseContext, MainActivity::class.java)
        intent.putExtra(SRC_PHOTO, curatedPhotoResult.src.original)
        intent.putExtra(PHOTOGRAPHER, curatedPhotoResult.photographer)
        intent.putExtra(PHOTOGRAPHER_URL, curatedPhotoResult.photographer_url)
        activity?.startActivity(intent)
    }

    private fun changeAdapterLayout(viewTypeRecyclerViewList: Boolean) {
        mRecyclerViewAdapter =
            CuratedRecyclerViewAdapter(activity!!, arrayListOf(), viewTypeRecyclerViewList, this)
        landingListRecyclerView.adapter = mRecyclerViewAdapter
        landingListRecyclerView.layoutManager =
            if (viewTypeRecyclerViewList)
                LinearLayoutManager(activity!!)
            else
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val refresh = Handler(Looper.getMainLooper())
        refresh.post {
            mRecyclerViewAdapter.updateListItems(listItems)
        }
    }
}