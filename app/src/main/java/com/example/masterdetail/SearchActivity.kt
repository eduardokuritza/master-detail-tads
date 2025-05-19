package com.example.masterdetail


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.masterdetail.data.DBHelper

class SearchActivity : AppCompatActivity() {
    private lateinit var db: DBHelper

    private lateinit var btnSearch : Button
    private lateinit var etQuery : EditText
    private  lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        db = DBHelper(this)

        btnSearch = findViewById<Button>(R.id.btnSearch)
        etQuery = findViewById<EditText>(R.id.etQuery)
        tvResults = findViewById<TextView>(R.id.tvResults)

        etQuery.setText(intent.getStringExtra("MASTER_QUERY") ?: "")

        btnSearch.setOnClickListener {
            val q = etQuery.text.toString()
            val results = db.searchTeams(q)
            tvResults.text = if (results.isEmpty()) {
                "Nenhum resultado para \"$q\""
            } else {
                results.joinToString("\n") {
                    "${it.name} — ${it.city}, fundado em ${it.founded}"
                }
            }
        }
        if (etQuery.text.isNotBlank()) btnSearch.performClick()
    }
}