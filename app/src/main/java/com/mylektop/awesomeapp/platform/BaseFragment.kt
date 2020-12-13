package com.mylektop.awesomeapp.platform

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

/**
 * Base Fragment for all Fragments.
 * Handles Alert, Toast, Logger etc
 * Created by iddangunawan on 12/13/20
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    fun showToast(msg: String) {
        activity!!.runOnUiThread {
            val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun loadImageURL(context: Context, imageView: ImageView, imageURL: String) {
        Glide.with(context).load(imageURL)
            .fallback(android.R.drawable.stat_notify_error)
            .timeout(4500)
            .into(imageView)
    }
}