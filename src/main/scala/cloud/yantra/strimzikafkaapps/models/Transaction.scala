package cloud.yantra.strimzikafkaapps.models

import java.time.ZonedDateTime
import java.util.Currency

case class Id[Resource](value: String)
case class Money(amount: Long, currency: Currency)
case class BankAccount(number: String, name: String)

enum TransactionType:
  case Credit, Debit

case class Transaction(
    id: Id[Transaction],
    value: Money,
    `type`: TransactionType,
    time: ZonedDateTime,
    account: BankAccount
)

case class CustomerMessage(
    id: Id[CustomerMessage],
    account: BankAccount,
    body: String,
    time: ZonedDateTime
)
