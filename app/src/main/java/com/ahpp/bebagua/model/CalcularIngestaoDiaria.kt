package com.ahpp.bebagua.model

class CalcularIngestaoDiaria {
    private val ML_Jovem = 0.04f //0 a 17
    private val ML_Adulto = 0.035f //18 a 55
    private val ML_Idoso = 0.03f //56 a 65
    private val ML_66Mais = 0.025f //66+

    fun CalcularTotalMl(peso: Float, idade: Int): Float{
        when (idade){
            in 0..17 -> return  peso * ML_Jovem
            in 18 .. 55 -> return  peso * ML_Adulto
            in 56 .. 65 -> return  peso * ML_Idoso
            else -> return peso * ML_66Mais
        }
    }
}