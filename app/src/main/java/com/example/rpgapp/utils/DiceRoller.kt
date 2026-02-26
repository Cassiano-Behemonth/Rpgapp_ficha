package com.example.rpgapp.utils

object DiceRoller {
    /**
     * Rola dados para DANO - soma todos os resultados
     */
    fun rolarDano(expr: String): Pair<Int, String> {
        val regex = Regex("(\\d+)d(\\d+)([+\\-*/]\\d+)?")
        val match = regex.find(expr.lowercase()) ?: return Pair(0, "")
        val qtd = match.groupValues[1].toIntOrNull() ?: return Pair(0, "")
        val faces = match.groupValues[2].toIntOrNull() ?: return Pair(0, "")
        val modStr = match.groupValues[3]
        val resultados = mutableListOf<Int>()
        repeat(qtd) { resultados.add((1..faces).random()) }
        val somaResultados = resultados.sum()
        val resultadoFinal = if (modStr.isNotEmpty()) {
            val operador = modStr[0]
            val valor = modStr.substring(1).toIntOrNull() ?: 0
            when (operador) {
                '+' -> somaResultados + valor
                '-' -> somaResultados - valor
                '*' -> somaResultados * valor
                '/' -> if (valor != 0) somaResultados / valor else somaResultados
                else -> somaResultados
            }
        } else somaResultados
        val textoFinal = if (qtd > 1) {
            if (modStr.isNotEmpty()) "dados: ${resultados.joinToString("+")} = $somaResultados $modStr = $resultadoFinal"
            else "dados: ${resultados.joinToString("+")} = $somaResultados"
        } else {
            if (modStr.isNotEmpty()) "dado: $somaResultados $modStr = $resultadoFinal"
            else "dado: $somaResultados"
        }
        return Pair(resultadoFinal, textoFinal)
    }

    /**
     * Rola dados para ACERTO - pega apenas o maior resultado
     */
    fun rolarAcerto(expr: String): Pair<Int, String> {
        val regex = Regex("(\\d+)d(\\d+)([+\\-*/]\\d+)?")
        val match = regex.find(expr.lowercase()) ?: return Pair(0, "")
        val qtd = match.groupValues[1].toIntOrNull() ?: return Pair(0, "")
        val faces = match.groupValues[2].toIntOrNull() ?: return Pair(0, "")
        val modStr = match.groupValues[3]
        val resultados = mutableListOf<Int>()
        repeat(qtd) { resultados.add((1..faces).random()) }
        val maiorResultado = resultados.maxOrNull() ?: 0
        val resultadoFinal = if (modStr.isNotEmpty()) {
            val operador = modStr[0]
            val valor = modStr.substring(1).toIntOrNull() ?: 0
            when (operador) {
                '+' -> maiorResultado + valor
                '-' -> maiorResultado - valor
                '*' -> maiorResultado * valor
                '/' -> if (valor != 0) maiorResultado / valor else maiorResultado
                else -> maiorResultado
            }
        } else maiorResultado
        val textoFinal = if (qtd > 1) {
            if (modStr.isNotEmpty()) "dados: ${resultados.joinToString(", ")} = $maiorResultado $modStr = $resultadoFinal"
            else "dados: ${resultados.joinToString(", ")} = $maiorResultado"
        } else {
            if (modStr.isNotEmpty()) "dado: $maiorResultado $modStr = $resultadoFinal"
            else "dado: $maiorResultado"
        }
        return Pair(resultadoFinal, textoFinal)
    }

    fun rolarCustom(expr: String): Pair<Int, String> {
        return rolarDano(expr)
    }
}
