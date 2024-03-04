package org.traderepublic.candlesticks.services.implementations

import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.models.InstrumentEvent
import org.traderepublic.candlesticks.repositories.InstrumentRepository
import org.traderepublic.candlesticks.services.InstrumentService

class InstrumentServiceImpl(private val instrumentRepository: InstrumentRepository) : InstrumentService {
    override fun processInstrumentEvent(event: InstrumentEvent) {
        when (event.type) {
            InstrumentEvent.Type.ADD -> instrumentRepository.saveInstrument(event.data)
            InstrumentEvent.Type.DELETE -> instrumentRepository.removeInstrument(event.data)
        }
    }

    override fun isValidInstrument(isin: ISIN): Boolean {
        return instrumentRepository.getInstruments().find { instrument: Instrument -> instrument.isin == isin } != null
    }
}
