package com.learndrools.domain

data class Employee(
    val name: String,
    val salary: Double,
    val role: String,
    val performance: Double,
    val extraRole: String,
    val hobbies: List<Hobby>
)