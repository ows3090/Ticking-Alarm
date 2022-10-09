package ows.kotlinstudy.ticking_alarm.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.ticking_alarm.databinding.ItemViewTimeBinding
import javax.inject.Inject

class MainAdapter @Inject constructor() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    val list = listOf<Int>()

    inner class ViewHolder(
        private val binding: ItemViewTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size
}