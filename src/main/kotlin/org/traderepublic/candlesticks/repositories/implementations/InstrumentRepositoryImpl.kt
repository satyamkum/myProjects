package org.traderepublic.candlesticks.repositories.implementations

import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.repositories.InstrumentRepository

class InstrumentRepositoryImpl : InstrumentRepository {

    override fun saveInstrument(instrument: Instrument) {
        instrumentStorage.add(instrument)
    }

    override fun removeInstrument(instrument: Instrument) {
        instrumentStorage.remove(instrument)

    }

    override fun getInstruments(): List<Instrument> {
        return instrumentStorage
    }

    companion object {
        private lateinit var instrumentStorage: MutableList<Instrument>

        @JvmStatic
        fun getInstrumentStorage(): MutableList<Instrument> {
            return instrumentStorage;
        }
    }

    init {
        instrumentStorage = mutableListOf()
    }
}