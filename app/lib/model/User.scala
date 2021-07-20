/**
  * This is a sample of Todo Application.
  * 
  */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import User._
case class User(
  id:        Option[Id],
  email:     String,
  password:  String,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object User {

  val  Id = the[Identity[Id]]
  type Id = Long @@ User
  type WithNoId = Entity.WithNoId [Id, User]
  type EmbeddedId = Entity.EmbeddedId[Id, User]

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(email: String, password: String): WithNoId = {
    new Entity.WithNoId(
      new User(
        id    = None,
        email  = email,
        password   = password
      )
    )
  }
}