package com.learnruleengine.rule

import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder
import com.deliveredtechnologies.rulebook.lang.RuleBookRuleBuilder
import com.deliveredtechnologies.rulebook.model.RuleBook

class HelloWorldRule {

    fun  defineHelloWorldRules() : RuleBook<Any>? {
        return RuleBookBuilder
            .create()
            .addRule { t ->
                t.withFactType(String::class.java)
                    .`when` { it.containsKey("hello") }
                    .using("hello")
                    .then { value -> run { println("result is $value") } }
            }
            .addRule { t ->
                t.withFactType(String::class.java)
                    .`when` { it.containsKey("world") }
                    .using("world")
                    .then { value -> run { println("result is $value") } }
            }
            .build()
    }

}