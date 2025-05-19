package com.example.masterdetail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.masterdetail.data.DBHelper
import com.example.masterdetail.model.Team


class DetailActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private var teamId = 0

    private lateinit var btnSave : Button
    private lateinit var btnDelete : Button
    private lateinit var etName : EditText
    private lateinit var etCity : EditText
    private lateinit var etFounded : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        btnSave = findViewById<Button>(R.id.btnSave)
        btnDelete = findViewById<Button>(R.id.btnDelete)
        etName = findViewById<EditText>(R.id.etName)
        etCity = findViewById<EditText>(R.id.etCity)
        etFounded = findViewById<EditText>(R.id.etFounded)

        db = DBHelper(this)
        teamId = intent.getIntExtra("TEAM_ID", 0)
        if (teamId != 0) {
            val t = db.getTeamById(teamId)
            t?.let {

                etName.setText(it.name)
                etCity.setText(it.city)
                etFounded.setText(it.founded.toString())
                btnDelete.visibility = View.VISIBLE
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val city = etCity.text.toString().trim()
            val founded = etFounded.text.toString().toIntOrNull() ?: 0
            if (name.isEmpty() || city.isEmpty() || founded == 0) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val team = Team(id = teamId, name = name, city = city, founded = founded)
            if (teamId == 0) db.insertTeam(team)
            else db.updateTeam(team)
            finish()
        }

        btnDelete.setOnClickListener {
            db.deleteTeam(teamId)
            finish()
        }
    }
}