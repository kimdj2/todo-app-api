package service

import lib.model.TodoCategory
import lib.model.TodoCategory._

import lib.persistence.onMySQL.TodoRepository
import lib.persistence.onMySQL.TodoCategoryRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import model.item.TodoCategoryItem
import forms.TodoCategoryForm._

object TodoCategoryService {
  def all(): Future[Seq[TodoCategoryItem]] = {
    for {
      todoCategoryList <- TodoCategoryRepository.all
    } yield todoCategoryList.map(category =>
      TodoCategoryItem(
        category.id,
        category.v.name,
        category.v.slug,
        category.v.color
      )
    )
  }

  def add(form: CategoryForm): Future[Long] = {
    TodoCategoryRepository.add(
      TodoCategory(
        name = form.name,
        slug = form.slug,
        color = TodoCategory.Color(form.color.toShort)
      )
    )
  }

  def update(id: Long, form: CategoryForm): Future[Option[Long]] = {
    for {
      categoryOpt <- TodoCategoryRepository.get(TodoCategory.Id(id))
    } yield {
      categoryOpt match {
        case Some(category) => {
          TodoCategoryRepository.update(
            new TodoCategory.EmbeddedId(
              category.v.copy(
                name = form.name,
                slug = form.slug,
                color = TodoCategory.Color(form.color.toShort)
              )
            )
          )
          category.v.id
        }
        case None => throw new Exception("データなし")
      }
    }
  }

  def get(id: Long): Future[TodoCategoryItem] = {
    for {
      categoryOpt <- TodoCategoryRepository.get(TodoCategory.Id(id))
    } yield {
      categoryOpt match {
        case Some(category) =>
          TodoCategoryItem(
            category.id,
            category.v.name,
            category.v.slug,
            category.v.color
          )
        case None => throw new Exception("データなし")
      }
    }
  }

  def delete(id: Long): Future[Option[Long]] = {
    for {
      categoryOpt <- TodoCategoryRepository.remove(TodoCategory.Id(id))
    } yield {
      categoryOpt match {
        case Some(category) => {
          for {
            _ <- TodoRepository.removeByCategoryId(category.id)
          } yield category.v.id
          category.v.id
        }
        case None => throw new Exception("データなし")
      }
    }
  }

}
