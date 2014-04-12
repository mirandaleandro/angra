package models
import PostgresConnection._
import java.util.Date


class Trip_Request(var request_id:Client_Request, var depart_date:String, var depart_location:String, var depart_time:String, var arrival_location:String, var arrival_time:String,var additional_transportation:String, var hotel:Boolean, var hotel_meal:Boolean, var checkout_date:String) extends Entity
{
  def taxi = this.additional_transportation=="taxi"
  def rental = this.additional_transportation=="rental"
  def shuttle = this.additional_transportation=="shuttle"
  def isThereAdditionalTransportation: Boolean = this.rental || this.taxi || this.shuttle

  def airlinesNames:List[String] = this.airlines.map(_.name)

  def airlines:List[Airline] = Trip_RequestAirline.findByTripRequest(this).map(_.airline)

  def addAirlines(names:List[String])
  {
      names.foreach(this.addAirline(_))
  }

  def addAirline(name:String)
  {
    val airline:Airline = Airline.findByName(name).getOrElse(Airline(name))

    Trip_RequestAirline.findByTripRequestAndAirline(this, airline).getOrElse(Trip_RequestAirline(this,airline))

  }
}

object Trip_Request
{
  def apply(request_id:Client_Request, depart_date:String, depart_location:String,  depart_time:String,  arrival_location:String,  arrival_time:String, airlines:List[String], additional_transportation:String,  hotel:Boolean,  hotel_meal:Boolean,  checkout_date:String) =
  transactional
  {
    val trip = new Trip_Request(request_id,depart_date, depart_location, depart_time, arrival_location, arrival_time, additional_transportation,  hotel,  hotel_meal, checkout_date)

    trip.addAirlines(airlines)

    trip

  }

  def findById(id: String) = byId[Trip_Request](id)

  def findByRequest(request_id:Client_Request): List[Trip_Request] = transactional
  {
    select[Trip_Request] where(_.request_id :== request_id)
  }

  def findByUser(user:User): Option[Trip_Request] = transactional
  {
    (select[Trip_Request] where(_.request_id.user_id :== user)).headOption
  }

  def getAll:List[Trip_Request] = all[Trip_Request]

}