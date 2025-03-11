package arc.graffiti.resourcepack

import java.security.MessageDigest
import java.util.*

fun sha1Base64(data: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-1").digest(data)
    return Base64.getEncoder().encodeToString(digest)
}