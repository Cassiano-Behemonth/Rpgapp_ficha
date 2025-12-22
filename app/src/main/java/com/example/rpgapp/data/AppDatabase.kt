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
                fisico INTEGER NOT NULL DEFAULT 0,
                velocidade INTEGER NOT NULL DEFAULT 0,
                intelecto INTEGER NOT NULL DEFAULT 0,
                coragem INTEGER NOT NULL DEFAULT 0,
                defesa INTEGER NOT NULL DEFAULT 0,
                vidaAtual INTEGER NOT NULL DEFAULT 6,
                dorAtual INTEGER NOT NULL DEFAULT 0,
                dinheiro TEXT NOT NULL DEFAULT '',
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

        // Criar tabela de antecedentes Velho Oeste
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS antecedentes_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                pontos INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Criar tabela de habilidades Velho Oeste
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS habilidades_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                descricao TEXT NOT NULL DEFAULT '',
                danoOuDado TEXT NOT NULL DEFAULT ''
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
        AntecedenteVelhoOesteEntity::class,
        HabilidadeVelhoOesteEntity::class,
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
    abstract fun antecedenteVelhoOesteDao(): AntecedenteVelhoOesteDao
    abstract fun habilidadeVelhoOesteDao(): HabilidadeVelhoOesteDao
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