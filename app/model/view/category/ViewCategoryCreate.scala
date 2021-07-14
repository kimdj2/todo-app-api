package model.view

import model.ViewValueHome
import model.item.TodoCategoryItem

// TodoCreateのviewvalue
case class ViewValueCategoryCreate(
  viewValue: ViewValueHome
)

object ViewValueCategoryCreate {
  def apply(): ViewValueCategoryCreate = {
    ViewValueCategoryCreate(
      viewValue = ViewValueHome(
        title = "カテゴリー作成",
        cssSrc = Seq("main.css", "category/create.css"),
        jsSrc = Seq("main.js")
      )
    )
  }
}
