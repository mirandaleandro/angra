package controllers


import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.iteratee._
import scala.concurrent.Future


/**
 * Created by MariaYacaman on 3/22/14.
 */
object Authentication extends Controller {


  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )




  /**
   * Provide security features
   */
  trait Secured {

    /**
     * Retrieve the connected user email.
     */
    private def username(request: RequestHeader) = request.session.get("email")

    /**
     * Redirect to login if the user in not authorized.
     */
    private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.authenticate())

    // --

    /**
     * Action for authenticated users.
     */
    def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }

    def withAuth(f: => String => Request[AnyContent] => Result) = {
      Security.Authenticated(username, onUnauthorized) { user =>
        Action(request => f(user)(request))
      }
    }
  }
}






