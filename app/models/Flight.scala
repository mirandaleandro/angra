package models
import PostgresConnection._
import java.util.Date


class Flight(var trip_confirmed_id:Trip_Confirmed, var number:String, var seat:String, var airline:String,var date:Date, var time:Date, var depart_location:String, var destination:String, var confirm_no:String) extends Entity

object Flight
{
  def apply(trip_confirmed_id:Trip_Confirmed, number:String, seat:String, airline:String, date:Date, time:Date, depart_location:String, destination:String, confirm_no:String) =
    transactional
    {
      new Flight(trip_confirmed_id, number, seat, airline, date, time, depart_location, destination, confirm_no)
    }

  def findById(id: String) = byId[Flight](id)

  def findByTrip_Confirmed(trip_confirmed_id:Trip_Confirmed): Option[Flight] = transactional
  {
    (select[Flight] where(_.trip_confirmed_id :== trip_confirmed_id)).headOption
  }


  def getAll:List[Flight] = all[Flight]

}