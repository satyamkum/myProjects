package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.QuoteEvent
interface QuoteService {
    /**
     * Processes the details as part of org.traderepublic.candlesticks.models.QuoteEvent
     *
     * @param event - contains details about the Quote
     * @return Unit
     */
    fun processQuoteEvent(event: QuoteEvent)
}