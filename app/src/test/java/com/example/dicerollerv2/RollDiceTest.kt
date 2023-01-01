package com.example.dicerollerv2

import org.junit.Test
import org.junit.Assert.*

class RollDiceTest {
    @Test
    fun testRollDice() {
        val dice = Dice()
        // Roll the dice 100 times and make sure each number appears at least once
        val counts = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0)
        repeat(100) {
            val roll = dice.roll6()
            counts[roll] = counts[roll]!! + 1
        }
        for (i in 1..6) {
            assertTrue(counts[i]!! > 0)
        }
    }

    @Test
    fun testFourSidedDice() {
        val dice = Dice()
        // Roll the dice 100 times and make sure each number appears at least once
        val counts = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0)
        repeat(100) {
            val roll = dice.roll4()
            counts[roll] = counts[roll]!! + 1
        }
        for (i in 1..4) {
            assertTrue(counts[i]!! > 0)
        }
    }

    @Test
    fun testTenSidedDice() {
        val dice = Dice()
        // Roll the dice 100 times and make sure each number appears at least once
        val counts = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0, 8 to 0, 9 to 0, 10 to 0)
        repeat(100) {
            val roll = dice.roll10()
            counts[roll] = counts[roll]!! + 1
        }
        for (i in 1..10) {
            assertTrue(counts[i]!! > 0)
        }
    }

    @Test
    fun testTwentySidedDice() {
        val dice = Dice()
        // Roll the dice 100 times and make sure each number appears at least once
        val counts = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0, 8 to 0, 9 to 0, 10 to 0,
            11 to 0, 12 to 0, 13 to 0, 14 to 0, 15 to 0, 16 to 0, 17 to 0, 18 to 0, 19 to 0, 20 to 0)
        repeat(100) {
            val roll = dice.roll20()
            counts[roll] = counts[roll]!! + 1
        }
        for (i in 1..20) {
            assertTrue(counts[i]!! > 0)
        }
    }
}