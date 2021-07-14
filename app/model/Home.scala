/** to do sample project
  */

package model

// Topページのviewvalue
case class ViewValueHome(
    title: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String]
) extends ViewValueCommon

object ViewValueHome {
  def apply(title: String, cssSrc: Seq[String], jsSrc: Seq[String]): ViewValueHome ={
    new ViewValueHome(title, cssSrc, jsSrc)
  }
}
