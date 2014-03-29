package controllers


import play.api._
import play.api.mvc._
import models.User
import models.PostgresConnection._
import play.api.data._
import data.Forms._
import play.api.libs.iteratee._
import scala.concurrent.Future
import scala.Some


/**
 * Created by MariaYacaman on 3/22/14.
 */
object Authentication extends Controller
{

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText(8),
      "password" -> nonEmptyText(8),
      "phone" -> nonEmptyText(10),
      "admin" -> optional(boolean)
    )(User.apply)(User.unapply))

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(views.html.login()()())
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(Some(formWithErrors))()()),
      user => Redirect(routes.Application.dashboard).withSession("email" -> user._1)
    )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Authentication.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }


  def register = Action {
    implicit request =>
      userForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login()( Some(formWithErrors) )()),
        registration => {
          Redirect(routes.Application.dashboard).flashing(
            "message" -> "User Registered!"
          )
        }
      )
  }


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
    private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Authentication.authenticate())

    // --

    /**
     * Action for authenticated users.
     */
    def IsAuthenticated(f: => Option[User] => Request[AnyContent] => Result) =

      Security.Authenticated(username, onUnauthorized) {
        email =>
           Action(request => f(User.findByEmail(email))(request))
    }

    def withAuth(f: => Option[User] => Request[AnyContent] => Result) = {
      Security.Authenticated(username, onUnauthorized) { email =>
        Action(request => f( User.findByEmail(email) )(request) )
      }
    }
  }
}






