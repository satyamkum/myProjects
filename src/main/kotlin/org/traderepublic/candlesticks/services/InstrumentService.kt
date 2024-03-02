package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.InstrumentEvent

interface InstrumentService {
    fun processInstrumentEvent(event : InstrumentEvent)
}