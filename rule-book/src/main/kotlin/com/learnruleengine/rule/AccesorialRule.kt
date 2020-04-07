package com.learnruleengine.rule

import com.deliveredtechnologies.rulebook.lang.RuleBuilder
import com.deliveredtechnologies.rulebook.model.rulechain.cor.CoRRuleBook
import com.learnruleengine.domain.Accessorial
import java.util.function.Predicate

class AccesorialRule : CoRRuleBook<HashMap<String, Double>>() {

    override fun defineRules() {
        addRule(RuleBuilder.create()
            .withFactType(Accessorial::class.java)
            .withResultType(Pair::class.java)
            .`when` { facts -> facts.one.weight > 50 }
            .then { t, u ->
                run {
                    u.value = Pair("WEIGHT", 24.0)
                }
            }
            .build())

        addRule(RuleBuilder.create()
            .withFactType(Accessorial::class.java)
            .withResultType(Pair::class.java)
            .`when` { facts -> facts.one.length > 48 }
            .then { t, u ->
                run {
                    u.value = Pair("HEIGHT", 15.0)
                }
            }
            //.stop()
            .build())
    }

}
