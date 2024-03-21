package com.example.final_project_bryants.ui.calendar

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_bryants.data.TimeCapsuleItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.final_project_bryants.R

class DateDisplayAdapter (capsules: List<TimeCapsuleItem>) : RecyclerView.Adapter<DateDisplayAdapter.DateDisplayViewHolder>() {

    private var capsulesList: List<TimeCapsuleItem> = capsules

    override fun getItemCount() = capsulesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateDisplayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_capsule_date_item, parent, false)
        return DateDisplayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateDisplayViewHolder, position: Int) {
        holder.bind(capsulesList[position], capsulesList[position].type)
    }

    class DateDisplayViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private lateinit var currentTimeCapsule: TimeCapsuleItem
        private val capsuleTV: TextView = view.findViewById(R.id.tv_capsule)
        private val capsuleIV: ImageView = view.findViewById(R.id.iv_capsule)

        fun bind(timeCapsuleItem: TimeCapsuleItem, type: String) {

            currentTimeCapsule = timeCapsuleItem


            when (type) {
                "Text", "Link" -> {
                    capsuleTV.visibility = View.VISIBLE
                    capsuleIV.visibility = View.GONE
                    capsuleTV.text = currentTimeCapsule.content
                }
                "Photo" -> {
                    capsuleTV.visibility = View.GONE
                    capsuleIV.visibility = View.VISIBLE
                    Log.d("Adapter test", currentTimeCapsule.content)
                    //capsuleIV.setImageURI(Uri.parse(currentTimeCapsule.content))
                    Glide.with(itemView.context)
                        .load(currentTimeCapsule.content).fitCenter()
                        .into(capsuleIV)
                }
            }
        }
    }
}