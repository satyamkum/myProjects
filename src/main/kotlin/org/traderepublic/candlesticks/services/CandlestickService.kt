package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.QuoteEvent

interface CandlestickService {

    fun saveOrUpdateCandlestick(event: QuoteEvent)

    fun getCandlesticks(isin: String): List<Candlestick>
}