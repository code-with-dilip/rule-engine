package com.learndrools.config

import org.kie.api.KieServices
import org.kie.api.builder.KieBuilder
import org.kie.api.builder.KieFileSystem
import org.kie.api.io.Resource
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import org.kie.internal.io.ResourceFactory
import java.io.IOException


class DroolsRuleFactory {

    val RULES_PATH = "rules/applicant"

    private val kieServices = KieServices.Factory.get()

    fun kieFileSystem(): KieFileSystem {
        val kieFileSystem = kieServices.newKieFileSystem()
        val rules = listOf("SuggestApplicant.drl")
        rules.forEach {
            kieFileSystem.write(ResourceFactory.newClassPathResource(it))
        }
        return kieFileSystem

    }

    @Throws(IOException::class)
    fun getKieContainer(): KieContainer? {
        getKieRepository()
        val kb: KieBuilder = kieServices.newKieBuilder(kieFileSystem())
        kb.buildAll()
        val kieModule = kb.kieModule
        return kieServices.newKieContainer(kieModule.releaseId)
    }

    fun getKieRepository() {
        val kieRepository = kieServices.repository
        kieRepository.addKieModule { kieRepository.defaultReleaseId }
    }

    fun getKieSession() : KieSession{

        getKieRepository()
        val kieFileSystem = kieServices.newKieFileSystem()
        kieFileSystem.write(ResourceFactory.newClassPathResource("$RULES_PATH/SuggestApplicant.drl"));
        val kb = kieServices.newKieBuilder(kieFileSystem)
        kb.buildAll()
        val kieModule = kb.kieModule
        val kContainer = kieServices.newKieContainer(kieModule.releaseId)
        return kContainer.newKieSession()
    }

    fun getKieSession(dt: Resource?): KieSession? {
        val kieFileSystem = kieServices.newKieFileSystem()
            .write(dt)
        val kieBuilder = kieServices.newKieBuilder(kieFileSystem)
            .buildAll()
        val kieRepository = kieServices.repository
        val krDefaultReleaseId = kieRepository.defaultReleaseId
        val kieContainer = kieServices.newKieContainer(krDefaultReleaseId)
        return kieContainer.newKieSession()
    }


}