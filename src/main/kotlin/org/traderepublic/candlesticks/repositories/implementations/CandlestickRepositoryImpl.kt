package org.traderepublic.candlesticks.repositories.implementations

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.repositories.CandlestickRepository

class CandlestickRepositoryImpl : CandlestickRepository {

    override fun saveCandlestick(isin: ISIN, candlestick: Candlestick) {
        val list = if (!candlestickStorage.containsKey(isin)) LinkedList<Candlestick>()
        else candlestickStorage[isin]

        if(list?.size!! >= 30) {
            list.removeFirst()
        }
        list.add(candlestick)
        candlestickStorage[isin] = list
    }

    override fun getCandlesticksForISIN(isin: ISIN): LinkedList<Candlestick> {
        return candlestickStorage[isin] ?: LinkedList();
    }

    override fun getCandlesticksForISIN2(isin: ISIN): LinkedList<Candlestick> {
        return candlestickStorage[isin] ?: LinkedList();
    }

    override fun updateCandleStick(isin: String, candlestick: Candlestick) {
        // delete old and add new
        val entry = candlestickStorage[isin]?.removeLast()
        candlestickStorage[isin]?.push(candlestick)
    }

    companion object {
        private lateinit var candlestickStorage: ConcurrentHashMap<String, LinkedList<Candlestick>>

        @JvmStatic
        fun getCandlestickStorage(): MutableMap<String, LinkedList<Candlestick>> {
            return candlestickStorage;
        }
    }

    init {
        candlestickStorage = ConcurrentHashMap()
    }

}
