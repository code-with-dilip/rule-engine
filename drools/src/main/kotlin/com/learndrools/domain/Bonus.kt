package com.learndrools.domain

class Bonus{
    var bonusAmount : Double = 0.0
        get() {
            return field
        }
        set(value){
            field = value
        }
    var pointsMap : Map<String,String> = mutableMapOf()
    set(map) {
        field.plus(map)
    }
    get() = field

}