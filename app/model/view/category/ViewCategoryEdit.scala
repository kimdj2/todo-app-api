package model.view

import model.ViewValueHome
import model.item.TodoCategoryItem

// TodoCreateのviewvalue
case class ViewValueCategoryEdit(
    viewValue: ViewValueHome,
    category: TodoCategoryItem
)

object ViewValueCategoryEdit {
  def apply(category: TodoCategoryItem): ViewValueCategoryEdit = {
    ViewValueCategoryEdit(
      viewValue = ViewValueHome(
        title = "カテゴリー編集",
        cssSrc = Seq("main.css", "category/edit.css"),
        jsSrc = Seq("main.js")
      ),
      category = category
    )
  }
}
