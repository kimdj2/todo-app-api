package helpers

object TodoHelper {
  import lib.model.Todo._
  import lib.model.TodoCategory._
  import model.item.TodoCategoryItem

  def getStateOptions(): Seq[(String, String)] = {
    Seq(
      (Status.TODO.code.toString, Status.TODO.name),
      (Status.PROGRESS.code.toString, Status.PROGRESS.name),
      (Status.END.code.toString, Status.END.name)
    )
  }

  def categoryName(categoryOpt: Option[TodoCategoryItem]): String =
    categoryOpt match {
      case Some(category) => {
        s"<span class='todo-list__category-name ${getStateStyle(category.color)}'>${category.name}</span>"
      }
      case None => ""
    }

  private def getStateStyle(color: Color): String = color match {
    case Color.RED    => "todo-list__category-name--red"
    case Color.YELLOW => "todo-list__category-name--yellow"
    case Color.BLUE   => "todo-list__category-name--blue"
  }
}
