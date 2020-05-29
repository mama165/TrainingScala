package bankAccount

import java.time._

object Constants {
  val CLOCK: Clock = Clock.fixed(Instant.now, ZoneId.of("Europe/Paris"))
}
