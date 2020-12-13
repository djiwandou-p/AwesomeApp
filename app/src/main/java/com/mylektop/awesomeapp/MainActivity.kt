package com.mylektop.awesomeapp

import android.os.Bundle
import com.mylektop.awesomeapp.platform.BaseActivity
import com.mylektop.awesomeapp.screens.curated.CuratedActivityFragment

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAndShowFragment()
    }

    private fun configureAndShowFragment() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.base_frame_layout) as CuratedActivityFragment?
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.base_frame_layout, CuratedActivityFragment.getMainActivityFragment())
                .commit()
        }
    }
}