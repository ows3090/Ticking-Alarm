package ows.kotlinstudy.ticking_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.databinding.ItemViewTimeBinding
import javax.inject.Inject


class AlarmAdapter @Inject constructor() : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ItemViewTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: AlarmModel) {
            binding.meridiemTextView.text = if (item.alarmInfo.meridiem == MERIDIEM.ANTE) "오전" else "오후"
            binding.timeTextView.text = String.format("%02d:%02d", item.alarmInfo.hour % 12, item.alarmInfo.minute)
            binding.switchView.isChecked = item.alarmInfo.switchOn
            binding.switchView.setOnClickListener { item.onToggleEvent() }
            binding.deleteButton.setOnClickListener { item.onClickEvent() }
        }
    }

    private var alarmList = listOf<AlarmModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.ViewHolder {
        return ViewHolder(
            ItemViewTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(alarmList[position])
    }

    override fun getItemCount(): Int = alarmList.size

    fun setAlarmList(list: List<AlarmModel>) {
        this.alarmList = list
    }
}