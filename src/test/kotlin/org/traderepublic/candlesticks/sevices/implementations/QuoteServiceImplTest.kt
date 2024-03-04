package org.traderepublic.candlesticks.sevices.implementations

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.traderepublic.candlesticks.models.Quote
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.services.CandlestickService
import org.traderepublic.candlesticks.services.InstrumentService
import org.traderepublic.candlesticks.services.implementations.QuoteServiceImpl

@ExtendWith(MockKExtension::class)
class QuoteServiceImplTest {
    @Test
    fun `Should process incoming quote event for valid isin`(@MockK mockInstrumentService: InstrumentService,
                                       @MockK mockCandlestickService: CandlestickService){
        every { mockInstrumentService.isValidInstrument(any()) } returns true
        justRun { mockCandlestickService.saveOrUpdateCandlestick(any()) }

        val mockQuoteService = QuoteServiceImpl(mockCandlestickService, mockInstrumentService )
        val event = QuoteEvent(Quote("XYZ",90.00))
        mockQuoteService.processQuoteEvent(event)

        verify { mockInstrumentService.isValidInstrument("XYZ") }
        verify { mockCandlestickService.saveOrUpdateCandlestick(event) }
    }

    @Test
    fun `Should not process incoming quote event for invalid isin`(@MockK mockInstrumentService: InstrumentService,
                                                      @MockK mockCandlestickService: CandlestickService){
        every { mockInstrumentService.isValidInstrument(any()) } returns false

        val mockQuoteService = QuoteServiceImpl(mockCandlestickService, mockInstrumentService )
        val event = QuoteEvent(Quote("XYZ",90.00))
        mockQuoteService.processQuoteEvent(event)

        verify { mockInstrumentService.isValidInstrument("XYZ") }
        verify(exactly = 0) { mockCandlestickService.saveOrUpdateCandlestick(event) }
    }
}