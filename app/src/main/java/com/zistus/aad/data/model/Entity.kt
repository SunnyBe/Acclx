package com.zistus.aad.data.model

sealed class Entity {

    data class Card(
        val scheme: String? = null,
        val type: String? = null,
        val brand: String? = null,
        val prepaid: Boolean = false,
        val bank: Bank? = null,
        val country: Country? = null,
        val number: CardNumber? = null
    ) : Entity()

    data class Bank(
        val name: String? = null,
        val url: String? = null,
        val phone: String? = null,
        val city: String? = null,
    ) : Entity()

    data class Country(
        val numeric: String? = null,
        val alpha: String? = null,
        val name: String? = null,
        val emoji: String? = null,
        val currency: String? = null,
        val latitude: Int? = null,
        val longitude: Int? = null,
    ) : Entity()

    data class CardNumber(
        val length: Int? = null,
        val luhn: Boolean = true
    ) : Entity()
}
