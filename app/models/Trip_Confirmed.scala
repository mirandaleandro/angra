package models
import PostgresConnection._
import java.util.Date

class Trip_Confirmed(var itinerary_id:Itinerary,var rental_car:String, var rental_confirm:String,var taxi:String, var shuttle:String, var hotel_name:String, var hotel_confirm:String,var hotel_address:String, var hotel_phone:String) extends Entity

object Trip_Confirmed
{
  def apply(itinerary_id:Itinerary,rental_car:String,rental_confirm:String,taxi:String, shuttle:String, hotel_name:String, hotel_confirm:String,hotel_address:String, hotel_phone:String) =
    transactional
    {
      new Trip_Confirmed(itinerary_id,rental_car,rental_confirm,taxi, shuttle, hotel_name, hotel_confirm,hotel_address, hotel_phone)
    }

  def findById(id: String) = byId[Trip_Confirmed](id)

  def findByItinerary(itinerary_id:Itinerary): Option[Trip_Confirmed] = transactional
  {
    (select[Trip_Confirmed] where(_.itinerary_id :== itinerary_id)).headOption
  }


  def getAll:List[Trip_Confirmed] = all[Trip_Confirmed]

}