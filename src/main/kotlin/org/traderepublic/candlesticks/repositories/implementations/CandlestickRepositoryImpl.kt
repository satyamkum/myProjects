package org.traderepublic.candlesticks.repositories.implementations

import java.util.LinkedList
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.repositories.CandlestickRepository

class CandlestickRepositoryImpl : CandlestickRepository {

    private val candlestickStorage: MutableMap<String, LinkedList<Candlestick>> = mutableMapOf()
    override fun saveCandlestick(isin: ISIN, candlestick: Candlestick) {
        val list = if(candlestickStorage[isin] == null) LinkedList<Candlestick>()
                    else candlestickStorage[isin]
        list!!.add(candlestick)
        candlestickStorage[isin] = list
    }

    override fun getCandlesticksForISIN(isin: ISIN): LinkedList<Candlestick> {
        val list = candlestickStorage[isin] ?: LinkedList<Candlestick>()
        return list;
    }

    override fun getCandlesticksForISIN2(isin: ISIN): LinkedList<Candlestick> {
        val list = candlestickStorage[isin] ?: LinkedList<Candlestick>()
        return list;
    }

    override fun updateCandleStick(isin: String,candlestick: Candlestick) {
        // delete old and add new
       val deleteCandlestick = candlestickStorage[isin]?.pop()
        candlestickStorage[isin]?.push(candlestick)
    }

}
