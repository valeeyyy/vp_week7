package com.valerie.vp_week7.soal1.data.repository

import com.valerie.vp_week7.soal1.data.dto.CardDto
import com.valerie.vp_week7.soal1.model.PokemonCard

private fun String?.clean(): String? = this?.trim()?.takeIf { it.isNotEmpty() && it != "null" }

fun CardDto.toDomain() = PokemonCard(
    id = id,
    tcgPlayerId = tcgPlayerId.orEmpty(),
    name = name,
    setName = setName.clean() ?: "Unknown Set",
    cardNumber = cardNumber.clean() ?: "-",
    rarity = rarity.clean() ?: "-",
    hp = hp.clean(),
    type = pokemonType.clean() ?: energyType?.firstOrNull().clean() ?: "-",
    artist = artist.clean() ?: "-",
    imageUrl = imageCdnUrl400.clean() ?: imageUrl.clean(),
    imageLargeUrl = imageCdnUrl800.clean() ?: imageCdnUrl400.clean() ?: imageUrl.clean(),
    marketPrice = prices?.market,
    lowPrice = prices?.low,
    listings = prices?.listings,
    sellers = prices?.sellers,
    primaryPrinting = prices?.primaryPrinting.clean() ?: "-",
)
