// Tail calls are not allowed to be Nothing typed. See KT-15051
import kotlin.coroutines.*

suspend fun suspendLogAndThrow(exception: Throwable): Nothing = CoroutineIntrinsics.suspendCoroutineOrReturn { c ->
    c.resumeWithException(exception)
    CoroutineIntrinsics.SUSPENDED
}
