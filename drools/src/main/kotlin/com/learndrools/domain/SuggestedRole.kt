package com.learndrools.domain

class SuggestedRole() {
    var role: String = ""
        set(value) {
            field = value
        }
        get() {
            return field
        }
    constructor(_role: String) : this() {
        role = _role
    }
}