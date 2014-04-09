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

    def responseTrip = Action
    {
      implicit request =>
        numberedViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          number =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.trip (number, open = true))
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

  case class TripRequestForm(depart_location:String,depart_date:String,  depart_time:String, airline:String, arrival_location:String,  arrival_time:String,  additional_transportation:String,  hotel_name:String,  hotel_membership:String,  checkout_date:String)
  case class ClientRequestForm(ret_location:String,ret_date:String, ret_time:String, notes:String, trips:List[TripRequestForm])

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
      )(TripRequestForm.apply)(TripRequestForm.unapply))
    )(ClientRequestForm.apply)(ClientRequestForm.unapply)
  )

  def sendRequest = Action {
    implicit request =>
      tripRequestForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.landingpage()),
        requested => {
          //models.Client_Request => requested.ClientRequestForm

          Redirect(routes.Application.travelPlanner).flashing(
            "message" -> "Your trip has been submitted!"
          )
        }
      )
  }

  case class FlightForm(depart_location:String, arrival_location:String, date:String, time:String,airline:String, number:String, seat:String,  confirm_num:String);
  case class TripPlanForm(additional_transportation:String,  hotel_name:String,  hotel_membership:String, hotel_address:String, hotel_phone:String, flights:List[FlightForm]);
  case class ItineraryPlanForm(tripPlans:List[TripPlanForm])
  case class ItineraryForm(comments:String, itineraryPlans:List[ItineraryPlanForm])

  val itineraryForm = Form(
    mapping(
    "comments" -> text,
    "itineraryPlans" -> list(mapping(
      "tripPlans" -> list(mapping(
        "additional_transportation" -> text,
        "hotel_name" -> text,
        "hotel_membership" -> text,
        "hotel_address" -> text,
        "hotel_phone" -> text,
        "flights" -> list(mapping(
          "depart_location" -> text,
          "arrival_location" -> text,
          "date" -> text,
          "time" -> text,
          "airline" -> text,
          "number" -> text,
          "seat" -> text,
          "confirm_num" -> text
      )(FlightForm.apply)(FlightForm.unapply))
    )(TripPlanForm.apply)(TripPlanForm.unapply)
  ))(ItineraryPlanForm.apply)(ItineraryPlanForm.unapply)))
      (ItineraryForm.apply)(ItineraryForm.unapply))

  def submitItinerary = Action {
    implicit request =>
      itineraryForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.landingpage()),
        requested => {
          //models.Client_Request => requested.ClientRequestForm

          Redirect(routes.Application.travelPlanner).flashing(
            "message" -> "Your itinerary has been submitted!"
          )
        }
      )
  }



}



