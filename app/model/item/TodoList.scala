package model.item

case class TodoList(
    todo: TodoItem,
    category: Option[TodoCategoryItem]
)
object TodoList {
  def apply(
      todo:     TodoItem,
      category: Option[TodoCategoryItem]
  ): TodoList = {
    
    new TodoList(todo = todo, category = category)
  }
}
