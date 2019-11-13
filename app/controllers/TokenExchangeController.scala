package controllers

import com.auth0.jwt.exceptions.{InvalidClaimException, TokenExpiredException}
import controllers.Constants.{REST_API_KEY, REST_USERNAME, TOKEN_ENDPOINT}
import entities.{TokenResponse, UserInfo}
import javax.inject.{Inject, Singleton}
import org.apache.commons.codec.binary.Base64
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.ValidateJwtService.validateIdToken

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@Singleton
class TokenExchangeController @Inject()(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {

  def index(code: String) = Action { implicit request: Request[AnyContent] =>
    implicit val ec = play.api.libs.concurrent.Execution.defaultContext
    implicit val tokenReads = Json.reads[TokenResponse]
    val futureResponse: Future[JsResult[TokenResponse]] = requestToken(code).map { response =>
      println(response.json)
      response.json.validate[TokenResponse]
    }
    val token = Await.result(futureResponse, 10.seconds).get

    var email: Option[String] = None
    var error: Option[String] = None
    try {
      val (decodedJwt, userInfo: UserInfo) = validateIdToken(token.id_token)
      email = Some(userInfo.email)
    } catch {
      case e: TokenExpiredException => error = Some(e.getMessage)
      case e: InvalidClaimException => error = Some(e.getMessage)
      case e: Throwable => error = Some(e.getMessage)
    }

    Ok(views.html.print_token(token.access_token, token.token_type, token.expires_in, token.id_token, email.getOrElse(error.getOrElse("unknown"))))
  }

  private def requestToken(code: String) =
    ws.url(TOKEN_ENDPOINT)
      .addHttpHeaders("Authorization" -> new String(Base64.encodeBase64((REST_USERNAME + ":" + REST_API_KEY).getBytes)))
      .addQueryStringParameters("grant_type" -> "authorization_code", "code" -> code)
      .addCookies(DefaultWSCookie("agentid", "385ad402"))
      .withRequestTimeout(10000.millis)
      .post("")
}
