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
              Ok( views.html.PlanTravel.trip (tripNumber =  tripNumber, open = true))
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

    def responseTrip = Action
    {
      implicit request =>
        numberedViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          number =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.trip (tripNumber = number, open = true))
            }
          })
    }


    def responsePlanView = Action
    {
      implicit request =>
        numberedViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          number =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.plan (number, open = true))
            }
          })
    }

  case class TripRequest(depart_location:String,depart_date:String,  depart_time:String, airline:String, arrival_location:String,  arrival_time:String,  additional_transportation:String,  hotel_name:String,  hotel_membership:String,  checkout_date:String)
  case class ClientRequest(ret_location:String,ret_date:String, ret_time:String, notes:String, trips:List[TripRequest])

  val tripRequestForm = Form(
    mapping(
      "ret_location" -> text,
      "ret_date" -> text,
      "ret_time" -> text,
      "notes" -> text,
      "trips" -> list(mapping(
        "depart_location" -> text,
        "depart_date" -> text,
        "depart_time" -> text,
        "airline" -> text,
        "arrival_location" -> text,
        "additional transportation" -> text,
        "hotel_name" -> text,
        "hotel_membership" -> text,
        "depart_time" -> text,
        "checkout_date" -> text
      )(TripRequest.apply)(TripRequest.unapply))
    )(ClientRequest.apply)(ClientRequest.unapply)
  )

  def sendRequest = Action {
    implicit request =>
      tripRequestForm.bindFromRequest.fold(
        formWithErrors =>
          BadRequest(views.html.landingpage()),
        requested =>
        {

          Redirect(routes.Application.dashboard)
        }
      )
  }
}

