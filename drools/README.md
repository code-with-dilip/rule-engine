# Drools

-   Drools is a **Business Rule Management System**
-   Drools rules are data-driven. This means that the only way to activate a rule is by adding data to the engine that matches the conditions of that rule 

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
        -   Rules can be grouped together in to different **KieBases**
    -   KieSession  -   Represents an instance of the rule engine instace containing the rules in the KieBase         


### KieSession
-   Represents the running instance of rule engine with specific configuration and set of rules.
-   You can have two different types of session in here.
    -   stateless
        -   This does not maintain any state between interactions to the rule engine
    -   stateful
        -   This  maintains any state between interactions to the rule engine
        
- Configuring the StateFul and Stateless in kmodule file

```
<kmodule xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xmlns="http://jboss.org/kie/6.0.0/kmodule">
    <!--<kbase name="rules" default="true" includes="rules.applicant">-->
    <kbase name="rules.applicant">
        <ksession name="rules.applicant.suggestapplicant.session" type="stateful"/>
        <ksession name="rules.applicant.suggestapplicant.session_stateless" type="stateless"/>
    </kbase>
</kmodule>
```        

#### StateFul

```aidl
 fun loadRulesFromClassPath(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole{

        val ks = KieServices.Factory.get()
        // Let's load the configurations for the kmodule.xml file
        //  defined in the /src/test/resources/META-INF/ directory
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newKieSession("rules.applicant.suggestapplicant.session")
        kSession.insert(applicant)
        kSession.insert(suggestedRole)
        //kSession.setGlobal("suggestedRole", suggestedRole)
        kSession.fireAllRules()
        return suggestedRole

    }
```


#### StateLess

-   We need to create a new session everytime we need to run a set of rules
 
-   We have used the method **newStatelessKieSession** in the below example to create s new session all the time.

```aidl
    fun loadRulesFromClassPath_Stateless(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole{

        val ks = KieServices.Factory.get()
        val kContainer = ks.kieClasspathContainer
        val kSession = kContainer.newStatelessKieSession("rules.applicant.suggestapplicant.session_stateless")
        val applicant = ks.commands.newInsert(applicant)
        val suggestedRole = ks.commands.newInsert(suggestedRole, "suggestedRoleOut")
        val newFireAllRules = ks.commands.newFireAllRules("outFired")
        val commands = listOf(applicant, suggestedRole, newFireAllRules)
        val execResults = kSession.execute(ks.commands.newBatchExecution(commands))
        return execResults.getValue("suggestedRoleOut") as SuggestedRole
    }
```

## Exploring DRL

-   Drools rules are data-driven

### Triggering rules based on a rule evaluation

-   You have three options when it comes to triggering the rules based on a modification of the object based on a rule
    -   insert 
        -   Use insert to re-evaluate the rules for a newly inserted object 
    -   update
        -   Use update to re-evaluate the rules for the update to an already existing object 
    -   delete
        -   Use delete to if you dont want to execute any rules for a certain condition met
        -   Use this to remove the objects from the working memory
-   All the above options are very powerful as it provides a way to control the execution of rules        

#### insert
     
-   The insert call in the then block loads the new object IsGoldCustomer in to the memory and it rerun the rules that match the below condition
 
```aidl
rule "Classify Customer - Gold"
    when
        $c: Customer( category == Customer.Category.GOLD )
    then
        insert(new IsGoldCustomer($c));
end
```

#### update

- This is for the use-case where the object is already in the memory and there is an update made to it.    

```aidl
rule "Suggest Manager Role"
    when
        Applicant(experienceInYears > 10)
        $a: Applicant(currentSalary > 1000000 && currentSalary <= 2500000)
        $s: SuggestedRole()
    then
        //approach 1
        //modify($s){setRole("Manager")};

        //approach 2
        $s.setRole("Manager");
        update($s)
end
```

### Rule attributes

-   Rule attributes is an addition feature which provides a  way to control the execution of rules
-   Rule attributes will only be evaluated after the rule conditions have matched with a group of data in the working memory, therefore, if the rule was not going to be triggered with the existing data, it won't be triggered regardless of how high or low the salience value is set.
-   All the rule attributes should be given before the **when** condition
-   Some of the rule attributes are given below
    -   enabled 
        -   This will disable the rule even when the condition in the when clause matches
        -   Example:
        
        ```aidl
            enabled false
        ```
    -   salience 
        -   This is set a priority for a given rule
        -   The value of the priority can be a positive or negative one
        -   Example:
        ```aidl
            salience 10
        ```
    -   no-loop
        -   This is maninly to avoid infinite loop situation where an update to an object in the memory can re-evaluate the rule multiple times
        -   Example:
        ```aidl
            no-loop
        ```                 

#### Using global

-   global is basically used in cases where the whole rules requires something based on a condition

##### Usage

-   First we need to set the global for the kieSession

```aidl
        kieSession.setGlobal("ruleEngineConstant", ruleEngineConstant)
``` 

-   Reference the same in the drool file

```aidl
global com.learndrools.constants.RuleEngineConstants ruleEngineConstant;
```

-   Using the global constant in the rule

```aidl
$s : (SuggestedRole(role == ruleEngineConstant.MANAGER.toString()) or SuggestedRole(role == ruleEngineConstant.SENIOR_DEVELOPER.toString()))
```

-   Using the global constant in the rule with one of the Rule Attributes
    -   Always use the braces() to use something defined as global to evaluate a condition
        ```aidl
        rule "Suggest Senior developer Role"
        enabled (ruleEngineConstant.BOOLEAN_TRUE)
            when
                Applicant(experienceInYears > 5 && experienceInYears <= 10)
                Applicant(currentSalary > 500000 && currentSalary <= 1500000)
                $s: SuggestedRole()
            then
                $s.setRole("Senior Developer");
                update($s)
        end
        ```

#### agenda-group
-   This is basically used to group the rules

##### Usage

-   This is enabled by defining the rules in the "agenda-group" 

```aidl
rule "Suggest Developer Role"
agenda-group "developer"
    when
        Applicant(experienceInYears > 0 && experienceInYears <= 5)
        Applicant(currentSalary > 200000 && currentSalary <= 1000000)
        $s: SuggestedRole()
    then
        $s.setRole("Developer");
        update($s)
end

rule "Perform Manager Action"
agenda-group "developer"
    when
       $s : (SuggestedRole(role == ruleEngineConstant.MANAGER.toString()) or SuggestedRole(role == ruleEngineConstant.SENIOR_DEVELOPER.toString()))
    then
        System.out.println("Perform " + $s.getRole()  +" Action");
 end
``` 

-   By default all the rules have the implicit **MAIN** group.
-   In order to use the custom **agenda-group** we need to explicitly set that in the code
    - The same can be done by using the below command
    ```
    val kSession = kContainer.newKieSession("rules.applicant.suggestapplicant.session")
    ```     