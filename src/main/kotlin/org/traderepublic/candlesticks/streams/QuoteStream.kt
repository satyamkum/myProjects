package org.traderepublic.candlesticks.streams

import org.traderepublic.candlesticks.models.QuoteEvent
import com.fasterxml.jackson.module.kotlin.readValue
import org.traderepublic.candlesticks.jackson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.http4k.client.WebsocketClient
import org.http4k.core.Uri
import org.http4k.websocket.Websocket
import org.slf4j.LoggerFactory

class QuoteStream(
    uriString: String = "ws://localhost:8032/quotes",
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val wsURI = Uri.of(uriString)

    private lateinit var ws: Websocket

    fun connect(onEvent: (QuoteEvent) -> Unit) {
        ws = WebsocketClient.nonBlocking(wsURI) { logger.info("Connected quote stream") }

        ws.onMessage {
            val event = jackson.readValue<QuoteEvent>(it.body.stream)
            onEvent(event)
        }

        ws.onClose {
            logger.info("Disconnected quote stream: ${it.code}; ${it.description}")
            runBlocking {
                launch {
                    delay(5000L)
                    logger.info("Attempting reconnect for quote stream")
                    connect(onEvent)
                }
            }
        }

        ws.onError {
            logger.info("Exception in quote stream: $it")
        }
    }
}
