package org.traderepublic.candlesticks.streams

import org.traderepublic.candlesticks.models.InstrumentEvent
import com.fasterxml.jackson.module.kotlin.readValue
import org.traderepublic.candlesticks.jackson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.http4k.client.WebsocketClient
import org.http4k.core.Uri
import org.http4k.websocket.Websocket
import org.slf4j.LoggerFactory

class InstrumentStream(
    uriString: String = "ws://localhost:8032/instruments",
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val uri = Uri.of(uriString)

    private lateinit var ws: Websocket

    fun connect(onEvent: (InstrumentEvent) -> Unit) {
        ws = WebsocketClient.nonBlocking(uri) { logger.info("Connected instrument stream") }

        ws.onMessage {
            val event = jackson.readValue<InstrumentEvent>(it.body.stream)
            onEvent(event)
        }

        ws.onClose {
            logger.info("Disconnected instrument stream: ${it.code}; ${it.description}")
            runBlocking {
                launch {
                    delay(5000L)
                    logger.info("Attempting reconnect for instrument stream")
                    connect(onEvent)
                }
            }
        }

        ws.onError {
            logger.info("Exception in instrument stream: $it")
        }
    }
}
