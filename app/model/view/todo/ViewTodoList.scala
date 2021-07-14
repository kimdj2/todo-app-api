package model.view

import model.ViewValueHome
import model.item.TodoList

// TodoListのviewvalue
case class ViewValueTodoList(
    viewValue: ViewValueHome,
    model: Seq[TodoList]
)

object ViewValueTodoList {
  def apply(model: Seq[TodoList]): ViewValueTodoList = {
    ViewValueTodoList(
      viewValue = ViewValueHome(
        title = "TODO一覧",
        cssSrc = Seq("main.css", "todo/list.css"),
        jsSrc = Seq("main.js")
      ),
      model = model
    )
  }
}
