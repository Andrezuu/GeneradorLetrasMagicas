package com.example.generadorletrasmagicas

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.generadorletrasmagicas.databinding.AlgoritmoBinding
import org.json.JSONObject
import java.util.Stack

class AlgoritmoActivity: AppCompatActivity() {

    private val trie = Node(' ', false, 0)
    private val context: Context = this
    private val englishWords = mutableSetOf<String>()

    private lateinit var binding: AlgoritmoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlgoritmoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonWords = readJson(context, "dictionary.json")
        val keys = jsonWords.keys()
        for(key in keys) {
            englishWords.add(key)
            insertWord(key)
        }

        dfs("being")

    }

        fun readJson(context: Context, fileName: String): JSONObject {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val jsonString = inputStream.bufferedReader().use {it.readText()}
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

    fun isPossibleWord(word: String, letters: String): Boolean {
        val letterCount = IntArray(26) { 0 }
        for (char in letters) {
            letterCount[char - 'a']++
        }

        for (char in word) {
            if (letterCount[char - 'a'] > 0) {
                letterCount[char - 'a']--
            } else {
                return false
            }
        }
        return true
    }

    fun dfs(letters:String) {
        val stack = Stack<Pair<String, Node>>()
        stack.push("" to trie)

        while (stack.isNotEmpty()) {
            val (actWord, actNode) = stack.pop()
            for (child in actNode.children) {
                if (child != null) {
                    stack.push(actWord + child.currentCharacter to child)
                }
            }
            if (actNode.isWord && isPossibleWord(actWord, letters)) {
                println(actWord)
            }
        }
    }
}