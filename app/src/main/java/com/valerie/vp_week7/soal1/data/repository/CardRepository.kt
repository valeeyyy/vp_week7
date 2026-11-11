package com.valerie.vp_week7.soal1.data.repository

import com.valerie.vp_week7.soal1.data.remote.PokemonPriceService
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.util.EmptyResultException
import com.valerie.vp_week7.soal1.util.safeCall
import java.util.concurrent.ConcurrentHashMap

class CardRepository(private val service: PokemonPriceService) {

    private val searchCache = ConcurrentHashMap<String, List<PokemonCard>>()
    private val cardCache = ConcurrentHashMap<String, PokemonCard>()

    suspend fun searchCards(query: String, forceRefresh: Boolean = false): Result<List<PokemonCard>> = safeCall {
        val key = query.trim().lowercase()
        if (!forceRefresh) searchCache[key]?.let { return@safeCall it }

        val cards = service.searchCards(query.trim()).data.map { it.toDomain() }
        if (cards.isEmpty()) throw EmptyResultException("No cards match \"${query.trim()}\".")

        searchCache[key] = cards
        cards.forEach { cardCache[it.id] = it }
        cards
    }

    suspend fun getCard(cardId: String, tcgPlayerId: String): Result<PokemonCard> = safeCall {
        cardCache[cardId]?.let { return@safeCall it }

        val card = service.getCardByTcgPlayerId(tcgPlayerId).data.firstOrNull()?.toDomain()
            ?: throw EmptyResultException("Card details are not available.")
        cardCache[card.id] = card
        card
    }
}
