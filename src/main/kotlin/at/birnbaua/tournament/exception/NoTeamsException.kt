package at.birnbaua.tournament.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class NoTeamsException(msg: String = "") : RuntimeException(msg)