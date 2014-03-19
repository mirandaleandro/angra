package controllers

import play.api.mvc.{Action, Controller}
import models.PostgresConnection._
import models.User
import play.api.data.Form
import play.api.data.Forms._
import scala.Some

object TravelPlanner extends Controller
{
    val tripViewForm = Form[Int](
      mapping("tripNumber" -> number)
        // binding
        (number => number)
        // unbinding
        (info => Some(info))
    )

    def tripView = Action
    {
      implicit request =>
        tripViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          tripNumber =>
          {
            transactional{
              Ok( views.html.PlanTravel.trip (tripNumber, open = true))
            }
          })
    }
}
