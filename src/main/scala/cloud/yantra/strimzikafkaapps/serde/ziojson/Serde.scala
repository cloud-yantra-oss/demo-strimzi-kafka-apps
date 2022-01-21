//import zio.json.{JsonDecoder, JsonError}
//import zio.json.internal.RetractReader
//
//import zio.*
//
//import java.util.Currency

//implicit val currencyDecoder: JsonDecoder[Currency] =
//  new JsonDecoder[Currency] {
//    override def unsafeDecode(
//        trace: List[JsonError],
//        in: RetractReader
//    ): Currency = ???
//  }

//  (c: HCursor) => {
//  for {
//    code <- c.downField("code").as[String]
//  } yield Currency.getInstance(code)
//}
