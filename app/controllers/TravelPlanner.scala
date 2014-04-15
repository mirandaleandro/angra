package controllers

import
play.api.mvc.{Action, Controller}
import models.PostgresConnection._
import models._
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import java.util.Date
import controllers.Authentication.Secured
import scala.Some


object TravelPlanner extends Controller with Secured
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
            Ok( views.html.PlanTravel.trip (tripNumber =  tripNumber, open = true))
          }
        })
  }

  case class FlightViewForm(var planNumber:Int, var tripNumber:Int, var flightNumber:Int)

  val flightViewForm = Form[FlightViewForm](
    mapping("planNumber" -> number,
      "tripNumber" -> number,
      "flightNumber" -> number )

      (FlightViewForm.apply)(FlightViewForm.unapply)
  )

  def flightView = Action
    {
      implicit request =>
        flightViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          flightViewForm =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.flight (
                planNumber = flightViewForm.planNumber,
                tripNumber = flightViewForm.tripNumber,
                flightNumber = flightViewForm.flightNumber,
                open = true,
                hidden = false)
              )
            }
          })
    }

  case class ResponseTripViewForm(var planNumber:Int, var tripNumber:Int)

  val responseTripViewForm = Form[ResponseTripViewForm](
    mapping("planNumber" -> number,
            "tripNumber" -> number )
    (ResponseTripViewForm.apply)(ResponseTripViewForm.unapply)
  )

  def responseTrip = Action
  {
    implicit request =>
      responseTripViewForm.bindFromRequest.fold(

        formWithErrors => InternalServerError,

        responseViewForm =>
        {
          transactional{
            Ok( views.html.TravelPlanResponse.trip (planNumber = responseViewForm.planNumber,
              tripNumber = responseViewForm.tripNumber,
              open = true,
              hidden = false)
            )
          }
        })
  }

    case class ResponsePlanViewForm(var planNumber:Int)

    val responsePlanViewForm = Form[ResponsePlanViewForm](
      mapping("planNumber" -> number)
      (ResponsePlanViewForm.apply)(ResponsePlanViewForm.unapply)
    )


    def responsePlanView = Action
    {
      implicit request =>
        responsePlanViewForm.bindFromRequest.fold(

          formWithErrors =>
            InternalServerError,

          responsePlanViewForm =>
          {
            transactional{
              Ok( views.html.TravelPlanResponse.plan (
                planNumber = responsePlanViewForm.planNumber,
                open = true,
                hidden = false)
              )
            }
          })
    }

  case class TripRequestForm(trip_id:String,trip_number:Int, depart_location:String,depart_date:String,  depart_time:String, airline:List[String], arrival_location:String,arrival_time:String, additional_transportation:String,  hotel:Option[String], hotel_meal:Option[String], checkout_date:String)
  case class ClientRequestForm(request_id:String, ret_location:String,ret_date:String, ret_time:String, notes:String, trips:List[TripRequestForm])

  val tripRequestForm = Form(
    mapping(
      "request_id" -> text,
      "ret_location" -> text,
      "ret_date" -> text,
      "ret_time" -> text,
      "notes" -> text,
      "trips" -> list(mapping(
        "trip_id" -> text,
        "trip_number" -> number,
        "depart_location" -> text,
        "depart_date" -> text,
        "depart_time" -> text,
        "airline" -> list(text),
        "arrival_location" -> text,
        "arrival_time" -> text,
        "additional_transportation" -> text,
        "hotel" -> optional(text),
        "hotel_meal" -> optional(text),
        "checkout_date" -> text
      )(TripRequestForm.apply)(TripRequestForm.unapply))
    )(ClientRequestForm.apply)(ClientRequestForm.unapply)
  )

 def sendRequest = withAuth{ implicit user =>

    implicit request =>
      tripRequestForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        requested => {
          transactional{
            user.map{ myUser =>

              val clientRequest = Client_Request.findById(requested.request_id).getOrElse(Client_Request(user_id = myUser))
              clientRequest.ret_date = requested.ret_date
              clientRequest.ret_location = requested.ret_location
              clientRequest.ret_time = requested.ret_time
              clientRequest.comments = requested.notes

              requested.trips.foreach { tripInfo =>
                   val trip = Trip_Request.findById(tripInfo.trip_id).getOrElse( Trip_Request(request_id = clientRequest) )
                    trip.trip_number = tripInfo.trip_number
                    trip.depart_date = tripInfo.depart_date
                    trip.depart_location = tripInfo.depart_location
                    trip.depart_time = tripInfo.depart_time
                    trip.addAirlines(tripInfo.airline)
                    trip.arrival_location = tripInfo.arrival_location
                    trip.arrival_time = tripInfo.arrival_time
                    trip.additional_transportation = tripInfo.additional_transportation
                    trip.hotel = tripInfo.hotel.exists(_=="true")
                    trip.hotel_meal = tripInfo.hotel_meal.exists(_=="true")
                    trip.checkout_date = tripInfo.checkout_date
              }
            }
            try {
              Mailer.sendEmail("New Travel Request Submitted from " + user.get.name, "Char Black", "cetservicesinc@gmail.com", user.get.name, user.get.email, "Hi Char! A new travel request has been submitted from " + user.get.name + ". Logon to your account at http://www.cetservicesinc.com/ to view.")
            } catch {
              case e: Exception => println("exception caught: " + e);
            }
          }
          Redirect(routes.Application.dashboard)
        }
      )
  }

  case class ItineraryPlanForm(itinerary_plan_id:String, itinerary_plan_number:Int, trip_plans:List[TripPlanForm])
  case class ItineraryForm(comments:String, request_id:String, itineraryPlans:List[ItineraryPlanForm])
  case class TripPlanForm(trip_plan_id:String, trip_plan_number:Int, additional_transportation:String,  hotel_name:String,  hotel_confirm:String, hotel_address:String, hotel_phone:String, flights:List[FlightForm])
  case class FlightForm(flight_id:String, flight_number:Int, arrival_location:String,arrival_time:String,arrival_date:String,depart_location:String, depart_date:String, depart_time:String,airline:String, number:String, seat:String,  confirm_num:String)

  val itineraryForm = Form(
    mapping(
    "comments" -> text,
    "request_id" -> text,
    "itinerary_plans" -> list(mapping(
        "itinerary_plan_id" -> text,
        "itinerary_plan_number" -> number,
        "trip_plans" -> list(mapping(
            "trip_plan_id" -> text,
            "trip_plan_number" -> number,
            "additional_transportation" -> text,
            "hotel_name" -> text,
            "hotel_confirm" -> text,
            "hotel_address" -> text,
            "hotel_phone" -> text,
            "flights" -> list(mapping(
                "flight_id" -> text,
                "flight_number" -> number,
                "arrival_location" -> text,
                "arrival_time" -> text,
                "arrival_date" -> text,
                "depart_location" -> text,
                "depart_date" -> text,
                "depart_time" -> text,
                "airline" -> text,
                "number" -> text,
                "seat" -> text,
                "confirm_num" -> text
                )(FlightForm.apply)(FlightForm.unapply))
            )(TripPlanForm.apply)(TripPlanForm.unapply)
        ))(ItineraryPlanForm.apply)(ItineraryPlanForm.unapply)))
    (ItineraryForm.apply)(ItineraryForm.unapply))

  def submitItinerary = withAuth {
    implicit user => implicit request =>
      itineraryForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        requested => {


          transactional
          {
            if( user.exists(_.isAdmin))
            {


              val clientRequestOption = models.Client_Request.findById(requested.request_id)

              clientRequestOption.map{ clientRequest =>
                  clientRequest.comments =  requested.comments

                  requested.itineraryPlans.foreach{ itineraryPlanInfo =>
                     val itineraryPlan: ItineraryPlan = ItineraryPlan.findById( itineraryPlanInfo.itinerary_plan_id).getOrElse(ItineraryPlan(clientRequest.itinerary))
                     itineraryPlan.plan_number = itineraryPlanInfo.itinerary_plan_number

                     itineraryPlanInfo.trip_plans.foreach{ tripPlanInfo =>
                        val trip = Trip_Plan.findById( tripPlanInfo.trip_plan_id ).getOrElse( Trip_Plan(itineraryPlan_id = itineraryPlan) )
                        trip.trip_number = tripPlanInfo.trip_plan_number

                        trip.additional_transportation = tripPlanInfo.additional_transportation
                        trip.hotel_address = tripPlanInfo.hotel_address
                        trip.hotel_confirm = tripPlanInfo.hotel_confirm
                        trip.hotel_name = tripPlanInfo.hotel_name
                        trip.hotel_phone = tripPlanInfo.hotel_phone

                        tripPlanInfo.flights.foreach{ flightInfo =>
                          val flight = Flight.findById(flightInfo.flight_id).getOrElse(Flight(trip))
                          flight.flight_sort_number = flightInfo.flight_number

                          flight.airline = flightInfo.airline
                          flight.arrival_location = flightInfo.arrival_location
                          flight.arrival_date = flightInfo.arrival_date
                          flight.arrival_time = flightInfo.arrival_time
                          flight.confirm_no = flightInfo.confirm_num
                          flight.depart_location = flightInfo.depart_location
                          flight.depart_date = flightInfo.depart_date
                          flight.depart_time = flightInfo.depart_time
                          flight.number = flightInfo.number
                          flight.seat = flightInfo.seat

                        }

                     }

                  }
              }
              try {
                val clientUser = clientRequestOption.get.user_id;

                Mailer.sendEmail("New Itinerary from CET Services", clientUser.name, clientUser.email, "Char Black", "cetservicesinc@gmail.com", "An itinerary has been created for your travel request.  Please log on to your account at http://www.cetservicesinc.com/ to view.")
              } catch {
                case e: Exception => println("exception caught: " + e)
              }

                Redirect(routes.Application.dashboard)
            }
            else
            {
              BadRequest
            }
          }

        }
      )
  }



}

