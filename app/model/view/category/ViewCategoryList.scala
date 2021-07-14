package model.view

import model.ViewValueHome
import model.item.TodoCategoryItem

// TodoListのviewvalue
case class ViewValueCategoryList(
  viewValue: ViewValueHome,
  categories: Seq[TodoCategoryItem]
)

object ViewValueCategoryList {
  def apply(categories: Seq[TodoCategoryItem]): ViewValueCategoryList = {
    ViewValueCategoryList(
      viewValue = ViewValueHome(
        title = "カテゴリー一覧",
        cssSrc = Seq("main.css", "category/list.css"),
        jsSrc = Seq("main.js")
      ),
      categories = categories
    )
  }
}
