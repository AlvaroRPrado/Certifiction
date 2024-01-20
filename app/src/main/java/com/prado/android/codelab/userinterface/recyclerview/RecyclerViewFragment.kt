package com.prado.android.codelab.userinterface.recyclerview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.prado.android.R
import com.prado.android.databinding.FragmentRecyclerViewBinding
import com.prado.android.databinding.WordlistItemBinding

class RecyclerViewFragment : Fragment(R.layout.fragment_recycler_view) {

    private lateinit var binding: FragmentRecyclerViewBinding

    //0 Defina o modelo
    private var words = mutableListOf("Alvaro", "Priscila", "Carol", "Pedro")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecyclerViewBinding.bind(view)

        // +-----------------------------------------------------------------+
        // | Cool explosion animation                                        |
        // +-----------------------------------------------------------------+
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.circle_explosion_anim).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
        }

        //3 atribua o adapter ao seu recyclerView
        binding.wordRecycler.adapter = WordListAdapter(requireActivity(), words)
        binding.addWordButton.setOnClickListener {
            binding.root.setBackgroundColor(0) // make sure is set to re-trigger the anim again
            words.add("+ New word" + words.size)
            (binding.wordRecycler.adapter as WordListAdapter).notifyItemChanged(words.size)
            binding.wordRecycler.smoothScrollToPosition(words.size)

            // +-------------------------------------------------+
            // | Cool explosion animation                        |
            //+--------------------------------------------------+
            binding.animCircle.isVisible = true
            binding.animCircle.startAnimation(anim)
             //onEnd call back - do what ever you want here...]
             //exemple: change the background color
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
    }

}

//1 Crie se viewAdapter
class WordListAdapter(context: Context, val  words: MutableList<String>): RecyclerView.Adapter<WordListAdapter.WordViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = WordlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    // Aqui é onde acontece a "adaptação" do modelo para lyout xml
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
       holder.wordItemView.text = words[position]
    }

    // 2 Defina um viewHolder e implement o onclick de cada item
    inner class WordViewHolder(private var binding: WordlistItemBinding, private val adapter: WordListAdapter) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        val wordItemView: TextView = binding.word

        init {
            wordItemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            words[layoutPosition] = "Clicked!" + words[layoutPosition]
            adapter.notifyDataSetChanged()
        }

    }


}