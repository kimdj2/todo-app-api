package forms

object TodoCategoryForm  {
  import play.api.data._
  import play.api.data.Forms._
  import play.api.data.validation.Constraints
  import play.api.data.validation.Constraints._

  case class CategoryForm(name: String, slug: String, color: Int)

  // 半角英数のみ
  val halfWidthAlphaNum = Constraints.pattern("[a-zA-Z0-9]+".r, error="半角英数のみ")
  val categoryForm: Form[CategoryForm] = Form(
    mapping(
      "name"      -> nonEmptyText(maxLength = 255),
      "slug"      -> nonEmptyText(maxLength = 64).verifying(halfWidthAlphaNum),
      "color"     -> number
    )(CategoryForm.apply)(CategoryForm.unapply)
  )
}
