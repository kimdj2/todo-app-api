package json.writes

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed case class JsValueTodoCategory(
    id: Long,
    name:  String,
    slug:  String,
    color: Short
)

object JsValueTodoCategory {
  implicit val writes: Writes[JsValueTodoCategory] = Json.writes[JsValueTodoCategory]
}
