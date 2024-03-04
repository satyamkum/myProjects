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

        //If list size is equal to configured size then remove old entry
        if(list!!.size == CANDLESTICK_LIST_SIZE) {
            list.removeFirst()
        }
        list.add(candlestick)
        candlestickStorage[isin] = list
    }

    override fun getCandlesticksForISIN(isin: ISIN): LinkedList<Candlestick> {
        return candlestickStorage[isin] ?: LinkedList()
    }

    companion object {
        private lateinit var candlestickStorage: ConcurrentHashMap<String, LinkedList<Candlestick>>
        //TODO Derive this value from config
        private const val CANDLESTICK_LIST_SIZE: Int = 30

        @JvmStatic
        fun getCandlestickStorage(): MutableMap<String, LinkedList<Candlestick>> {
            return candlestickStorage;
        }
    }

    init {
        candlestickStorage = ConcurrentHashMap()
    }

}
