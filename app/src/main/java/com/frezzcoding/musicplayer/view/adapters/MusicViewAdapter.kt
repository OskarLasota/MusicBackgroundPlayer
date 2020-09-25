package com.frezzcoding.musicplayer.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.databinding.SongItemLayoutBinding
import com.frezzcoding.musicplayer.models.Song


class MusicViewAdapter(private val data : List<Song>, private val listener : OnItemClickListener) : RecyclerView.Adapter<MusicViewAdapter.ViewHolder>() {

    private lateinit var binding : SongItemLayoutBinding
    private var selectedPos = RecyclerView.NO_POSITION
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater : LayoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.song_item_layout, parent, false)
        context = parent.context
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.isSelected = (selectedPos == position)
        holder.bind(data[position])
        binding.topLayout.setOnClickListener {
            listener.onSongClick(data[position])
            notifyItemChanged(selectedPos)
            selectedPos = position
            notifyItemChanged(selectedPos)
        }
        binding.ivEdit.setOnClickListener {
            listener.onEditClick(data[position])
        }
    }

    class ViewHolder(private var binding : SongItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song : Song){
            binding.song = song

        }
    }



    interface OnItemClickListener{
        fun onSongClick(song: Song)
        fun onEditClick(song: Song)
    }


}