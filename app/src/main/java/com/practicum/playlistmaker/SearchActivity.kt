package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView


class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
        var textSearchField: CharSequence? = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val iconDeletionText = findViewById<ImageView>(R.id.icDellText)
        val backButton = findViewById<Button>(R.id.back_button)
        val searchField = findViewById<EditText>(R.id.searchField)

        searchField.setText(textSearchField)


        iconDeletionText.setOnClickListener {
            searchField.setText("")
            it.visibility = doVisibilityIconDellText(searchField.text.toString())

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val searchFieldWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                iconDeletionText.visibility = doVisibilityIconDellText(s)
                textSearchField = searchField.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }

        searchField.addTextChangedListener(searchFieldWatcher)

        backButton.setOnClickListener {
            finish()
        }


    }

    fun doVisibilityIconDellText(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_SEARCH, textSearchField)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        textSearchField = savedInstanceState.getCharSequence(KEY_SEARCH)

    }

}