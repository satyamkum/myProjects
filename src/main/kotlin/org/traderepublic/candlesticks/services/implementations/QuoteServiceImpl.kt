package org.traderepublic.candlesticks.services.implementations

import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.services.CandlestickService
import org.traderepublic.candlesticks.services.QuoteService

class QuoteServiceImpl: QuoteService {

    private val candlestickService: CandlestickService = CandlestickServiceImpl()

    private val instrumentService: InstrumentServiceImpl = InstrumentServiceImpl()
    override fun processQuoteEvent(event: QuoteEvent) {
        //isin is valid or not
        //if(!instrumentService.isValidInstrument(event.data.isin))
          //  return

        candlestickService.saveOrUpdateCandlestick(event)
    }

}