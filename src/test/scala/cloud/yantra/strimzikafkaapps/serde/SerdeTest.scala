package cloud.yantra.strimzikafkaapps.serde

import cats.Eq
import cloud.yantra.strimzikafkaapps.models.{
  BankAccount,
  Id,
  Money,
  Transaction,
  TransactionType
}
import io.circe.testing.{ArbitraryInstances, CodecTests}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.forAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.time.ZonedDateTime
import java.util.Currency

class SerdeTest extends AnyFlatSpec with should.Matchers {
  object Implicits extends ArbitraryInstances {
    implicit val eqTransaction: Eq[Transaction] = Eq.fromUniversalEquals
    implicit val arpTransaction: Arbitrary[Transaction] = Arbitrary {
      for {
        id <- Gen.alphaNumStr.map(Id[Transaction](_))
        amount <- Gen.long
        accNumber <- Gen.alphaNumStr
        accName <- Gen.alphaNumStr
        trnType <- Gen.oneOf(TransactionType.Credit, TransactionType.Debit)
      } yield Transaction(
        id = id,
        value = Money(amount, Currency.getInstance("EUR")),
        `type` = TransactionType.Debit,
        time = ZonedDateTime.now(),
        account = BankAccount(number = accNumber, name = accName)
      )
    }
  }

  "A Serde for Transaction" should "deserialized correctly" in {
    import Implicits._
    val transactionCodecTests = CodecTests[Transaction]
    transactionCodecTests.codec
  }
}
