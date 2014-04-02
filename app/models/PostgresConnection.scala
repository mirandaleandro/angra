package models

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.postgresqlDialect

object PostgresConnection extends ActivateContext {
  override val storage = new PooledJdbcRelationalStorage {
    val jdbcDriver = "org.postgresql.Driver"
    val user = "d2fsoa6nldt0du"
    val password = "y9YH2xFG2hfTzJKbytiRspSXAr"
    val url = "jdbc:postgresql://ec2-184-73-194-196.compute-1.amazonaws.com/d2fsoa6nldt0du"
    val dialect = postgresqlDialect
  }
}