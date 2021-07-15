package json.writes

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed case class JsValueTodo(
    id: Long,
    title: String,
    body: String,
    state: Short,
    category: Option[JsValueTodoCategory]
)

object JsValueTodo {
  implicit val writes: Writes[JsValueTodo] = Json.writes[JsValueTodo]
}
