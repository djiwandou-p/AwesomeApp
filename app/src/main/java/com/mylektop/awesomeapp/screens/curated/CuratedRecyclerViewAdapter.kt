package com.mylektop.awesomeapp.screens.curated

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mylektop.awesomeapp.R
import com.mylektop.awesomeapp.models.curated.CuratedPhotoResult

/**
 * Adapter for Curated screen recyclerview.
 * Created by iddangunawan on 12/13/20
 */
class CuratedRecyclerViewAdapter(val context: Context, list: ArrayList<CuratedPhotoResult>) :
    RecyclerView.Adapter<CuratedRecyclerViewAdapter.CuratedFragViewHolder>() {

    var mItemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuratedFragViewHolder {
        return CuratedFragViewHolder(
            LayoutInflater.from(context).inflate(R.layout.landing_list_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CuratedFragViewHolder, position: Int) {
        val model: CuratedPhotoResult = mItemList[position]
        holder.photographer.text = model.photographer
        holder.photographerUrl.text = model.photographer_url
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    fun updateListItems(updatedList: ArrayList<CuratedPhotoResult>) {
        mItemList.clear()
        mItemList = updatedList
        notifyDataSetChanged()
    }

    class CuratedFragViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val photographer: TextView = item.findViewById(R.id.photographer)
        val photographerUrl: TextView = item.findViewById(R.id.photographerUrl)
    }
}