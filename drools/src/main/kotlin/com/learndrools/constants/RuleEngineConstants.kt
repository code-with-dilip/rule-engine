package com.learndrools.constants

import java.time.LocalDate

open class RuleEngineConstants {
    companion object {
        const val MANAGER = "Manager"
        const val SENIOR_DEVELOPER = "Senior Developer"
        const val BOOLEAN_TRUE = true
        const val DEVELOPER = "developer"
        val HOBBIES = listOf("coaching", "mentoring", "pair_prograaming")
        val START_DATE = LocalDate.now().minusDays(7)
        val END_DATE = LocalDate.now().plusDays(30)



    }

    fun roles() = listOf("coaching", "mentoring", "pair_prograaming")


}