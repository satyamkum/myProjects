package org.traderepublic.candlesticks.sevices.implementations

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.traderepublic.candlesticks.models.Candlestick
import org.traderepublic.candlesticks.models.Quote
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.repositories.CandlestickRepository
import org.traderepublic.candlesticks.repositories.implementations.CandlestickRepositoryImpl
import org.traderepublic.candlesticks.services.implementations.CandlestickServiceImpl
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class CandlestickServiceImplTest {

    @Test
    fun `Should save the candlestick for quote event`( ) {
        val mockCandlestickRepository = CandlestickRepositoryImpl()
        val candlestickService = CandlestickServiceImpl(mockCandlestickRepository)
        val event = QuoteEvent(Quote("XYZ",90.00))
        candlestickService.saveOrUpdateCandlestick(event)
        assertTrue { candlestickService.getCandlesticks("XYZ").isNotEmpty()}
    }
    @Test
    fun `Should return candlesticks properly in the response`(@MockK mockCandlestickRepository: CandlestickRepository) {
        val candlestickService = CandlestickServiceImpl(mockCandlestickRepository)
        val mockCandlesticks = LinkedList<Candlestick>()
        mockCandlesticks.add(
            Candlestick(
                openTimestamp = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
                closeTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
                openPrice = 243.5856,
                lowPrice = 235.1712,
                highPrice = 243.9369,
                closingPrice = 235.1712
            )
        )
        mockCandlesticks.add(
            Candlestick(
                openTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
                closeTimestamp = Instant.now().plusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
                openPrice = 243.585,
                lowPrice = 222.222,
                highPrice = 243.9369,
                closingPrice = 222.1122
            )
        )
        every { mockCandlestickRepository.getCandlesticksForISIN(any()) } returns mockCandlesticks

        assertEquals(
            mockCandlesticks,
            candlestickService.getCandlesticks("XYZ")
        )
    }

    @Test
    fun `Should return candlesticks properly in the response for missing intervals`(@MockK mockCandlestickRepository: CandlestickRepository) {
        val candlestickService = CandlestickServiceImpl(mockCandlestickRepository)
        val mockCandlesticks = LinkedList<Candlestick>()
        mockCandlesticks.add(
            Candlestick(
                openTimestamp = Instant.now().minusSeconds(120).truncatedTo(ChronoUnit.MINUTES),
                closeTimestamp = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
                openPrice = 243.5856,
                lowPrice = 235.1712,
                highPrice = 243.9369,
                closingPrice = 235.1712
            )
        )
        mockCandlesticks.add(
            Candlestick(
                openTimestamp = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
                closeTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
                openPrice = 243.585,
                lowPrice = 222.222,
                highPrice = 243.9369,
                closingPrice = 222.1122
            )
        )
        every { mockCandlestickRepository.getCandlesticksForISIN(any()) } returns mockCandlesticks

        mockCandlesticks.add(
            Candlestick(
                openTimestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES),
                closeTimestamp = Instant.now().plusSeconds(60).truncatedTo(ChronoUnit.MINUTES),
                openPrice = 243.585,
                lowPrice = 222.222,
                highPrice = 243.9369,
                closingPrice = 222.1122
            )
        )
        assertEquals(
            mockCandlesticks,
            candlestickService.getCandlesticks("XYZ")
        )
    }

    @Test
    fun `Should return empty list in the response for isin which is invalid`(@MockK mockCandlestickRepository: CandlestickRepository) {
        val candlestickService = CandlestickServiceImpl(mockCandlestickRepository)
        every { mockCandlestickRepository.getCandlesticksForISIN(any()) } returns LinkedList<Candlestick>()
        assertTrue(
            candlestickService.getCandlesticks("XYZ").isEmpty()
        )
    }
}