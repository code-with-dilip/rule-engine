package com.learnruleengine.launcher

import com.deliveredtechnologies.rulebook.FactMap
import com.deliveredtechnologies.rulebook.NameValueReferableMap
import com.learnruleengine.rule.HelloWorldRule


fun main() {

    val helloWorldRule =  HelloWorldRule().defineHelloWorldRules()
    //helloWorldRule.defineHelloWorldRules()

    val factMap = FactMap<String>()
    factMap.setValue("hello", "Hello world")
    factMap.setValue("world", "World hello")
    //factMap.setValue("hello", "hello")
    helloWorldRule?.run(factMap)


}