package org.traderepublic.candlesticks.repositories

import org.traderepublic.candlesticks.models.Instrument

interface InstrumentRepository {

    fun saveInstrument(instrument: Instrument)

    fun deleteInstrument(instrument: Instrument)
}