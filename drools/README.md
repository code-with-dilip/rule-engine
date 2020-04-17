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
    -   lock-on-active
        -   This is mainly used when other rules invoke each other in an infinite loop. Enabling this flag makes this rule not ot be invoked anymore for the same objects
        -   Example:
        ```aidl
         lock-on-active true
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
    
    
#### date-effective/data-expires   

-   The below makes it effective on a given date

```aidl
rule "Hike Based On Performance"
    date-effective "01-Apr-2020"
     date-expires "31-Apr-2020"
when
    $e : Employee(performance >= 4)
    $b: Bonus()
then
    Double bonusValue = $e.getSalary() * 0.3;
    $b.setBonusAmount(bonusValue);
 end

```

### Controlling Loops in rules

#### Declared Types

-   Types can also be declared in the DRL file. Example given below

```aidl
declare SpecialOrder extends Order
      whatsSoSpecialAboutIt: String
      order: Order
      applicableDiscount: Discount
   end
```

-   But its really difficult to work with.

```aidl
KieSession ksession = ...; //previously initialized
FactType type = ksession.getKieBase().getFactType("chapter04.declaredTypes", "SpecialOrder");
Object instance = type.newInstance();
type.set(instance, "relevance", 2L);
Object attr = type.get(instance, "relevance");
```

#### Property-reactive beans

-   This is handy when you would like to reevaluate a rule based on the single property change

```aidl
 declare PropertyReactiveOrder
       @propertyReactive
       discount: Discount
       totalItems: Integer
       total: Double
   end
```


### Special Drools operations

#### Boolean and numeric operations

```aidl
Item( salePrice > 100.00 && salePrice <= 500.00&& salePrice != 101.00 )

```
-   It is best to avoid rules that has multiple conditions in it. 
-   The right way to use these conditions is to have a separate rule for each condition   
-   For performing those operations we need to use &&, ||. 

#### Regex operations – matches

```
rule "validate customer emails"
       when $c: Customer(email not matches "[A-Za-z0-9-.]+@[A-Za-z0-9-.]+$")
       then $c.setEmail(null); //invalidate email
   end
```

#### Collection operations – contains and memberOf

```aidl
rule "Hike Based On Performance Greater than 4 with Extra Role"
   /* date-effective "01-Apr-2020"
     date-expires "31-Apr-2020"*/
when
    $e : Employee(performance >= 4, extraRole memberOf ruleEngineConstant.roles(), ruleEngineConstant.roles() contains extraRole)
    $b: Bonus()
then
    Double bonusValue = $e.getSalary() * 0.4;
    $b.setBonusAmount(bonusValue);
    Map<String, String> pair = new HashMap<>();
    pair.put("ABC", "1");
    $b.getPointsMap().putAll(pair);
    //$b.setPointsMap(pair);
    System.out.println("First Rule");
 end
```

###  Working memory breakdown: the from clause

-   The **from** clause is a very versatile tool. It can be used to get data from multiple sources and not only from attributes.
-   In the below example the **from** clause to iterate through the list and match on a specific value on each and every element

```aidl
rule "Cricket Hobby"
    /* date-effective "01-Apr-2020"
      date-expires "31-Apr-2020"*/
 when
     $e : Employee($hobbies: hobbies)
     $ho: Hobby($h : hobby && hobby == "cricket") from $hobbies
 then
      System.out.println("Cricket Hobby Rule invoked" + $ho);
  end
```

#### Collect from objects

 -  If we insert 50 orders to KieSession and then fire the rules, the rule will fire only once, producing a list of 50 orders.
```aidl
rule "Grouping orders"
     when $list: List() from collect(Order())
     then
          System.out.println("we've found " +$list.size() + " orders");
   end
```

#### Accumulate keyword

-    The **accumulate** keyword is used to transform every match of a condition to a specific type of data.

-   Some common examples where the accumulate keyword is used are counting elements that match a specific condition, get average values of an attribute of certain type of objects, and to find the average, minimum, or maximum values of a specific attribute in a condition.

-   Check the book for examples.

#### Advanced conditional elements

-   NOT
-   EXISTS AND FORALL KEYWORDS
-   FORALL


**NOT KEYWORD**

```aidl
rule "warn about empty working memory"
    when
           not(Order() or Item())
       then
           System.out.println("we don't have elements");
   end
```

### Drools syntactic sugar

-   Nested accessors for the attributes of our types
    ```aidl
    OrderLine( item.cost < 30.0, item.salePrice < 25.0 )

    ```
    -   The better approach is to use types
    
    ```aidl
    OrderLine( item.( cost < 30.0,salePrice < 25.0) )
    ```    
-   Inline casts for attributes of our types
-   Null-safe operators
    ```aidl
    Order(customer!.category != Category.NA)
    ```
### Decorating our objects in memory

-   This is done using **Traits**

-   We can create a trait using the below example.

#### Creating a new trait 

```aidl
declare trait KidFriendly
    kidAppeal: String
end
```

-   Example usage of Trait is given below

```aidl
rule "toy items are kid friendly"
    no-loop
    when 
        $i: TraitableItem(name contains "toy")
    then
        KidFriendly kf = don($i, KidFriendly.class);
        kf.setKidAppeal("can play with it");
end

rule "Advertise kid friendly element"
    when 
        $kf: KidFriendly($ka: kidAppeal)
    then
        System.out.println("The element "+$kf+ 
            " is kid friendly because " + $ka);
end
```

#### Removing Traits

```aidl
Object o = shed( $traitedObject, KidFriendly.class)
```

### Logical insertion of elements

-   The **insertLogical** allows you to remove objects automatically from the memory is the condition is evaluated to be false.
-   Logical insertion not only avoid needing extra rules to sanitize our working memory, but also open the possibility of locking objects to specific conditions   

```
rule "determine large orders"
    when $o: Order(total > 150)
    then insertLogical(new IsLargeOrder($o));
end
```

#### Handling deviations of our rules

- This can be done using the **neg** operator

```aidl
rule "large orders exception"
       when $o: Order(total > 150, totalItems < 5)
       then insertLogical(new IsLargeOrder($o), "neg");
   end
```

### Rule Inheritance

-   Rule Inheritance is the concept of inheriting the rules behavior in to another rule.
-   This concept is similar to **Inheritance** in Java

- Example :  

```aidl
rule "A"
whens: String(this == "A")
thenSystem.out.println(s);
end

rule "B" extends "A"
when
  i: Integer(intValue > 2)
then  System.out.println(i);
end
```

#### Conditional named consequences

-   They are basically extra then clauses marked by an identifier to make one rule behave as several. The same identifier has to be used in the rule condition with the go keyword to identify when you should go to that specific consequence

## Understanding KIE Sessions

The sessions are basically split in to two
    -   Stateless
    -   Stateful 
 
 ### Stateless KieSession 
 
 -  This kind of session is equivalent to invoking a method in a Java class.
 
 ```
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
```  

-   All the resources are automatically feed up after the execution of the stateless session
-   Summing up, stateless Kie Sessions are ideal for stateless evaluations such as:
    -   Data validation
    -   Calculations
    -   Data filtering
    -   Message routing

### Stateful KieSession

-   This maintains the session between the invocations of **fireAllRules** method

- Example Code
-   In the below example the session maintained between the two fireRules() invocations.

```
  fun suggestedRoleForApplicant(applicant: Applicant, suggestedRole: SuggestedRole): SuggestedRole {

        kieSession.insert(applicant) // this is a fact to the rule engine
        kieSession.insert(suggestedRole)
        //kieSession.setGlobal("suggestedRole", suggestedRole)
        kieSession.setGlobal("ruleEngineConstant", ruleEngineConstant)
        kieSession.fireAllRules()
        kieSession.insert(suggestedRole)
        kieSession.fireAllRules() // The state that got created in the previous fireAllRules() is still accessible to the rule engine.
        return suggestedRole
    }
```

### Kie runtime components
-   The most common way to interact with the Drools session is insert/modify/retract and executing a rule that may have happened as a consequence of it
-   There are many other ways to interact with it:
    -   globals, 
    -   channels, 
    -   queries, and 
    -   event listeners

#### global

-   A global is, in many cases, a contact point between a session and the external word
-   A global is mainly used to extract information to/from session

##### GLOBALS AS A WAY TO PARAMETERIZE THE CONDITION OF A PATTERN

-   One way globals are normally used in Drools is as a way to externally parameterize the condition of a rule

##### GLOBALS AS A WAY TO INTRODUCE NEW INFORMATION INTO A SESSION IN THE LHS

-   Using the global to retrieve the information from the external world through service calls.

## Things to Do

-   Dynamically set the rule attributes [TODO]
    -   date-effective "01-Apr-2020"
    -   date-expires "31-Apr-2020"
-   Have the same object run through different rules to get the result [DONE]
    -   This is working as expected    