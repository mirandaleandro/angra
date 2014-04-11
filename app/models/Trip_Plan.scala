package models
import PostgresConnection._
import java.util.Date

class Trip_Plan(var itineraryPlan_id:ItineraryPlan,var additional_transportation:String, var hotel_name:String, var hotel_confirm:String,var hotel_address:String, var hotel_phone:String) extends Entity

object Trip_Plan
{
  def apply(itineraryPlan_id:ItineraryPlan,additional_transportation:String, hotel_name:String, hotel_confirm:String,hotel_address:String, hotel_phone:String) =
    transactional
    {
      new Trip_Plan(itineraryPlan_id, additional_transportation, hotel_name, hotel_confirm,hotel_address, hotel_phone)
    }

  def findById(id: String) = byId[Trip_Plan](id)

  def findByItineraryPlan(itineraryPlan_id:ItineraryPlan): List[Trip_Plan] = transactional
  {
    (select[Trip_Plan] where(_.itineraryPlan_id :== itineraryPlan_id))
  }


  def getAll:List[Trip_Plan] = all[Trip_Plan]

}