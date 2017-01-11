// WITH_RUNTIME
// WITH_COROUTINES
import kotlin.coroutines.*

suspend fun suspendThere(v: String): String = CoroutineIntrinsics.suspendCoroutineOrReturn { x ->
    x.resume(v)
    CoroutineIntrinsics.SUSPENDED
}

suspend fun suspendHere(suspend: Boolean): String {
    if (!suspend) return "O"

    return suspendThere("") + suspendThere("K")
}
fun builder(c: suspend () -> Unit) {
    c.startCoroutine(EmptyContinuation)
}

fun box(): String {
    var result = ""

    builder {
        result = suspendHere(false) + suspendHere(true)
    }

    return result
}
