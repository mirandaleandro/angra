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
           User("leandro","mirandaleandro@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
           User("tahna","tahna@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
           User("alex","alex@gmail.com","piracyisgood", "(801) 123-4567", admin=true)
           User("maria","maria@gmail.com","piracyisgood", "(801) 123-4567",admin=true)

        }
    }
}
