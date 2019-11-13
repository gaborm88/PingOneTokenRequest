package entities

case class TokenResponse(access_token: String, token_type: String, expires_in: Int, id_token: String)