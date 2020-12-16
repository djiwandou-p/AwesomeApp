package com.mylektop.awesomeapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mylektop.awesomeapp.constants.VIEW_TYPE_RECYCLERVIEW_LIST
import com.mylektop.awesomeapp.platform.BaseActivity
import com.mylektop.awesomeapp.screens.curated.CuratedActivityFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAndShowFragment(true)
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