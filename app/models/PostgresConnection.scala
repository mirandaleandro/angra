package models

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.postgresqlDialect

object PostgresConnection extends ActivateContext {
    override val storage = new PooledJdbcRelationalStorage {
      val jdbcDriver = "org.postgresql.Driver"
      val user = "angra"
      val password = "imapirate"
      val url = "jdbc:postgresql://127.0.0.1/angra"
      val dialect = postgresqlDialect
  }
}
