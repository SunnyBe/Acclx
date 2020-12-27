package com.zistus.aad.data.network

import com.zistus.aad.data.model.Entity
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    /**
     * Get card information by returning a card from the api request for the query entry
     * @param queryEntry The card number to be queried
     */
    @GET("{cardNumber}")
    suspend fun card(
        @Path("cardNumber") queryEntry: String
    ): Entity.Card
}
