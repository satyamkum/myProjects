package org.traderepublic.candlesticks.repositories

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import org.traderepublic.candlesticks.models.Instrument

interface CandlestickRepository {
    fun saveCandlestick(isin: ISIN, candlestick: Candlestick)
}
