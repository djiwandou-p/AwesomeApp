package com.mylektop.awesomeapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.ViewCompat
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER
import com.mylektop.awesomeapp.constants.PHOTOGRAPHER_URL
import com.mylektop.awesomeapp.constants.SRC_PHOTO
import com.mylektop.awesomeapp.constants.VIEW_TYPE_RECYCLERVIEW_LIST
import com.mylektop.awesomeapp.platform.BaseActivity
import com.mylektop.awesomeapp.screens.curated.CuratedActivityFragment
import com.mylektop.awesomeapp.screens.curated.CuratedDetailActivityFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.inflateMenu(R.menu.menu)
        setSupportActionBar(toolbar)

        configureAndShowFragment(true)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_grid) {
            configureAndShowFragment(false)
            return true
        }
        if (id == R.id.action_list) {
            configureAndShowFragment(true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureAndShowFragment(viewTypeRecyclerViewList: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(VIEW_TYPE_RECYCLERVIEW_LIST, viewTypeRecyclerViewList)

        val fragment = CuratedActivityFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.base_frame_layout, fragment)
            .commit()
    }
}