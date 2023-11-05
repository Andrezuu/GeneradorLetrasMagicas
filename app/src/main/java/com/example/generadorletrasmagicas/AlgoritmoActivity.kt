package com.example.generadorletrasmagicas

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.generadorletrasmagicas.databinding.AlgoritmoBinding
import org.json.JSONObject
import java.util.Stack

class AlgoritmoActivity : AppCompatActivity() {

    private val wordAdapter by lazy {WordAdapter()}
    private val trie = Node(' ', false, 0)
    private val context: Context = this
    private val englishWords = mutableSetOf<String>()
    private val possibleWords = mutableSetOf<String>()
    private var letters = ""

    private lateinit var binding: AlgoritmoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlgoritmoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonWords = readJson(context, "dictionary.json")
        val keys = jsonWords.keys()
        for (key in keys) {
            englishWords.add(key)
            insertWord(key)
        }

        binding.recyclerWords.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 50
                }
            })

            adapter = wordAdapter
        }

        binding.generar.setOnClickListener {
            possibleWords.clear()
            letters = binding.words.text.toString()
            dfs()
            iniciarWordsRecyclerView()
        }


    }

    fun readJson(context: Context, fileName: String): JSONObject {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return JSONObject(jsonString)
    }

    data class Node(
        var currentCharacter: Char,
        var isWord: Boolean,
        var count: Int
    ) {
        val children = Array<Node?>(27) { null }
    }


    fun insertWord(word: String) {
        var currentNode = trie
        for (char in word) {
            val character = char - 'a'
            if (currentNode.children[character] == null) {
                currentNode.children[character] = Node(char, false, 0)
            }
            currentNode.count++
            currentNode = currentNode.children[character]!!
            currentNode.currentCharacter = char
        }
        currentNode.count++
        currentNode.isWord = true
    }

    fun isPossibleWord(word: String): Boolean {
        val letterCount = IntArray(26) { 0 }
        //lleva la cuenta de cada letra del input
        for (char in letters) {
            letterCount[char - 'a']++
        }

        for (char in word) {
            //si es que hay una letra por usar se la quita
            //si ya no hay letras para usar devuelve falso directo
            if (letterCount[char - 'a'] > 0) {
                letterCount[char - 'a']--
            } else {
                return false
            }
        }
        return true
    }

    fun dfs() {
        //dfs para recorrer el trie como tal
        //se relaciona cada palabra posible del trie con su nodo respectivo
        val stack = Stack<Pair<String, Node>>()
        stack.push("" to trie)

        while (stack.isNotEmpty()) {
            val (actWord, actNode) = stack.pop()
            for (child in actNode.children) {
                //agrega todos los child posibles del trie al stack
                //agrega al actWord el caracter actual, relacionandolo con su nodo
                if (child != null) {
                    stack.push(actWord + child.currentCharacter to child)
                }
            }

            //si el nodoactual es una palabra y se puede formar con las letras del input
            //se agrega a la lista para mostrarla
            if (actNode.isWord && isPossibleWord(actWord) && actWord.length > 1) {
                possibleWords.add(actWord)
            }
        }
    }

    fun iniciarWordsRecyclerView() {
        wordAdapter.addWords(possibleWords.toList())
        binding.recyclerWords.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            adapter = wordAdapter

        }
    }
}