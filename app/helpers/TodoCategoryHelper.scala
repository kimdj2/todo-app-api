package helpers

object TodoCategoryHelper {
  import lib.model.TodoCategory._

  def getColorOptions(): Seq[(String, String)] = {
    Seq(
      (Color.RED.code.toString,    Color.RED.name),
      (Color.YELLOW.code.toString, Color.YELLOW.name),
      (Color.BLUE.code.toString,   Color.BLUE.name)
    )
  }

  def getColorStyle(color: Color): String = color match {
    case Color.RED    => "category-list__color--red"
    case Color.YELLOW => "category-list__color--yellow"
    case Color.BLUE   => "category-list__color--blue"
  }

}
