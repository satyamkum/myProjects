package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.models.InstrumentEvent

interface InstrumentService {
    fun processInstrumentEvent(event : InstrumentEvent)
    fun isValidInstrument(isin: ISIN): Boolean
}