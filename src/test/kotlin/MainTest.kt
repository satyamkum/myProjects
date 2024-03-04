import io.mockk.every
import io.mockk.mockk
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.traderepublic.candlesticks.models.Quote
import org.traderepublic.candlesticks.models.QuoteEvent
import org.traderepublic.candlesticks.services.QuoteService
import org.traderepublic.candlesticks.services.implementations.QuoteServiceImpl
import kotlin.test.assertEquals

class MainTest {

  private val quoteService: QuoteService = QuoteServiceImpl()
  @Test
  fun `dummy test`() {
    assertEquals(4, 2+2)
  }

  @Test
  fun `dummy two test`() {
    quoteService.processQuoteEvent(QuoteEvent(Quote("X8321062V15",1714.8814)))
    quoteService.processQuoteEvent(QuoteEvent(Quote("X8321062V15",1715.8814)))
    quoteService.processQuoteEvent(QuoteEvent(Quote("X8321062V15",1716.8814)))
    quoteService.processQuoteEvent(QuoteEvent(Quote("X8321062V15",1717.8814)))
    assertEquals(4, 2+2)
  }
  
}
