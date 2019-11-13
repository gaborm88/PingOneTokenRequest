package services

import java.net.URL
import java.security.interfaces.RSAPublicKey
import java.util

import com.auth0.jwk.{GuavaCachedJwkProvider, Jwk, UrlJwkProvider}
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.PublicClaims
import com.auth0.jwt.interfaces.Claim
import controllers.Constants.{CLIENT_ID, JKS_ENDPOINT}
import entities.UserInfo

import scala.util.{Failure, Success, Try}

object ValidateJwtService {

  private val issuer = s"https://sso.connect.pingidentity.com/$CLIENT_ID" // typically obtained during Discovery

  def validateIdToken(token: String) = {
    validate(token) match {
      case Success(result) => result
      case Failure(e) => throw e
    }
  }

  private def validate(token: String) =
    for {
      jwk <- getJwk(token)
      jwt <- verifyJwt(jwk, token)
      userInfo <- getUserInfo(jwt.getClaims())
    } yield
      (jwt, userInfo)

  private def getJwk(token: String) = {
    Try {
      val provider = getProvider(new URL(JKS_ENDPOINT))
      val kid = JWT.decode(token).getKeyId
      provider.get(kid)
    }
  }

  private def verifyJwt(jwk: Jwk, token: String) = {
    Try {
      val publicKey = jwk.getPublicKey.asInstanceOf[RSAPublicKey]
      val algorithm = Algorithm.RSA256(publicKey, null)
      JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(CLIENT_ID)
        .build()
        .verify(token)
    }
  }

  private def getUserInfo(claims: util.Map[String, Claim]) = {
    Try {
      val email = claims.get(PublicClaims.SUBJECT).asString()

      UserInfo(email)
    }
  }

  private def getProvider(jwkUrl: URL) = new GuavaCachedJwkProvider(new UrlJwkProvider(jwkUrl))
}
