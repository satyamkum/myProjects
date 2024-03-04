package org.traderepublic.candlesticks.repositories

import org.traderepublic.candlesticks.models.Instrument

interface InstrumentRepository {

    /**
     * Saves the instrument in storage
     *
     * @param org.traderepublic.candlesticks.models.Instrument
     * return Unit
     */
    fun saveInstrument(instrument: Instrument)

    /**
     * Removes the instrument from the storage
     *
     * @param org.traderepublic.candlesticks.models.Instrument
     * @return Unit
     */
    fun removeInstrument(instrument: Instrument)it

    /**
     * fetches the instrument list
     *
     * @return List<org.traderepublic.candlesticks.models.Instrument>
     */
    fun getInstruments(): List<Instrument>
}
