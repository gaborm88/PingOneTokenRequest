package controllers

import controllers.Constants.{AUTHORIZATION_ENDPOINT, CLIENT_ID, IDP_ID}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class InitiateController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    val redirect_url =
      s"""$AUTHORIZATION_ENDPOINT
         |?client_id=$CLIENT_ID
         |&idpid=$IDP_ID
         |&scope=openid
         |&response_type=code""".stripMargin.replaceAll("\n", "")

    Redirect(redirect_url)
  }
}
