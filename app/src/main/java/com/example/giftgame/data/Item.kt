package com.example.giftgame.data

sealed class Item {
    abstract var resource: Int

    data class Gift(override var resource: Int, var point: Int) : Item()

    data class BOOM(override var resource: Int, var point: Int) : Item()

    data class FlashTime(override var resource: Int) : Item()
}