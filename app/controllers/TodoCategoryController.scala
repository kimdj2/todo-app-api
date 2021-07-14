/** to do sample project
  */

package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import model.view.ViewValueCategoryList
import model.view.ViewValueCategoryCreate
import model.view.ViewValueCategoryEdit
import model.ViewValueError

import service.TodoService
import service.TodoCategoryService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoCategoryController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController
    with I18nSupport {

  import forms.TodoCategoryForm._

  def index() = Action.async { implicit req =>
    for {
      categories <- TodoCategoryService.all
    } yield {
      val vv = ViewValueCategoryList(categories)
      Ok(views.html.category.List(vv))
    }
  }

  def create() = Action { implicit req =>
    val vv = ViewValueCategoryCreate()
    Ok(views.html.category.Create(vv, categoryForm))
  }

  def save() = Action.async { implicit req =>
    categoryForm
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[CategoryForm]) => {
          val vv = ViewValueCategoryCreate()
          Future.successful(Ok(views.html.category.Create(vv, formWithErrors)))
        },
        (categoryData: CategoryForm) => {
          for {
            _ <- TodoCategoryService.add(categoryData)
          } yield {
            Redirect(routes.TodoCategoryController.index)
          }
        }
      )

  }

  def edit(id: Long) = Action.async { implicit req =>
    (for {
      category <- TodoCategoryService.get(id)
    } yield {
      val vv = ViewValueCategoryEdit(category)
      Ok(
        views.html.category.Edit(
          vv,
          categoryForm.fill(
            CategoryForm(
              name = category.name,
              slug = category.slug,
              color = category.color.code
            )
          )
        )
      )
    }) recover { case _: Exception =>
      NotFound(views.html.error.page404(ViewValueError()))
    }

  }

  def update(id: Long) = Action.async { implicit req =>
    categoryForm
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[CategoryForm]) => {
          for {
            category <- TodoCategoryService.get(id)
          } yield {
            val vv = ViewValueCategoryEdit(category)
            Ok(
              views.html.category.Edit(
                vv,
                formWithErrors
              )
            )
          }
        },
        (categoryData: CategoryForm) =>
          (
            for {
              _ <- TodoCategoryService.update(id, categoryData)
            } yield Redirect(routes.TodoCategoryController.index)
          ) recover { case _: Exception =>
            NotFound(views.html.error.page404(ViewValueError()))
          }
      )

  }

  def delete(id: Long) = Action.async { implicit req =>
    (for {
      _ <- TodoCategoryService.delete(id)
    } yield Redirect(routes.TodoCategoryController.index)) recover {
      case _: Exception =>
        NotFound(views.html.error.page404(ViewValueError()))
    }
  }

}
