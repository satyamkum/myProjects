package org.traderepublic.candlesticks.repositories

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.ISIN
import java.util.LinkedList

interface CandlestickRepository {

    /**
     * Saves the candlestick for a particular isin
     *
     * @param isin - instrument identifier
     * @param candlestick - details about candlestick
     * @return Unit
     */
    fun saveCandlestick(isin: ISIN, candlestick: Candlestick)

    /**
     * Fetch the last 30 minutes candlestick for the isin in input
     *
     * @param isin - instrument identifier
     * @return List<org.traderepublic.candlesticks.models.Candlestick>
     */
    fun getCandlesticksForISIN(isin: ISIN): LinkedList<Candlestick>
}
