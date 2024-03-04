package org.traderepublic.candlesticks.api

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.traderepublic.candlesticks.jackson
import org.traderepublic.candlesticks.repositories.implementations.CandlestickRepositoryImpl
import org.traderepublic.candlesticks.services.CandlestickService
import org.traderepublic.candlesticks.services.implementations.CandlestickServiceImpl

class Server(
    port: Int = 9000,
) {

    private var candlestickService: CandlestickService = CandlestickServiceImpl(CandlestickRepositoryImpl())

    private val routes = routes(
        "candlesticks" bind Method.GET to { getCandlesticks(it) },
    )

    private val server: Http4kServer = routes.asServer(Netty(port))

    fun start() {
        server.start()
    }

    private fun getCandlesticks(req: Request): Response {
        val isin = req.query("isin")
            ?: return Response(Status.BAD_REQUEST).body("{'reason': 'missing_isin'}")

        val candlesticks = candlestickService.getCandlesticks(isin)
        return if (candlesticks.isEmpty()) {
            Response(Status.NOT_FOUND).body("{'reason': 'no_data_for_isin'}")
        } else {
            Response(Status.OK).body( jackson.writeValueAsBytes(candlesticks).inputStream())
        }
    }
}
