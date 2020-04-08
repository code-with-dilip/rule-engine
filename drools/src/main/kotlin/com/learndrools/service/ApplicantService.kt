package com.learndrools.service

import com.learndrools.config.DroolsRuleFactory
import com.learndrools.domain.Applicant
import com.learndrools.domain.SuggestedRole

class ApplicantService {

    val kieSession = DroolsRuleFactory().getKieSession()

    fun suggestedRoleForApplicant( applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

        kieSession.insert(applicant)
        kieSession.setGlobal("suggestedRole", suggestedRole)
        kieSession.fireAllRules()
        return suggestedRole
    }
}