package com.zistus.aad.utils

import com.zistus.aad.data.model.Entity
import com.zistus.aad.data.network.ApiService
import kotlinx.coroutines.CompletableDeferred

data class NetworkFakes(
    val test: String? = null,
    val card: Entity.Card? = null
) {
    companion object {
        fun testCardQuery(testNumber: String?) = testCard()

        private fun testCard(testNumber: String? = "012345678912345") = Entity.Card(
            scheme = "visa",
            type = "debit",
            brand = "Visa/Dankort",
            prepaid = false,
            bank = Entity.Bank(
                name = "Jske Bank",
                url = "www.jyskebank.dk",
                phone = "+4589893300",
                city = "Hjørring"
            ),
            country = Entity.Country(
                numeric = "208",
                alpha = "DK",
                name = "Denmark",
                emoji = "\uD83C\uDDE9\uD83C\uDDF0",
                currency = "DKK",
                latitude = 56,
                longitude = 10
            ),
            number = Entity.CardNumber(
                length = testNumber?.length,
                luhn = true
            )
        )
    }
}

/**
 * Testing all network request with fake responses.
 * This helps with passing the test data from the "consuming" test class
 */
class NetworkServiceFake() : ApiService {
    override suspend fun card(
        queryEntry: String
    ): Entity.Card = NetworkFakes.testCardQuery(queryEntry)
}

/**
 * Testing Network requests that lets you complete or error all current requests
 */
class NetworkServiceCompletableFake() : ApiService {
    override suspend fun card(queryEntry: String): Entity.Card =
        CompletableDeferred<Entity.Card>().await()
}
