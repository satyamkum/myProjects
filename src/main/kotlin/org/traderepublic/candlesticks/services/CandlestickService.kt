package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.QuoteEvent
interface CandlestickService {

    /**
     * Processes the details as part of org.traderepublic.candlesticks.models.QuoteEvent
     *
     * @param event - contains details about the Quote
     * @return Unit
     */
    fun saveOrUpdateCandlestick(event: QuoteEvent)

    /**
     * Gets the candlesticks for the input isin for last 30 minutes
     *
     * @param isin - the instrument identifier
     * @return List<org.traderepublic.candlesticks.models.Candlestick>
     */
    fun getCandlesticks(isin: String): List<Candlestick>
}