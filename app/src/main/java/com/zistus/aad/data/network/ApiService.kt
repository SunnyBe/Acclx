package com.zistus.aad.data.network

import com.zistus.aad.data.model.Entity

interface ApiService {
    /**
     * Get card information by returning a card from the api request for the query entry
     * @param queryEntry The card number to be queried
     */
    suspend fun card(queryEntry: String): Entity.Card
}