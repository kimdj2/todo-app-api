package model.view

import model.ViewValueHome
import model.item.TodoCategoryItem

// TodoCreateのviewvalue
case class ViewValueTodoCreate(
  viewValue: ViewValueHome,
  categories: Seq[TodoCategoryItem]
)

object ViewValueTodoCreate {
  def apply(categories: Seq[TodoCategoryItem]): ViewValueTodoCreate = {
    ViewValueTodoCreate(
      viewValue = ViewValueHome(
        title = "TODO作成",
        cssSrc = Seq("main.css", "todo/create.css"),
        jsSrc = Seq("main.js")
      ),
      categories = categories
    )
  }
}
