package json.reads

import play.api.libs.json._
import play.api.libs.json.{Reads, __}
import play.api.libs.functional.syntax._

sealed case class JsValueTodoCategory(
    name: String,
    slug: String,
    color: Short
)

object JsValueTodoCategory {
  implicit val reads: Reads[JsValueTodoCategory] = (
    (__ \ "name").read[String] and
      (__ \ "slug").read[String] and
      (__ \ "color").read[Short]
  )(JsValueTodoCategory.apply _)
}
