package com.learndrools.service

import com.learndrools.config.DroolsRuleFactory
import com.learndrools.constants.RuleEngineConstants
import com.learndrools.domain.Applicant
import com.learndrools.domain.SuggestedRole
import org.kie.api.KieServices


class ApplicantService {

    val kieSession = DroolsRuleFactory().getKieSession()
    val ruleEngineConstant = RuleEngineConstants()

    fun suggestedRoleForApplicant(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

        kieSession.insert(applicant) // this is a fact to the rule engine
        kieSession.insert(suggestedRole)
        //kieSession.setGlobal("suggestedRole", suggestedRole)
        kieSession.setGlobal("ruleEngineConstant", ruleEngineConstant)
        kieSession.fireAllRules()
        return suggestedRole
    }

    fun loadRulesFromClassPath(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

        val ks = KieServices.Factory.get()
        // Let's load the configurations for the kmodule.xml file
        //  defined in the /src/test/resources/META-INF/ directory
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newKieSession("rules.applicant.suggestapplicant.session")
        kSession.insert(applicant)
        kSession.insert(suggestedRole)
        kSession.setGlobal("ruleEngineConstant", ruleEngineConstant)
        kSession.fireAllRules()
        return suggestedRole

    }

    fun loadRulesFromClassPath_Stateless(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newStatelessKieSession("rules.applicant.suggestapplicant.session_stateless")
        kSession.setGlobal("ruleEngineConstant", ruleEngineConstant)
        val applicant = ks.commands.newInsert(applicant)
        val suggestedRole = ks.commands.newInsert(suggestedRole, "suggestedRoleOut")
        val newFireAllRules = ks.commands.newFireAllRules("outFired")
        // kSession.(applicant)
        val commands = listOf(applicant, suggestedRole, newFireAllRules)
        val execResults = kSession.execute(ks.commands.newBatchExecution(commands))
        return execResults.getValue("suggestedRoleOut") as SuggestedRole
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