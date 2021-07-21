package json.reads

import play.api.libs.json._
import play.api.libs.json.{Reads, __}
import play.api.libs.functional.syntax._

sealed case class JsValueUser(
    email: String,
    password: String,
)

object JsValueUser {
  implicit val reads: Reads[JsValueUser] = (
    (__ \ "email").read[String] and
      (__ \ "password").read[String]
  )(JsValueUser.apply _)
}
