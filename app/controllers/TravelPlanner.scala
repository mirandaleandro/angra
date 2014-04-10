package controllers

import
play.api.mvc.{Action, Controller}
import models.PostgresConnection._
import models.{Trip_Request, User}
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import java.util.Date
import controllers.Authentication.Secured
import models.Client_Request


object TravelPlanner extends Controller with Secured
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
        "arrival_time" -> text,
        "additional_transportation" -> text,
        "hotel_name" -> text,
        "hotel_membership" -> text,
        "checkout_date" -> text
      )(TripRequestForm.apply)(TripRequestForm.unapply))
    )(ClientRequestForm.apply)(ClientRequestForm.unapply)
  )

 def sendRequest = withAuth{ implicit user =>

    implicit request =>
      tripRequestForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.landingpage()),
        requested => {
          user.map{ myUser =>
            val cr = models.Client_Request(user_id = myUser,ret_date = requested.ret_date, ret_location = requested.ret_location, ret_time = requested.ret_time, comments = requested.notes)

            requested.trips.foreach{trip =>
              models.Trip_Request(request_id = cr, depart_date = trip.depart_date, depart_location = trip.depart_location, depart_time = trip.depart_time, airline = trip.airline, arrival_location = trip.arrival_location, arrival_time = trip.arrival_time, additional_transportation = trip.additional_transportation, hotel_name = trip.hotel_name, hotel_membership = trip.hotel_membership, checkout_date = trip.checkout_date)
            }
          }
          Redirect(routes.Application.travelPlanner).flashing(
            "message" -> "Your trip has been submitted!"
          )
        }
      )
  }

  case class FlightForm(depart_location:String, arrival_location:String, date:String, time:String,airline:String, number:String, seat:String,  confirm_num:String)
  case class TripPlanForm(additional_transportation:String,  hotel_name:String,  hotel_membership:String, hotel_address:String, hotel_phone:String, flights:List[FlightForm])
  case class ItineraryPlanForm(ret_date:String, ret_location:String,ret_time:String,ret_flight:String,ret_seat:String,ret_airline:String, tripPlans:List[TripPlanForm])
  case class ItineraryForm(comments:String, request_id:String, itineraryPlans:List[ItineraryPlanForm])

  val itineraryForm = Form(
    mapping(
    "comments" -> text,
    "request_id" -> text,
    "itineraryPlans" -> list(mapping(
      "ret_date" -> text,
      "ret_location" -> text,
      "ret_time" -> text,
      "ret_flight" -> text,
      "ret_seat" -> text,
      "ret_airline" -> text,
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
//          val clientreq = models.Client_Request.findById(requested.request_id)
//          val itin =  models.Itinerary(request_id = clientreq, comments = requested.comments)
//
//          requested.itineraryPlans.foreach{itineraryPlan =>
//            val itinPlan = models.ItineraryPlan(itinerary_id = itin, ret_date = itineraryPlan.ret_date, ret_location = itineraryPlan.ret_location, ret_time = itineraryPlan.ret_time, ret_flight = itineraryPlan.ret_flight, ret_seat = itineraryPlan.ret_seat, ret_airline = itineraryPlan.ret_airline)
//
//            itineraryPlan.tripPlans.foreach{tripPlan =>
//             val trip = models.Trip_Plan(itineraryPlan_id = itinPlan, additional_transportation = tripPlan.additional_transportation, hotel_name = tripPlan.hotel_name, hotel_confirm = tripPlan.hotel_membership, hotel_address = tripPlan.hotel_address, hotel_phone= tripPlan.hotel_phone)
//
//              tripPlan.flights.foreach{flight =>
//                models.Flight(trip_plan_id = trip, number = flight.number, seat = flight.seat, airline = flight.airline, date=flight.date, time=flight.time, depart_location=flight.depart_location, destination= flight.arrival_location, confirm_no = flight.confirm_num)
//              }
//            }
//          }

          Redirect(routes.Application.travelPlanner).flashing(
            "message" -> "Your itinerary has been submitted!"
          )
        }
      )
  }



}

