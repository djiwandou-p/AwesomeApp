package com.mylektop.awesomeapp.screens.curated

import android.os.Bundle
import com.mylektop.awesomeapp.R
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER_URL
import com.mylektop.awesomeapp.constants.SRC_PHOTO
import com.mylektop.awesomeapp.platform.BaseFragment

/**
 * Created by iddangunawan on 12/16/20
 */
class CuratedDetailActivityFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_curated_detail_activity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        val srcPhoto = bundle?.getString(SRC_PHOTO) ?: ""
        val photographer = bundle?.getString(PHOTOGRAPHER) ?: ""
        val photographerUrl = bundle?.getString(PHOTOGRAPHER_URL) ?: ""
    }
}