package cloud.yantra.strimzikafkaapps.serde.circe

import cloud.yantra.strimzikafkaapps.models.{
  BankAccount,
  Id,
  Money,
  Transaction,
  TransactionType
}
import io.circe.{CursorOp, Decoder, DecodingFailure, Encoder, HCursor, Json}
import io.circe.Decoder.Result

import java.time.ZonedDateTime
import java.util.Currency

implicit val currencyEncoder: Encoder[Currency] = (c: Currency) =>
  Json.obj(
    ("code", Json.fromString(c.getCurrencyCode))
  )

implicit val currencyDecoder: Decoder[Currency] = (c: HCursor) => {
  for {
    code <- c.downField("code").as[String]
  } yield Currency.getInstance(code)
}

implicit val moneyEncoder: Encoder[Money] = (m: Money) =>
  Json.obj(
    ("amount", Json.fromLong(m.amount)),
    ("currency", currencyEncoder.apply(m.currency))
  )

implicit val moneyDecoder: Decoder[Money] = (c: HCursor) => {
  for {
    amount <- c.downField("amount").as[Long]
    currency <- c.downField("currency").as[Currency]
  } yield Money(amount, currency)
}

implicit val bankAccountEncoder: Encoder[BankAccount] = (b: BankAccount) => {
  Json.obj(
    ("number", Json.fromString(b.number)),
    ("name", Json.fromString(b.name))
  )
}

implicit val bankAccountDecoder: Decoder[BankAccount] = (c: HCursor) => {
  for {
    number <- c.downField("number").as[String]
    name <- c.downField("name").as[String]
  } yield BankAccount(number, name)
}

implicit val transactionTypeEncoder: Encoder[TransactionType] =
  (a: TransactionType) =>
    a match {
      case TransactionType.Credit => Json.fromString("CR")
      case TransactionType.Debit  => Json.fromString("DR")
    }

implicit val transactionTypeDecoder: Decoder[TransactionType] =
  (c: HCursor) =>
    c.as[String].flatMap { str =>
      str.toUpperCase match {
        case "CR" => Right(TransactionType.Credit)
        case "DR" => Right(TransactionType.Debit)
        case _ =>
          Left(
            DecodingFailure(
              "Invalid transaction type provided",
              List.empty[CursorOp]
            )
          )
      }
    }

implicit val transactionEncoder: Encoder[Transaction] = (t: Transaction) =>
  Json.obj(
    ("id", Json.fromString(t.id.value)),
    ("value", moneyEncoder.apply(t.value)),
    ("type", transactionTypeEncoder.apply(t.`type`)),
    ("time", Encoder.encodeZonedDateTime.apply(t.time)),
    ("account", bankAccountEncoder.apply(t.account))
  )

implicit val transactionDecoder: Decoder[Transaction] = (c: HCursor) => {
  for {
    id <- c.downField("id").as[String]
    value <- c.downField("value").as[Money]
    `type` <- c.downField("type").as[TransactionType]
    time <- c.downField("time").as[ZonedDateTime]
    account <- c.downField("accounts").as[BankAccount]
  } yield Transaction(Id[Transaction](id), value, `type`, time, account)
}
