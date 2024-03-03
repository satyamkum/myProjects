package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.QuoteEvent

interface QuoteService {

    fun processQuoteEvent(event: QuoteEvent)
}