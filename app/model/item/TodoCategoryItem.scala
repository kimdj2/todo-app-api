package model.item

import lib.model.TodoCategory
import lib.model.TodoCategory._

case class TodoCategoryItem(
    id: Long,
    name:  String,
    slug:  String,
    color: TodoCategory.Color
)
object TodoCategoryItem {
  def apply(
      id:  Long,
      name:  String,
      slug:  String,
      color: TodoCategory.Color
  ): TodoCategoryItem = {
    new TodoCategoryItem(id = id, name = name, slug = slug, color = color)
  }
}
