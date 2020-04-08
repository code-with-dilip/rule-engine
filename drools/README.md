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

### KieSession
-   Represents the running instance of rule engine with specific configuration and set of rules.