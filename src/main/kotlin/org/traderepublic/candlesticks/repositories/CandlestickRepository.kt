package org.traderepublic.candlesticks.repositories

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument
import java.util.LinkedList

interface CandlestickRepository {
    fun saveCandlestick(isin: ISIN, candlestick: Candlestick)

    fun getCandlesticksForISIN(isin: ISIN): LinkedList<Candlestick>

    fun getCandlesticksForISIN2(isin: ISIN): LinkedList<Candlestick>

    fun updateCandleStick(isin: String, candlestick: Candlestick)
}
