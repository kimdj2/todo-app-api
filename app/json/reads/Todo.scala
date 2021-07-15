package json.reads

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed case class JsValueTodo(
    title: String,
    body: String,
    state: Short,
    categoryId: Long
)

object JsValueTodo {
  implicit val reads: Reads[JsValueTodo] = (
    (__ \ "title").read[String] and
      (__ \ "body").read[String] and
      (__ \ "state").read[Short] and
      (__ \ "categoryId").read[Long]
  )(JsValueTodo.apply _)
}
