package org.traderepublic.candlesticks.services.implementations

import java.time.Instant
import java.time.temporal.ChronoUnit
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.repositories.CandlestickRepository
import org.traderepublic.candlesticks.services.CandlestickService
import java.util.LinkedList

class CandlestickServiceImpl(private val candlestickRepository: CandlestickRepository) : CandlestickService {
    override fun saveOrUpdateCandlestick(event: QuoteEvent) {

        val candlestick = createCandlestick(event)

        val candlesticks = candlestickRepository.getCandlesticksForISIN(event.data.isin)

        if (candlesticks.isNotEmpty()) {
            val lastUpdatedCandlestick = candlesticks.peekLast()
            /**
             * If the quote price event belongs to the same time interval as last stored candlestick
             * then update same candlestick entry.
             */
            if (lastUpdatedCandlestick.openTimestamp == candlestick.openTimestamp) {
                updateCandlestick(lastUpdatedCandlestick, candlestick)
            } else {
                /**
                 * If no events received for some time intervals then copy last candlestick record.
                 */
                val intervals = checkInterval(lastUpdatedCandlestick.openTimestamp, candlestick.openTimestamp)
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

        //TODO: Add validation to input isin is present in valid instrument list and return proper response message

        val candlesticks = candlestickRepository.getCandlesticksForISIN(isin)

        if (candlesticks.isNotEmpty()) {
            val lastUpdatedCandlestick = candlesticks.peekLast()
            val currentTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES)

            /**
             *  Here we have preprocessed and stored candlesticks for all instruments in in-memory storage
             *  (can be replaced with data storage)
             *  Assumption: We are returning history of last 30 minutes including current minute.
             */
            if (lastUpdatedCandlestick.openTimestamp == currentTimestamp)
                return candlesticks
            else {
                /**
                 * Assumption: There can be a get call for isin which price is not updated since few minutes.
                 * We are copying last updated candlestick data for that time interval.
                 */
                val intervals = checkInterval(lastUpdatedCandlestick.openTimestamp, currentTimestamp)
                var counter = 1
                val tempCandlesticks = LinkedList<Candlestick>()
                // Copy last entry for missing time intervals by updating openTimestamp & closeTimestamp
                while (intervals > counter) {
                    tempCandlesticks.add(
                        lastUpdatedCandlestick.copy(
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
                val result = LinkedList<Candlestick>()
                if (tempCandlesticks.isNotEmpty() && candlesticks.size - tempCandlesticks.size > 0) {
                    result.addAll(candlesticks.subList(tempCandlesticks.size - 1, candlesticks.size))
                }
                result.addAll(tempCandlesticks)
                return result
            }
        }
        return emptyList()
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

        lastUpdatedCandlestick.closingPrice = candlestick.closingPrice
        lastUpdatedCandlestick.closeTimestamp = candlestick.closeTimestamp
    }

    private fun checkInterval(lastUpdatedCandlestickTimestamp: Instant, candlestickTimestamp: Instant): Long {
        return ChronoUnit.MINUTES.between(lastUpdatedCandlestickTimestamp, candlestickTimestamp)
    }
}