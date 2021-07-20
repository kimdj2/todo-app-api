package json.writes

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed case class JsValueUser(
  id: Long,
  email: String,
  password: String,
)

object JsValueUser {
  implicit val writes: Writes[JsValueUser] = Json.writes[JsValueUser]
}
