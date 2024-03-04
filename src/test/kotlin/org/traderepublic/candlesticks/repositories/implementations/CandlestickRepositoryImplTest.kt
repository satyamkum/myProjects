package org.traderepublic.candlesticks.repositories.implementations

import org.junit.jupiter.api.Test
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.Quote
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.services.implementations.CandlestickServiceImpl
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertTrue

class CandlestickRepositoryImplTest {
    @Test
    fun `Should save the candlestick for quote event`() {
        val candlestickRepository = CandlestickRepositoryImpl()
        // val candlestickService = CandlestickServiceImpl(mockCandlestickRepository)

        val candlestick = Candlestick(
            openTimestamp = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
            closeTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
            openPrice = 243.5856,
            lowPrice = 235.1712,
            highPrice = 243.9369,
            closingPrice = 235.1712
        )
        candlestickRepository.saveCandlestick("XYZ", candlestick)
        assertTrue { candlestickRepository.getCandlesticksForISIN("XYZ").isNotEmpty() }
    }

    @Test
    fun `Should return the candlesticks for isin`() {
        val candlestickRepository = CandlestickRepositoryImpl()
        val candlestick1 = Candlestick(
            openTimestamp = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
            closeTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
            openPrice = 243.5856,
            lowPrice = 235.1712,
            highPrice = 243.9369,
            closingPrice = 235.1712
        )
        val candlestick2 = Candlestick(
            openTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
            closeTimestamp = Instant.now().plusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
            openPrice = 243.585,
            lowPrice = 222.222,
            highPrice = 243.9369,
            closingPrice = 222.1122
        )

        candlestickRepository.saveCandlestick("XYZ", candlestick1)
        candlestickRepository.saveCandlestick("XYZ", candlestick2)
        assertTrue { candlestickRepository.getCandlesticksForISIN("XYZ").size == 2}
    }
}
