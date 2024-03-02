package org.traderepublic.candlesticks.repositories.implementations

import java.util.LinkedList
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.repositories.CandlestickRepository

class CandlestickRepositoryImpl : CandlestickRepository {

    private val candlestickStorage: Map<String, List<Candlestick>> = mutableMapOf()

    private val instrumentStorage: MutableList<Instrument> = mutableListOf()

    override fun saveCandlestick(isin: ISIN, candlestick: Candlestick) {
        candlestickStorage.get()
    }

    override fun saveInstrument(instrument: Instrument) {
        instrumentStorage.add(instrument)
    }
}
