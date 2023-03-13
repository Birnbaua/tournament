package at.birnbaua.tournament.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Resource not in database")
class ResourceNotFoundException(msg: String = "") : RuntimeException(msg)