package models
import PostgresConnection._
import java.util.Date


class Flight(var trip_plan_id:Trip_Plan, var number:String, var seat:String, var airline:String,var date:String,var arrival_time:String, var depart_time:String, var depart_location:String, var destination:String, var confirm_no:String) extends Entity

object Flight
{
  def apply(trip_plan_id:Trip_Plan, number:String, seat:String, airline:String, date:String, arrival_time:String, depart_time:String, depart_location:String, destination:String, confirm_no:String) =
    transactional
    {
      new Flight(trip_plan_id, number, seat, airline, date,arrival_time, depart_time, depart_location, destination, confirm_no)
    }

  def findById(id: String) = byId[Flight](id)

  def findByTrip_Confirmed(trip_plan_id:Trip_Plan): Option[Flight] = transactional
  {
    (select[Flight] where(_.trip_plan_id :== trip_plan_id)).headOption
  }


  def getAll:List[Flight] = all[Flight]

}