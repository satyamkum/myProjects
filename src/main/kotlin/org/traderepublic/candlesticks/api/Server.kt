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
import org.traderepublic.candlesticks.services.CandlestickService
import org.traderepublic.candlesticks.services.implementations.CandlestickServiceImpl

class Server(
    port: Int = 9000,
) {

    // TODO - invoke your implementation here
    private var service: CandlestickService = CandlestickServiceImpl()

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

        val resp = service.getCandlesticks(isin)
        val body = jackson.writeValueAsBytes(resp)

        return Response(Status.OK).body(body.inputStream())
    }
}
