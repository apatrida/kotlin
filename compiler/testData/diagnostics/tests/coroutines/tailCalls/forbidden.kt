// !DIAGNOSTICS: -UNUSED_PARAMETER
import kotlin.coroutines.*

fun nonSuspend() {}

suspend fun foo() {
    CoroutineIntrinsics.suspendCoroutineOrReturn { x: Continuation<Int> -> }

    nonSuspend()
}

suspend fun unitSuspend() {
    CoroutineIntrinsics.suspendCoroutineOrReturn { x: Continuation<Int> -> }
}

suspend fun baz(): Int = run {
    CoroutineIntrinsics.suspendCoroutineOrReturn { x: Continuation<Int> -> }
}
