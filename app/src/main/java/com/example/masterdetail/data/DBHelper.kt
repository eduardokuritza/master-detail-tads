package com.example.masterdetail.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.masterdetail.model.Team


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "football.db"
        private const val DB_VERSION = 1
        const val TABLE = "teams"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_CITY = "city"
        const val COL_FOUNDED = "founded"

        private const val CREATE_TABLE = """
            CREATE TABLE $TABLE (
              $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
              $COL_NAME TEXT NOT NULL,
              $COL_CITY TEXT NOT NULL,
              $COL_FOUNDED INTEGER NOT NULL
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
        db.execSQL("INSERT INTO $TABLE ($COL_NAME,$COL_CITY,$COL_FOUNDED) VALUES" +
                "('Flamengo','Rio de Janeiro',1895)," +
                "('Palmeiras','São Paulo',1914)," +
                "('Atlético Mineiro','Belo Horizonte',1908)," +
                "('Grêmio','Porto Alegre',1903)," +
                "('Internacional','Porto Alegre',1909)," +
                "('Cruzeiro','Belo Horizonte',1921)," +
                "('São Paulo','São Paulo',1930)," +
                "('Fluminense','Rio de Janeiro',1902)," +
                "('Botafogo','Rio de Janeiro',1904)," +
                "('Vasco da Gama','Rio de Janeiro',1898)," +
                "('Atlético Paranaense','Curitiba',1924)," +
                "('Corinthians','São Paulo',1910)")
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun getAllTeams(): List<Team> {
        val list = mutableListOf<Team>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE", null).use { c ->
            while (c.moveToNext()) {
                list += Team(
                    id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    name = c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    city = c.getString(c.getColumnIndexOrThrow(COL_CITY)),
                    founded = c.getInt(c.getColumnIndexOrThrow(COL_FOUNDED))
                )
            }
        }
        return list
    }

    fun getTeamById(id: Int): Team? {
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE WHERE $COL_ID = ?",
            arrayOf(id.toString())
        ).use { c ->
            if (c.moveToFirst()) {
                return Team(
                    id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    name = c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    city = c.getString(c.getColumnIndexOrThrow(COL_CITY)),
                    founded = c.getInt(c.getColumnIndexOrThrow(COL_FOUNDED))
                )
            }
        }
        return null
    }

    fun searchTeams(query: String): List<Team> {
        val list = mutableListOf<Team>()
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE WHERE $COL_NAME LIKE ?",
            arrayOf("%$query%")
        ).use { c ->
            while (c.moveToNext()) {
                list += Team(
                    id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    name = c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    city = c.getString(c.getColumnIndexOrThrow(COL_CITY)),
                    founded = c.getInt(c.getColumnIndexOrThrow(COL_FOUNDED))
                )
            }
        }
        return list
    }

    fun insertTeam(team: Team): Long {
        val cv = ContentValues().apply {
            put(COL_NAME, team.name)
            put(COL_CITY, team.city)
            put(COL_FOUNDED, team.founded)
        }
        return writableDatabase.insert(TABLE, null, cv)
    }

    fun updateTeam(team: Team): Int {
        val cv = ContentValues().apply {
            put(COL_NAME, team.name)
            put(COL_CITY, team.city)
            put(COL_FOUNDED, team.founded)
        }
        return writableDatabase.update(
            TABLE, cv,
            "$COL_ID = ?", arrayOf(team.id.toString())
        )
    }

    fun deleteTeam(id: Int): Int {
        return writableDatabase.delete(
            TABLE, "$COL_ID = ?", arrayOf(id.toString())
        )
    }
}