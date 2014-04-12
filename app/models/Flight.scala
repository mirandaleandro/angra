package models
import PostgresConnection._
import java.util.Date


class Flight(var trip_plan_id:Trip_Plan,
             var flight_sort_number:Int,
             var number:String,
             var seat:String,
             var airline:String,
             var depart_location:String,
             var depart_date:String,
             var depart_time:String,
             var arrival_location:String,
             var arrival_date:String,
             var arrival_time:String,
             var confirm_no:String) extends Entity

object Flight
{
  def apply(trip_plan_id:Trip_Plan,
            flight_sort_number:Int = 0,
            number:String = "",
            seat:String = "",
            airline:String = "",
            depart_location:String = "",
            depart_date:String = "",
            depart_time:String = "",
            arrival_location:String = "",
            arrival_date:String = "",
            arrival_time:String = "",
            confirm_no:String = "") =
    transactional
    {
      new Flight(trip_plan_id = trip_plan_id,
        flight_sort_number = flight_sort_number,
        number=number,
        seat=seat,
        airline=airline,
        depart_location=depart_time,
        depart_date=depart_date,
        depart_time=depart_time,
        arrival_location=arrival_location,
        arrival_date=arrival_date,
        arrival_time=arrival_time,
        confirm_no=confirm_no)
    }

  def findById(id: String) = byId[Flight](id)

  def findByTrip_Confirmed(trip_plan_id:Trip_Plan): List[Flight] = transactional
  {
    select[Flight] where(_.trip_plan_id :== trip_plan_id) sortBy(_.flight_sort_number)
  }

  def getAll:List[Flight] = all[Flight]

}