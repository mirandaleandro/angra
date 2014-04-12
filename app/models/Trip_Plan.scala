package models
import PostgresConnection._
import java.util.Date

class Trip_Plan(var itineraryPlan_id:ItineraryPlan,
                var trip_number:Int,
                var additional_transportation:String,
                var hotel_name:String,
                var hotel_confirm:String,
                var hotel_address:String,
                var hotel_phone:String) extends Entity
{

  def taxi = this.additional_transportation=="taxi"
  def rental = this.additional_transportation=="rental"
  def shuttle = this.additional_transportation=="shuttle"
  def isThereAdditionalTransportation: Boolean = this.rental || this.taxi || this.shuttle

  def flights =
  {
     val dbFlights = Flight.findByTrip_Confirmed(this)

     if(dbFlights.isEmpty)
     {
       List(Flight(this))
     }
      else
     {
       dbFlights
     }

  }
}

object Trip_Plan
{
  def apply(itineraryPlan_id:ItineraryPlan,
            trip_number:Int = 0,
            additional_transportation:String = "",
            hotel_name:String = "",
            hotel_confirm:String = "",
            hotel_address:String = "",
            hotel_phone:String = "") =
    transactional
    {
      new Trip_Plan(itineraryPlan_id, trip_number = trip_number, additional_transportation, hotel_name, hotel_confirm,hotel_address, hotel_phone)
    }

  def findById(id: String) = byId[Trip_Plan](id)

  def findByItineraryPlan(itineraryPlan_id:ItineraryPlan): List[Trip_Plan] = transactional
  {
    (select[Trip_Plan] where(_.itineraryPlan_id :== itineraryPlan_id)) sortBy(_.trip_number)
  }


  def getAll:List[Trip_Plan] = all[Trip_Plan]

}