/** This is a sample of Todo Application.
  */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// TODOを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import Todo._
case class Todo(
    id:          Option[Id],
    categoryId: TodoCategory.Id,
    title:       String,
    body:        String,
    state:       Status,
    updatedAt:   LocalDateTime = NOW,
    createdAt:   LocalDateTime = NOW
) extends EntityModel[Id] 
//EntityModelWithNoTimeRecも存在
//updatedAt、createdAtが別にいらないテーブルは使う

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object Todo {

  val Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  // 1:TODO    (着手前)
  // 2:PROGRESS(進行中)
  // 3:END     (完了)
  sealed abstract class Status(val code: Short, val name: String)
      extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object TODO     extends Status(code = 0, name = "着手前")
    case object PROGRESS extends Status(code = 1, name = "進行中")
    case object END      extends Status(code = 2, name = "終了")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(
      categoryId:  TodoCategory.Id,
      title:       String,
      body:        String,
      state:       Status
  ): WithNoId = {
    new Entity.WithNoId(
      new Todo(
        id         = None,
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }
}
