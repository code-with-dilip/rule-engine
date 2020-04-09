# Drools

-   Drools is a **Business Rule Management System** 

## Rules Execution LifeCycle

-   Rules Execution Lifecycle has two parts
    -   Rules Evaluation
        -   Rule Engine does not execute the rules as soon as the rules are evaluated to be true
        -   It first creates an object named  **Agenda** which has the complete info about the rules that are matched and its relevant data
    -   Rules Execution
        -   This is done through a method excecution in the rule engine
    -   Rule Execution Ordering
        -   The rule execution order is completely managed by the rule engine itself
                
## Rule Engine

-   The rule engine transforms the business rules that we define to an executable decision tree through a specific algorithm
-   The algorithm thats used with Drools are **PHREAK**
-   In the generated execution tree, every condition in our rules will be transformed to a node in the tree and how the different conditions connect to each other in our rules will determine the way these nodes will be connected. As we add data to our rule engine, it will be evaluated in batches, flowing through the network using the most optimized paths possible.
-   Each time we add more data to the rule engine, it is introduced through the root of the execution tree.      

## Drools Runtime

-   To spin up a new instance of rule engine we need to understand the below concepts.

-   KieServices
    -   KieServices class that gives us access to all these other concepts by providing a registry of services where we can find helpers for different purposes.
-   KieContainer
-   KieModule
-   KieBase
-   KieSession 

### KieServices

-   To Create an instance of KieService by running the below command

```
KieServices ks = KieServices.Factory.get();
```

-   Using the **KieServices**, we can create an instance of KieContainer.
-   Using KieContainer, we can defines the scope of the rules that will be used to create new instances of the Rule Engine
-   A KieContainer can hold Kiemodule and its dependencies
    -   This allows us so instantiate a KieModule
-   In Drools 6, everything is created around KieModules.
    -   KieModule consists of the following
        -   Business Rules, other assets among its resources
        -   This is a special file **kmodule.xml** in the **META-INF** directory
        
### KieContainer & KieModule

-   A **KieContainer** can be created using the **KieServices**
-   This provides the runtime for the RuleEngine. 
    -   Basically all the RuleEngine Instances can be created inside the container.
-   A KieContainer can create multiple instances of the rules engine with different configurations     

### How to Instantiate KieContainer

#### Loading Rules from classpath using the ClassPathContainer

- Configuring the **kmodule.xml** file under the **resources/META-INF** directory

```
<kmodule xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xmlns="http://jboss.org/kie/6.0.0/kmodule">
    <kbase name="rules.suggestapplicant">
        <ksession name="rules.suggestapplicant.session" type="stateful"/>
    </kbase>
</kmodule>
```

-   Accessing the rule using the classpathContainer method and kieSession Name   

```
fun loadRulesFromClassPath(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole{

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newKieSession("rules.suggestapplicant.session")
        kSession.insert(applicant)
        kSession.setGlobal("suggestedRole", suggestedRole)
        kSession.fireAllRules()
        return suggestedRole

    }
```

        
### KieModule

-   In drools everything is about KieModule. 
-   KieModule contains business assets(rules, processes and so on).
-   Using KieModule we can group rules based on the business functionality
    -   We can create sub KieModules based on the grouping of business rules.
-   The KieModule is created and represented by the  **kmodule.xml** file
-   A KieModule configuration consists of the following
    -   KieBase -   Represents a compiled version of a set of assets
    -   KieSession  -   Represents an instance of the rule engine containing the rules in the KieBase         


### KieSession
-   Represents the running instance of rule engine with specific configuration and set of rules.