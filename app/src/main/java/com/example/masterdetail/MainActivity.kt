package com.example.masterdetail

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.masterdetail.data.DBHelper
import com.example.masterdetail.model.Team


class MainActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private val teams = mutableListOf<Team>()

    private lateinit var btnAdd: Button
    private lateinit var btnSearchMaster: Button
    private lateinit var etSearchMaster: EditText
    private lateinit var lvTeams: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)

        btnAdd = findViewById(R.id.btnAdd)
        btnSearchMaster = findViewById(R.id.btnSearchMaster)
        etSearchMaster = findViewById(R.id.etSearchMaster)
        lvTeams = findViewById(R.id.lvTeams)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
        btnSearchMaster.setOnClickListener {
            val i = Intent(this, SearchActivity::class.java)
            i.putExtra("MASTER_QUERY", etSearchMaster.text.toString())
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        loadList()
    }

    private fun loadList() {
        teams.clear()
        teams += db.getAllTeams()
        lvTeams.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            teams.map { it.name }
        )
        lvTeams.setOnItemClickListener { _,_, pos, _ ->
            Intent(this, DetailActivity::class.java).also {
                it.putExtra("TEAM_ID", teams[pos].id)
                startActivity(it)
            }
        }
    }
}