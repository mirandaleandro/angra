package controllers

import
play.api.mvc.{Action, Controller}
import models.PostgresConnection._
import models.{Trip_Request, User}
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import java.util.Date

object TravelPlanner extends Controller
{
  val tripViewForm = Form[Int](
    mapping("tripNumber" -> number)
      // binding
      (number => number)
      // unbinding
      (info => Some(info))



  )

  val numberedViewForm = Form[Int](
      mapping("number" -> number)
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

    def flightView = Action
    {
      implicit request =>
        numberedViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          flighNumber =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.flight (flighNumber, open = true))
            }
          })
    }


}
