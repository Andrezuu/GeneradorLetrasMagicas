package com.example.generadorletrasmagicas

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.generadorletrasmagicas.databinding.ItemWordBinding

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordAdapterViewHolder>() {
    private var context: Context? = null
    private val listaWords = mutableListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordAdapter.WordAdapterViewHolder {
        context = parent.context
        return WordAdapterViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordAdapter.WordAdapterViewHolder, position: Int) {
        holder.binding(listaWords[position])
    }

    override fun getItemCount() = listaWords.size

    inner class WordAdapterViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(data: String) {
            binding.word.text = data
        }

    }

    fun addWords(newWords: List<String>) {
        listaWords.clear()
        listaWords.addAll(newWords)
    }

}