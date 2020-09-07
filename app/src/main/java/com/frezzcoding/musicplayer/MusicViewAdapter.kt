package com.frezzcoding.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.databinding.ActivityMainBinding
import com.frezzcoding.musicplayer.databinding.MusiclistCardBinding
import java.io.File


class MusicViewAdapter(private val data : List<File>) : RecyclerView.Adapter<MusicViewAdapter.ViewHolder>() {

    private lateinit var binding : MusiclistCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater : LayoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.musiclist_card, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ViewHolder(var binding : MusiclistCardBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(file : File){
            binding.file = file
        }

    }


}