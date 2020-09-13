package com.frezzcoding.musicplayer.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.databinding.MusiclistCardBinding
import com.frezzcoding.musicplayer.models.Song
import java.io.File


class MusicViewAdapter(private val data : List<Song>, val listener : OnItemClickListener) : RecyclerView.Adapter<MusicViewAdapter.ViewHolder>() {

    private lateinit var binding : MusiclistCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater : LayoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.musiclist_card, parent, false)

        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    class ViewHolder(private var binding : MusiclistCardBinding) : RecyclerView.ViewHolder(binding.root){


        fun bind(song : Song, listener : OnItemClickListener){
            binding.song = song

            binding.ivEdit.setOnClickListener {
                listener.onItemClick(song)
            }
        }
    }



    interface OnItemClickListener{
        fun onItemClick(song: Song)
    }


}