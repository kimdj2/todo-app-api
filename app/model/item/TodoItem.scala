package model.item

import lib.model.Todo
import lib.model.Todo._

case class TodoItem(
    id:         Long,
    title:      String,
    body:       String,
    state:      Todo.Status,
    categoryId: Long
)
object TodoItem {
  def apply(
      id:   Long,
      title: String,
      body:  String,
      state: Todo.Status,
      categoryId: Long
  ): TodoItem = {
    new TodoItem(
      id         = id,
      title      = title,
      body       = body,
      state      = state,
      categoryId = categoryId
    )
  }
}
