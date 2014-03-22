package models
import PostgresConnection._
import java.util.Date


class Airline(var name:String) extends Entity

  object Airline
  {
    def apply(name:String) =
      transactional
      {
        new Airline(name)
      }

    def findById(id: String) = byId[Airline](id)

    def findByName(name:String): Option[Airline] = transactional
    {
      (select[Airline] where(_.name :== name)).headOption
    }


    def getAll:List[Airline] = all[Airline]

  
}
