package models

import PostgresConnection._
import net.fwbrasil.activate.migration.Migration
import scala.collection.mutable.{ Map => MutableMap }
import java.util.Date
import java.text.SimpleDateFormat

class CreateSchema extends Migration {
    
    def timestamp = 201402282003l

    def up = {
        removeAllEntitiesTables
            .ifExists
        createTableForAllEntities
            .ifNotExists
    }
}

class CreateDefaultUsers extends Migration {

    def timestamp = 201402282010l

    def up = {
        customScript
        {
           val user1 = User("leandro","mirandaleandro@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
          val user2 =User("tahna","tahna@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
          val user3 = User("alex","alex@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
          val user4 = User("maria","maria@gmail.com","piracyisgood", "(801) 123-4567",admin=true)

          val date1 = new Date(20140318)
          val time1 = new Date(100000)
          val date2 = new Date(20140319)
          val time2 = new Date(110000)
          val date3 = new Date(20140320)
          val time3 = new Date(120000)
          val date4 = new Date(20140418)
          val time4 = new Date(100000)
          val date5 = new Date(20140419)
          val time5 = new Date(110000)
          val date6 = new Date(20140420)
          val time6 = new Date(120000)

          val req1 = Request(user1,date4,"San Diego",time4,"Better to return before 10:00 than after")
          val req2 = Request(user2,date5,"Las Vegas",time5,"Better to return before 11:00 than after")
          val req3 = Request(user3,date6,"Salt Lake City",time6,"Better to return before 12:00 than after")

          Trip_Request(req1, date1, "San Diego",  time1,  "Denver",  time2, "United", rental=true,  taxi=false,  shuttle=false,  "The Hilton",  "123ABC",  date3)
          Trip_Request(req2, date2, "Las Vegas",  time2,  "Fresno",  time2, "United", rental=true,  taxi=false,  shuttle=false,  "The Hilton",  "123ABC",  date3)
          Trip_Request(req3, date3, "Salt Lake",  time3,  "Orlando",  time2, "United", rental=true,  taxi=false,  shuttle=false,  "The Hilton",  "123ABC",  date3)

         val it1 =  Itinerary(req1, date1, "San Diego", time1, "370", "35B", "United", "10:00AM on the dot!")
         val it2 =  Itinerary(req2, date2, "Las Vegas", time2, "371", "36B", "American", "11:00AM on the dot!")
         val it3 =  Itinerary(req3, date3, "Salt Lake", time3, "372", "37B", "Delta", "12:00AM on the dot!")

         val tc1 = Trip_Confirmed(it1,"Hertz Rental","A92385345",null, null, "The Hilton", "9347653495","123 Fake Street", "(619)-777-8888")
         val tc2 = Trip_Confirmed(it1,"Hertz Rental","A92385345",null, null, "The Hilton", "9347653495","123 Fake Street", "(619)-777-8888")
         val tc3 = Trip_Confirmed(it1,"Hertz Rental","A92385345",null, null, "The Hilton", "9347653495","123 Fake Street", "(619)-777-8888")

          Flight(tc1, "551", "21C", "United",date1, time1, "San Diego", "Denver", "34076597651")
          Flight(tc2, "552", "22C", "United",date2, time2, "Las Vegas", "Fresno", "34076597652")
          Flight(tc3, "553", "23C", "United",date3, time3, "Salt Lake", "Orlando", "34076597653")
        }
    }
}
