/** to do sample project
  */

package model

// Topページのviewvalue
case class ViewValueError(
    title: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String]
) extends ViewValueCommon

object ViewValueError {
  def apply(): ViewValueError = {
    new ViewValueError("エラー", Seq("main.css"), Seq("main.js"))
  }
}
