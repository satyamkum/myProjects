package org.traderepublic.candlesticks

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.traderepublic.candlesticks.api.Server
import org.traderepublic.candlesticks.repositories.implementations.CandlestickRepositoryImpl
import org.traderepublic.candlesticks.repositories.implementations.InstrumentRepositoryImpl
import org.traderepublic.candlesticks.services.implementations.CandlestickServiceImpl
import org.traderepublic.candlesticks.services.implementations.InstrumentServiceImpl
import org.traderepublic.candlesticks.services.implementations.QuoteServiceImpl
import org.traderepublic.candlesticks.streams.InstrumentStream
import org.traderepublic.candlesticks.streams.QuoteStream

private val logger = LoggerFactory.getLogger("MainKt")

fun main() {
    logger.info("starting up")

    val server = Server()
    val instrumentStream = InstrumentStream()
    val candlestickRepository = CandlestickRepositoryImpl()
    val candlestickService = CandlestickServiceImpl(candlestickRepository)
    val quoteStream = QuoteStream()
    val instrumentRepository = InstrumentRepositoryImpl()
    val instrumentService = InstrumentServiceImpl(instrumentRepository)
    val quoteService = QuoteServiceImpl(candlestickService,instrumentService)

    instrumentStream.connect { event ->
        logger.info("Instrument: {}", event)
        instrumentService.processInstrumentEvent(event)
    }

    quoteStream.connect { event ->
        logger.info("Quote: {}", event)
        quoteService.processQuoteEvent(event)
    }

    server.start()
}

val jackson: ObjectMapper =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
