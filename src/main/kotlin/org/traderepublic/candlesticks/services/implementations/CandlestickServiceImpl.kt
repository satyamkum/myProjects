package org.traderepublic.candlesticks.services.implementations

import java.time.Instant
import java.time.temporal.ChronoUnit
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.repositories.CandlestickRepository
import org.traderepublic.candlesticks.repositories.implementations.CandlestickRepositoryImpl
import org.traderepublic.candlesticks.services.CandlestickService

class CandlestickServiceImpl : CandlestickService {

    private val candlestickRepository: CandlestickRepository = CandlestickRepositoryImpl()

    @Synchronized
    override fun saveOrUpdateCandlestick(event: QuoteEvent) {
        val candlestick = createCandlestick(event)

        val candlesticks = candlestickRepository.getCandlesticksForISIN2(event.data.isin)

        if (candlesticks.isNotEmpty()) {
            val lastUpdatedCandlestick = candlesticks.peekLast()
            if (lastUpdatedCandlestick.openTimestamp == candlestick.openTimestamp) {
                // update candlestick
                updateCandlestick(lastUpdatedCandlestick, candlestick)
            } else {
                val intervals = checkInterval(lastUpdatedCandlestick, candlestick)

                // create missing candlesticks for intervals
                var counter = 1
                while (intervals > counter) {
                    candlestickRepository.saveCandlestick(
                        event.data.isin, lastUpdatedCandlestick.copy(
                            openTimestamp = lastUpdatedCandlestick.openTimestamp.plusSeconds(
                                counter.times(60)
                                    .toLong()
                            ),
                            closeTimestamp = lastUpdatedCandlestick.closeTimestamp.plusSeconds(
                                counter++.times(60)
                                    .toLong()
                            )
                        )
                    )
                }

                candlestickRepository.saveCandlestick(event.data.isin, candlestick)
            }
        } else {
            candlestickRepository.saveCandlestick(event.data.isin, candlestick)
        }
    }

    override fun getCandlesticks(isin: String): List<Candlestick> {
        return candlestickRepository.getCandlesticksForISIN(isin)
    }

    private fun createCandlestick(event: QuoteEvent): Candlestick {

        val price = event.data.price

        return Candlestick(
            Instant.now().truncatedTo(ChronoUnit.MINUTES), // add start of the minute
            Instant.now().truncatedTo(ChronoUnit.MINUTES).plusSeconds(60),
            price,
            price,
            price,
            price
        )

    }

    private fun updateCandlestick(lastUpdatedCandlestick: Candlestick, candlestick: Candlestick) {
        lastUpdatedCandlestick.highPrice = if (lastUpdatedCandlestick.highPrice > candlestick.highPrice)
            lastUpdatedCandlestick.highPrice
        else candlestick.highPrice

        lastUpdatedCandlestick.lowPrice =
            if (lastUpdatedCandlestick.lowPrice < candlestick.lowPrice) lastUpdatedCandlestick.lowPrice
            else candlestick.lowPrice

        lastUpdatedCandlestick.closeTimestamp = candlestick.closeTimestamp
    }

    private fun checkInterval(lastUpdatedCandlestick: Candlestick, candlestick: Candlestick): Long {
        return ChronoUnit.MINUTES.between(lastUpdatedCandlestick.openTimestamp,candlestick.openTimestamp)
    }
}