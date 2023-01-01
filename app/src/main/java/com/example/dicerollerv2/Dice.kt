package com.example.dicerollerv2

import java.util.Random

class Dice {
    fun roll6(): Int {
        val random = Random()
        return random.nextInt(6) + 1
    }

    fun roll4(): Int {
        val random = Random()
        return random.nextInt(4) + 1
    }

    fun roll10(): Int {
        val random = Random()
        return random.nextInt(10) + 1
    }

    fun roll20(): Int {
        val random = Random()
        return random.nextInt(20) + 1
    }
}