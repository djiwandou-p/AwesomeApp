package com.mylektop.awesomeapp

import android.os.Bundle
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER_URL
import com.mylektop.awesomeapp.constants.SRC_PHOTO
import com.mylektop.awesomeapp.platform.BaseActivity
import com.mylektop.awesomeapp.screens.curated.CuratedActivityFragment
import com.mylektop.awesomeapp.screens.curated.CuratedDetailActivityFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAndShowFragment()

        val srcPhoto = intent.getStringExtra(SRC_PHOTO) ?: ""
        val photographer = intent.getStringExtra(PHOTOGRAPHER) ?: ""
        val photographerUrl = intent.getStringExtra(PHOTOGRAPHER_URL) ?: ""

        if (srcPhoto != "" && photographer != "" && photographerUrl != "") {
            val bundle = Bundle()
            bundle.putString(SRC_PHOTO, srcPhoto)
            bundle.putString(PHOTOGRAPHER, photographer)
            bundle.putString(PHOTOGRAPHER_URL, photographerUrl)

            val fragment = CuratedDetailActivityFragment()
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.base_frame_layout, fragment)
                .commit()
        }
    }

    private fun configureAndShowFragment() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.base_frame_layout) as CuratedActivityFragment?
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.base_frame_layout, CuratedActivityFragment.getCuratedActivityFragment())
                .commit()
        }
    }
}