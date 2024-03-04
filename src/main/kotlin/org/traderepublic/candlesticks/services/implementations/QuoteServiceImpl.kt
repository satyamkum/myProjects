package org.traderepublic.candlesticks.services.implementations

import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.services.CandlestickService
import org.traderepublic.candlesticks.services.InstrumentService
import org.traderepublic.candlesticks.services.QuoteService

class QuoteServiceImpl(
    private val candlestickService: CandlestickService,
    private val instrumentService: InstrumentService
) : QuoteService {

    override fun processQuoteEvent(event: QuoteEvent) {
        /**
         * We are pre-processing incoming quote events and storing candlesticks into storage,
         * so that while retrieving candlesticks information response time will be low.
         */
        if (instrumentService.isValidInstrument(event.data.isin)) {
            candlestickService.saveOrUpdateCandlestick(event)
        }
    }

}