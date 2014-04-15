// CODE FOR HEROKU DEPLOYMENT
//package models
//
//import net.fwbrasil.activate.ActivateContext
//import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
//import net.fwbrasil.activate.storage.relational.idiom.postgresqlDialect
//
//object PostgresConnection extends ActivateContext {
//  override val storage = new PooledJdbcRelationalStorage {
//    val jdbcDriver = "org.postgresql.Driver"
//    val user = "ibibjyxukawtqo"
//    val password = "y9YH2xFG2hfTzJKbytiRspSXAr"
//    val url = "jdbc:postgresql://ec2-184-73-194-196.compute-1.amazonaws.com/d2fsoa6nldt0du"
//    val dialect = postgresqlDialect
//  }
//}

//CODE FOR PLAY FRAMEWORK LOCAL USE
package models

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.postgresqlDialect
import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
import net.fwbrasil.activate.ActivateContext

object PostgresConnection extends ActivateContext
{
  override val storage = new TransientMemoryStorage
}