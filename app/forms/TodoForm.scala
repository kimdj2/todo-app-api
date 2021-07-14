package forms

object TodoForm {
  import play.api.data._
  import play.api.data.Forms._
  import play.api.data.validation.Constraints._

  case class TodoAdd(title: String, body: String, categoryId: Long)
  case class TodoEdit(title: String, body: String, state: Int, categoryId: Long)

  val todoAddForm: Form[TodoAdd] = Form(
    mapping(
      "title"      -> nonEmptyText(maxLength = 255),
      "body"       -> nonEmptyText,
      "categoryId" -> longNumber
    )(TodoAdd.apply)(TodoAdd.unapply)
  )

  val todoEditForm: Form[TodoEdit] = Form(
    mapping(
      "title"      -> nonEmptyText(maxLength = 255),
      "body"       -> nonEmptyText,
      "state"      -> number,
      "categoryId" -> longNumber
    )(TodoEdit.apply)(TodoEdit.unapply)
  )

}
