package org.traderepublic.candlesticks.sevices.implementations

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.traderepublic.candlesticks.models.Instrument
import org.traderepublic.candlesticks.models.InstrumentEvent
import org.traderepublic.candlesticks.repositories.InstrumentRepository
import org.traderepublic.candlesticks.services.InstrumentService
import org.traderepublic.candlesticks.services.implementations.InstrumentServiceImpl
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class InstrumentServiceImplTest {

    @Test
    fun `Should save instrument when ADD event is received`(@MockK mockInstrumentRepository: InstrumentRepository) {
        justRun { mockInstrumentRepository.saveInstrument(any()) }
        val instrumentService = InstrumentServiceImpl(mockInstrumentRepository)
        val event = InstrumentEvent(
            InstrumentEvent.Type.ADD,
            Instrument("XYZ", "new instrument")
        )
        instrumentService.processInstrumentEvent(event)

        verify { mockInstrumentRepository.saveInstrument(event.data) }
    }

    @Test
    fun `Should remove instrument when DELETE event is received`(@MockK mockInstrumentRepository: InstrumentRepository) {
        justRun { mockInstrumentRepository.removeInstrument(any()) }

        val instrumentService = InstrumentServiceImpl(mockInstrumentRepository)
        val event = InstrumentEvent(
            type = InstrumentEvent.Type.DELETE,
            data = Instrument("XYZ", "old instrument")
        )
        instrumentService.processInstrumentEvent(event)

        verify { mockInstrumentRepository.removeInstrument(event.data) }
    }
}