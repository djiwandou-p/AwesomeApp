package com.mylektop.awesomeapp.screens.curated

import com.mylektop.awesomeapp.models.curated.CuratedPhotoResult

/**
 * Created by iddangunawan on 12/16/20
 */
interface CuratedOnItemClickListener {
    fun onItemClick(curatedPhotoResult: CuratedPhotoResult)
}