package com.example.rpgapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rpgapp.data.dao.*
import com.example.rpgapp.data.entity.*

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Criar tabela de fichas Velho Oeste
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS fichas_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                pontaria INTEGER NOT NULL DEFAULT 0,
                vigor INTEGER NOT NULL DEFAULT 0,
                esperteza INTEGER NOT NULL DEFAULT 0,
                carisma INTEGER NOT NULL DEFAULT 0,
                reflexos INTEGER NOT NULL DEFAULT 0,
                vidaAtual INTEGER NOT NULL DEFAULT 0,
                vidaMax INTEGER NOT NULL DEFAULT 0,
                municao INTEGER NOT NULL DEFAULT 0,
                dinheiro TEXT NOT NULL DEFAULT '0',
                nome TEXT NOT NULL DEFAULT '',
                jogador TEXT NOT NULL DEFAULT '',
                arquetipo TEXT NOT NULL DEFAULT '',
                origem TEXT NOT NULL DEFAULT '',
                reputacao TEXT NOT NULL DEFAULT '',
                aparencia TEXT NOT NULL DEFAULT '',
                personalidade TEXT NOT NULL DEFAULT '',
                historia TEXT NOT NULL DEFAULT '',
                anotacoes TEXT NOT NULL DEFAULT ''
            )
        """)

        // Criar tabela de perícias Velho Oeste
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS pericias_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                atributo TEXT NOT NULL,
                treino INTEGER NOT NULL DEFAULT 0,
                bonus INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Criar tabela de itens Velho Oeste
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS itens_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                tipo TEXT NOT NULL DEFAULT 'Item',
                quantidade TEXT NOT NULL DEFAULT '1',
                descricao TEXT NOT NULL DEFAULT ''
            )
        """)
    }
}

@Database(
    entities = [
        FichaEntity::class,
        PericiaEntity::class,
        ItemEntity::class,
        FichaVelhoOesteEntity::class,
        PericiaVelhoOesteEntity::class,
        ItemVelhoOesteEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fichaDao(): FichaDao
    abstract fun periciaDao(): PericiaDao
    abstract fun itemDao(): ItemDao
    abstract fun fichaVelhoOesteDao(): FichaVelhoOesteDao
    abstract fun periciaVelhoOesteDao(): PericiaVelhoOesteDao
    abstract fun itemVelhoOesteDao(): ItemVelhoOesteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rpg_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}