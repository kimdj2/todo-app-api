package model.view

import model.ViewValueHome
import model.item.TodoItem
import model.item.TodoCategoryItem

// TodoCreateのviewvalue
case class ViewValueTodoEdit(
  viewValue: ViewValueHome,
  categories: Seq[TodoCategoryItem],
  model: TodoItem,
)

object ViewValueTodoEdit {
  def apply(categories: Seq[TodoCategoryItem], model: TodoItem): ViewValueTodoEdit = {
    ViewValueTodoEdit(
      viewValue = ViewValueHome(
        title = "TODO編集",
        cssSrc = Seq("main.css", "todo/edit.css"),
        jsSrc = Seq("main.js")
      ),
      categories = categories,
      model = model
    )
  }
}
