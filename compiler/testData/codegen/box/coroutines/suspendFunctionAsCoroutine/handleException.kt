// WITH_RUNTIME
// WITH_COROUTINES
import kotlin.coroutines.*

class Controller {
    var exception: Throwable? = null
    val postponedActions = ArrayList<() -> Unit>()

    suspend fun suspendWithValue(v: String): String = CoroutineIntrinsics.suspendCoroutineOrReturn { x ->
        postponedActions.add {
            x.resume(v)
        }

        CoroutineIntrinsics.SUSPENDED
    }

    suspend fun suspendWithException(e: Exception): String = CoroutineIntrinsics.suspendCoroutineOrReturn { x ->
        postponedActions.add {
            x.resumeWithException(e)
        }

        CoroutineIntrinsics.SUSPENDED
    }

    fun run(c: suspend Controller.() -> Unit) {
        c.startCoroutine(this, handleExceptionContinuation {
            exception = it
        })
        while (postponedActions.isNotEmpty()) {
            postponedActions[0]()
            postponedActions.removeAt(0)
        }
    }
}

fun builder(c: suspend Controller.() -> Unit) {
    val controller = Controller()
    controller.run(c)

    if (controller.exception?.message != "OK") {
        throw RuntimeException("Unexpected result: ${controller.exception?.message}")
    }
}

fun commonThrow(t: Throwable) {
    throw t
}

suspend fun justContinue(): Unit = CoroutineIntrinsics.suspendCoroutineOrReturn { x ->
    x.resume(Unit)

    CoroutineIntrinsics.SUSPENDED
}

suspend fun Controller.test1() {
    justContinue()
    throw RuntimeException("OK")
}

suspend fun Controller.test2() {
    justContinue()
    commonThrow(RuntimeException("OK"))
}

suspend fun Controller.test3() {
    justContinue()
    suspendWithException(RuntimeException("OK"))
}

suspend fun Controller.test4() {
    justContinue()
    try {
        suspendWithException(RuntimeException("fail 1"))
    } catch (e: RuntimeException) {
        suspendWithException(RuntimeException("OK"))
    }
}

suspend fun Controller.test5() {
    justContinue()
    try {
        suspendWithException(Exception("OK"))
    } catch (e: RuntimeException) {
        suspendWithException(RuntimeException("fail 3"))
        throw RuntimeException("fail 4")
    }
}

fun box(): String {
    builder {
        test1()
    }

    builder {
        test2()
    }

    builder {
        test3()
    }

    builder {
        test4()
    }

    builder {
        test5()
    }

    return "OK"
}
