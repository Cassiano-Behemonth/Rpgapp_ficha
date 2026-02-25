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
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS fichas_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fisico INTEGER NOT NULL DEFAULT 0,
                velocidade INTEGER NOT NULL DEFAULT 0,
                intelecto INTEGER NOT NULL DEFAULT 0,
                coragem INTEGER NOT NULL DEFAULT 0,
                defesa INTEGER NOT NULL DEFAULT 0,
                vidaAtual INTEGER NOT NULL DEFAULT 0,
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

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS antecedentes_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                pontos INTEGER NOT NULL DEFAULT 0
            )
        """)

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS habilidades_velho_oeste (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL,
                descricao TEXT NOT NULL DEFAULT '',
                danoOuDado TEXT NOT NULL DEFAULT ''
            )
        """)

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

/**
 * MIGRATION 2 -> 3
 * Adiciona campos:
 * - selosMorteBonus em fichas_velho_oeste
 * - pesoBonus em fichas_velho_oeste
 * - peso em itens_velho_oeste
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE fichas_velho_oeste 
            ADD COLUMN selosMorteBonus INTEGER NOT NULL DEFAULT 0
        """)

        database.execSQL("""
            ALTER TABLE fichas_velho_oeste 
            ADD COLUMN pesoBonus INTEGER NOT NULL DEFAULT 0
        """)

        database.execSQL("""
            ALTER TABLE itens_velho_oeste 
            ADD COLUMN peso INTEGER NOT NULL DEFAULT 1
        """)
    }
}

/**
 * MIGRATION 3 -> 4
 * Adiciona campo de dano em itens_velho_oeste
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE itens_velho_oeste 
            ADD COLUMN dano TEXT NOT NULL DEFAULT ''
        """)
    }
}

/**
 * MIGRATION 4 -> 5
 * Adiciona todas as tabelas do modo Assimilação:
 * - fichas_assimilacao
 * - assimilacoes
 * - itens_assimilacao
 * - caracteristicas_assimilacao
 */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // Ficha principal do Assimilação
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS fichas_assimilacao (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nome TEXT NOT NULL DEFAULT '',
                jogador TEXT NOT NULL DEFAULT '',
                pontosNivel6 INTEGER NOT NULL DEFAULT 5,
                pontosNivel5 INTEGER NOT NULL DEFAULT 5,
                pontosNivel4 INTEGER NOT NULL DEFAULT 5,
                pontosNivel3 INTEGER NOT NULL DEFAULT 5,
                pontosNivel2 INTEGER NOT NULL DEFAULT 5,
                pontosNivel1 INTEGER NOT NULL DEFAULT 5,
                maxNivel6 INTEGER NOT NULL DEFAULT 5,
                maxNivel5 INTEGER NOT NULL DEFAULT 5,
                maxNivel4 INTEGER NOT NULL DEFAULT 5,
                maxNivel3 INTEGER NOT NULL DEFAULT 5,
                maxNivel2 INTEGER NOT NULL DEFAULT 5,
                maxNivel1 INTEGER NOT NULL DEFAULT 5,
                nivelDeterminacao INTEGER NOT NULL DEFAULT 9,
                pontosDeterminacao INTEGER NOT NULL DEFAULT 9,
                pontosAssimilacao INTEGER NOT NULL DEFAULT 1,
                influencia INTEGER NOT NULL DEFAULT 1,
                percepcao INTEGER NOT NULL DEFAULT 1,
                potencia INTEGER NOT NULL DEFAULT 1,
                reacao INTEGER NOT NULL DEFAULT 1,
                resolucao INTEGER NOT NULL DEFAULT 1,
                sagacidade INTEGER NOT NULL DEFAULT 1,
                biologia INTEGER NOT NULL DEFAULT 0,
                erudicao INTEGER NOT NULL DEFAULT 0,
                engenharia INTEGER NOT NULL DEFAULT 0,
                geografia INTEGER NOT NULL DEFAULT 0,
                medicina INTEGER NOT NULL DEFAULT 0,
                seguranca INTEGER NOT NULL DEFAULT 0,
                armas INTEGER NOT NULL DEFAULT 0,
                atletismo INTEGER NOT NULL DEFAULT 0,
                expressao INTEGER NOT NULL DEFAULT 0,
                furtividade INTEGER NOT NULL DEFAULT 0,
                manufaturas INTEGER NOT NULL DEFAULT 0,
                sobrevivencia INTEGER NOT NULL DEFAULT 0,
                eventoMarcante TEXT NOT NULL DEFAULT '',
                ocupacao TEXT NOT NULL DEFAULT '',
                proposito1 TEXT NOT NULL DEFAULT '',
                proposito2 TEXT NOT NULL DEFAULT '',
                propositoColetivo TEXT NOT NULL DEFAULT '',
                aparencia TEXT NOT NULL DEFAULT '',
                personalidade TEXT NOT NULL DEFAULT '',
                historia TEXT NOT NULL DEFAULT '',
                anotacoes TEXT NOT NULL DEFAULT ''
            )
        """)

        // Mutações/Assimilações adquiridas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS assimilacoes (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL DEFAULT '',
                tipo TEXT NOT NULL DEFAULT 'Evolutiva',
                descricao TEXT NOT NULL DEFAULT ''
            )
        """)

        // Itens do inventário
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS itens_assimilacao (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL DEFAULT '',
                quantidade TEXT NOT NULL DEFAULT '1',
                descricao TEXT NOT NULL DEFAULT '',
                slots INTEGER NOT NULL DEFAULT 1
            )
        """)

        // Características (talentos/vantagens)
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS caracteristicas_assimilacao (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fichaId INTEGER NOT NULL DEFAULT 0,
                nome TEXT NOT NULL DEFAULT '',
                custo INTEGER NOT NULL DEFAULT 1,
                requisitos TEXT NOT NULL DEFAULT '',
                descricao TEXT NOT NULL DEFAULT ''
            )
        """)
    }
}

@Database(
    entities = [
        // ── Modo Horror ──────────────────────────
        FichaEntity::class,
        PericiaEntity::class,
        ItemEntity::class,
        // ── Modo Velho Oeste ─────────────────────
        FichaVelhoOesteEntity::class,
        AntecedenteVelhoOesteEntity::class,
        HabilidadeVelhoOesteEntity::class,
        ItemVelhoOesteEntity::class,
        // ── Modo Assimilação ─────────────────────
        FichaAssimilacaoEntity::class,
        AssimilacaoEntity::class,
        ItemAssimilacaoEntity::class,
        CaracteristicaAssimilacaoEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // ── Modo Horror ──────────────────────────────
    abstract fun fichaDao(): FichaDao
    abstract fun periciaDao(): PericiaDao
    abstract fun itemDao(): ItemDao
    // ── Modo Velho Oeste ─────────────────────────
    abstract fun fichaVelhoOesteDao(): FichaVelhoOesteDao
    abstract fun antecedenteVelhoOesteDao(): AntecedenteVelhoOesteDao
    abstract fun habilidadeVelhoOesteDao(): HabilidadeVelhoOesteDao
    abstract fun itemVelhoOesteDao(): ItemVelhoOesteDao
    // ── Modo Assimilação ─────────────────────────
    abstract fun fichaAssimilacaoDao(): FichaAssimilacaoDao
    abstract fun assimilacaoDao(): AssimilacaoDao
    abstract fun itemAssimilacaoDao(): ItemAssimilacaoDao
    abstract fun caracteristicaAssimilacaoDao(): CaracteristicaAssimilacaoDao

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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}