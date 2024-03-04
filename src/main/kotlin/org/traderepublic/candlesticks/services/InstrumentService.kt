package org.traderepublic.candlesticks.services

import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.models.InstrumentEvent

interface InstrumentService {
    /**
     * Processes the details as part of org.traderepublic.candlesticks.models.InstrumentEvent
     *
     * @param event - contains details about the Quote
     * @return Unit
     */
    fun processInstrumentEvent(event: InstrumentEvent)

    /**
     * Checks if the isin in input is a valid one
     *
     * @param isin - instrument identifier
     * @return Boolean
     */
    fun isValidInstrument(isin: ISIN): Boolean
}