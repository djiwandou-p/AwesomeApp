package com.mylektop.awesomeapp.screens.curated

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mylektop.awesomeapp.R
import com.mylektop.awesomeapp.models.curated.CuratedPhotoResult

/**
 * Adapter for Curated screen recyclerview.
 * Created by iddangunawan on 12/13/20
 */
class CuratedRecyclerViewAdapter(
    val context: Context,
    list: ArrayList<CuratedPhotoResult>,
    private val viewTypeRecyclerViewList: Boolean,
    private val curatedOnItemClickListener: CuratedOnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }

    var mItemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return if (viewTypeRecyclerViewList)
                CuratedFragListViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.landing_list_view_item, parent, false)
                )
            else
                CuratedFragGridViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.landing_grid_view_item, parent, false)
                )
        } else {
            return CuratedFragLoadingViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.landing_loading_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CuratedFragListViewHolder || holder is CuratedFragGridViewHolder) {
            val model: CuratedPhotoResult = mItemList[position]

            if (viewTypeRecyclerViewList) {
                val viewHolderList = holder as CuratedFragListViewHolder

                Glide.with(context).load(model.src.tiny)
                    .fallback(android.R.drawable.stat_notify_error)
                    .timeout(4500)
                    .into(viewHolderList.srcPhoto)

                viewHolderList.photographer.text = model.photographer
                viewHolderList.photographerUrl.text = model.photographer_url

                viewHolderList.srcPhoto.setOnClickListener {
                    curatedOnItemClickListener.onItemClick(model)
                }

                viewHolderList.mainItemView.setOnClickListener {
                    curatedOnItemClickListener.onItemClick(model)
                }
            } else {
                val viewHolderGrid = holder as CuratedFragGridViewHolder

                Glide.with(context).load(model.src.large)
                    .fallback(android.R.drawable.stat_notify_error)
                    .timeout(4500)
                    .into(viewHolderGrid.srcPhoto)

                viewHolderGrid.photographer.text = model.photographer
                viewHolderGrid.photographerUrl.text = model.photographer_url

                viewHolderGrid.srcPhoto.setOnClickListener {
                    curatedOnItemClickListener.onItemClick(model)
                }

                viewHolderGrid.mainItemView.setOnClickListener {
                    curatedOnItemClickListener.onItemClick(model)
                }
            }
//            populateItemRows(viewHolder as ItemViewHolder?, position)
        } else if (holder is CuratedFragLoadingViewHolder) {
//            showLoadingView(viewHolder as LoadingViewHolder?, position)
        }
    }

    override fun getItemCount(): Int {
        return if (mItemList == null) 0 else mItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mItemList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun updateListItems(updatedList: ArrayList<CuratedPhotoResult>) {
        mItemList.clear()
        mItemList = updatedList
        notifyDataSetChanged()
    }

    fun addListItems(addList: ArrayList<CuratedPhotoResult>) {
        val lastIndex = addList.size - 1
        mItemList.addAll(addList)
        notifyItemRangeInserted(lastIndex, addList.size)
    }

    class CuratedFragListViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val mainItemView: CardView = item.findViewById(R.id.mainItemView)
        val srcPhoto: ImageView = item.findViewById(R.id.srcPhoto)
        val photographer: TextView = item.findViewById(R.id.photographer)
        val photographerUrl: TextView = item.findViewById(R.id.photographerUrl)
    }

    class CuratedFragGridViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val mainItemView: CardView = item.findViewById(R.id.mainItemView2)
        val srcPhoto: ImageView = item.findViewById(R.id.srcPhoto2)
        val photographer: TextView = item.findViewById(R.id.photographer2)
        val photographerUrl: TextView = item.findViewById(R.id.photographerUrl2)
    }

    class CuratedFragLoadingViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val progressBar: ProgressBar = item.findViewById(R.id.progressBar)
    }
}