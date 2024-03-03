package org.traderepublic.candlesticks.services.implementations

import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.repositories.CandlestickRepository
import org.traderepublic.candlesticks.repositories.implementations.CandlestickRepositoryImpl
import org.traderepublic.candlesticks.services.CandlestickService
import java.time.Instant
import java.time.temporal.ChronoUnit

class CandlestickServiceImpl: CandlestickService {

    private val candlestickRepository: CandlestickRepository = CandlestickRepositoryImpl()
    override fun saveOrUpdateCandlestick(event: QuoteEvent) {
        val candlestick = createCandlestick(event)

        val candlesticks = candlestickRepository.getCandlesticksForISIN2(event.data.isin)

        if (candlesticks.isNotEmpty()) {
            val lastUpdatedCandlestick = candlesticks.peek()
            if(lastUpdatedCandlestick.openTimestamp == candlestick.openTimestamp){
                // update candlestick
                updateCandlestick(lastUpdatedCandlestick, candlestick, event.data.isin)
            }else{
                val intervals = checkInterval(lastUpdatedCandlestick, candlestick)
                if(intervals > 1){
                    // create missing candlesticks for intervals
                    while(intervals > 1){
                        candlestickRepository.saveCandlestick(event.data.isin,lastUpdatedCandlestick.copy())
                    }
                }
                candlestickRepository.saveCandlestick(event.data.isin,candlestick)
            }
        }
        else{
            candlestickRepository.saveCandlestick(event.data.isin, candlestick)
        }
    }

    override fun getCandlesticks(isin: String): List<Candlestick> {
        return candlestickRepository.getCandlesticksForISIN(isin)
    }

    private fun createCandlestick(event: QuoteEvent): Candlestick{

        val price = event.data.price

        return Candlestick(
            Instant.now().truncatedTo(ChronoUnit.MINUTES), // add start of the minute
            Instant.now().truncatedTo(ChronoUnit.MINUTES).plusSeconds(60),
            price,
            price,
            price,
            price)

    }

    private fun updateCandlestick(lastUpdatedCandlestick: Candlestick, candlestick: Candlestick, isin: String){
        val highPrice = if(lastUpdatedCandlestick.highPrice > candlestick.highPrice) lastUpdatedCandlestick.highPrice
        else candlestick.highPrice

        val lowPrice = if(lastUpdatedCandlestick.lowPrice < candlestick.lowPrice) lastUpdatedCandlestick.lowPrice
        else candlestick.lowPrice

        val newCandlestick = Candlestick(candlestick.openTimestamp,
            candlestick.closeTimestamp,candlestick.openPrice,
            highPrice,
            lowPrice,
            candlestick.closingPrice
        )

        candlestickRepository.updateCandleStick(isin, newCandlestick)
    }

    private fun checkInterval(lastUpdatedCandlestick: Candlestick, candlestick: Candlestick): Long{
        return ChronoUnit.MINUTES.between(candlestick.openTimestamp,lastUpdatedCandlestick.openTimestamp)
    }
}