package com.example.giftgame.common.gift

import java.util.Random

object RandomUtil {
    /**
     * Generates the random between two given integers.
     */
    fun generateRandomBetween(start: Int, end: Int): Int {
        val random = Random()
        var rand = random.nextInt(Int.MAX_VALUE - 1) % end
        if (rand < start) {
            rand = start
        }
        return rand
    }
}