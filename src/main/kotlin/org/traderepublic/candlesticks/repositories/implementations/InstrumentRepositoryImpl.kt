package org.traderepublic.candlesticks.repositories.implementations

import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.repositories.InstrumentRepository

class InstrumentRepositoryImpl : InstrumentRepository {

    private val instrumentStorage: MutableList<Instrument> = mutableListOf()
    override fun saveInstrument(instrument: Instrument) {
        instrumentStorage.add(instrument)
    }

    override fun removeInstrument(instrument: Instrument) {
        instrumentStorage.remove(instrument)
    }

    override fun getInstruments(): List<Instrument> {
        return this.instrumentStorage
    }
}