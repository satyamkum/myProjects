package org.traderepublic.candlesticks.repositories.implementations

import org.junit.jupiter.api.Test
import org.traderepublic.candlesticks.models.Instrument
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InstrumentRepositoryImplTest {

    @Test
    fun `Should save the instrument`() {

        val instrumentRepository = InstrumentRepositoryImpl()
        val instrument = Instrument("XYZ", "new instrument")
        instrumentRepository.saveInstrument(instrument)

        assertTrue {  instrumentRepository.getInstruments().contains(instrument)}
    }

    @Test
    fun `Should remove the instrument`() {
        val instrumentRepository = InstrumentRepositoryImpl()
        val instrument = Instrument("XYZ", "new instrument")
        instrumentRepository.removeInstrument(instrument)

        assertFalse {  instrumentRepository.getInstruments().contains(instrument)}

    }

    @Test
    fun `Should return the instrument`() {
        val instrumentRepository = InstrumentRepositoryImpl()
        val instrument = Instrument("XYZ", "new instrument")
        instrumentRepository.saveInstrument(instrument)

        assertTrue {  instrumentRepository.getInstruments().isNotEmpty()}
    }
}