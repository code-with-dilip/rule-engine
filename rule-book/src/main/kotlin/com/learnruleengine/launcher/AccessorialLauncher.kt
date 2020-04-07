package com.learnruleengine.launcher

import com.deliveredtechnologies.rulebook.FactMap
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder
import com.learnruleengine.domain.Accessorial
import com.learnruleengine.rule.AccesorialRule

fun main() {

    println("inside Accessorial Launcher")

    val default = Double.MIN_VALUE
    val accessorial = Accessorial(51.0, 13.0, 13.0)
    val accessorial1 = Accessorial(24.0, 49.0, 13.0)
    val accessorialRule = RuleBookBuilder.create(AccesorialRule::class.java)
        .withResultType(Double::class.java)
        .withDefaultResult(Double.MIN_VALUE)
        .build()

    val factMap = FactMap<Accessorial>()
    factMap.setValue("accessorial", accessorial)
    accessorialRule.run(factMap)
    accessorialRule.result.ifPresent { println("Result is $it") }

    val factMap1 = FactMap<Accessorial>()
    factMap1.setValue("accessorial1", accessorial1)
    accessorialRule.run(factMap1)
    accessorialRule.result.ifPresent { println("Result is $it") }

}