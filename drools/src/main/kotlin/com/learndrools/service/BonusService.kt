package com.learndrools.service

import com.learndrools.constants.RuleEngineConstants
import com.learndrools.domain.Bonus
import com.learndrools.domain.Employee
import org.kie.api.KieServices

class BonusService {

    fun applyBonus(employee: Employee, bonus : Bonus) : Bonus {

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newKieSession("rules.applicant.bonus.session")
        kSession.insert(employee)
        kSession.insert(bonus)
        kSession.setGlobal("ruleEngineConstant" , RuleEngineConstants())
        kSession.fireAllRules()
        println("bonus ${bonus.bonusAmount}")
        return bonus
    }
}