package com.learndrools.service

import com.learndrools.config.DroolsRuleFactory
import com.learndrools.domain.Applicant
import com.learndrools.domain.SuggestedRole
import org.kie.api.KieServices


class ApplicantService {

    val kieSession = DroolsRuleFactory().getKieSession()

    fun suggestedRoleForApplicant(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

         kieSession.insert(applicant) // this is a fact to the rule engine
        kieSession.setGlobal("suggestedRole", suggestedRole)
        kieSession.fireAllRules()
        return suggestedRole
    }

    fun loadRulesFromClassPath(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole{

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer

        // Let's load the configurations for the kmodule.xml file
        //  defined in the /src/test/resources/META-INF/ directory
        val kSession = kContainer.newKieSession("rules.applicant.suggestapplicant.session")
        kSession.insert(applicant)
        //kSession.insert(suggestedRole)
        kSession.setGlobal("suggestedRole", suggestedRole)
        kSession.fireAllRules()
        return suggestedRole

    }

    fun loadRulesFromClassPath_Stateless(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole{

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer

        // Let's load the configurations for the kmodule.xml file
        //  defined in the /src/test/resources/META-INF/ directory
        val kSession = kContainer.newStatelessKieSession("rules.applicant.suggestapplicant.session")
       // kSession.(applicant)
        kSession.setGlobal("suggestedRole", suggestedRole)
//        kSession.fireAllRules()
        return suggestedRole

    }

    fun suggestedRoleForApplicantUsingOwnLogic(applicant: Applicant): SuggestedRole {
        when {
            (applicant.experienceInYears > 10 && applicant.currentSalary in (1000000..2500000)) -> /*&& applicant.currentSalary in (1000000..2500000)*/ {
                println("inside greater than 10")
                val suggestedRole = SuggestedRole()
                suggestedRole.role = "Manager"
                return suggestedRole
            }
            (applicant.experienceInYears in (5..10) && applicant.currentSalary in (500000..1500000)) -> {
                println("inside greater than 10")
                val suggestedRole = SuggestedRole()
                suggestedRole.role = "Senior Developer"
                return suggestedRole
            }
            (applicant.experienceInYears in (0..5) && applicant.currentSalary in (200000..1000000)) -> {
                println("inside greater than 10")
                val suggestedRole = SuggestedRole()
                suggestedRole.role = "Developer"
                return suggestedRole
            }
            else -> {
                val suggestedRole = SuggestedRole()
                suggestedRole.role = "Unknown"
                return suggestedRole
            }
        }
    }
}