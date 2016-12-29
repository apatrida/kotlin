/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@JsName("arrayIterator")
internal fun arrayIterator(array: dynamic): MutableIterator<dynamic> {
    return object : MutableIterator<dynamic> {
        var index = 0;

        override fun hasNext(): Boolean {
            val length: Int = array.length
            return index < length
        }

        override fun next() = array[index++]

        override fun remove() {
            array.splice(--index, 1)
        }
    }
}

@JsName("PropertyMetadata")
internal class PropertyMetadata(val name: String)

@JsName("captureStack")
internal fun captureStack(baseClass: JsClass<in Throwable>, instance: Throwable) {
    if (js("Error").captureStackTrace) {
        js("Error").captureStackTrace(instance, instance::class.js);
    }
    else {
        instance.asDynamic().stack = js("new Error()").stack;
    }
}

@JsName("newThrowable")
internal fun newThrowable(message: String?, cause: Throwable?): Throwable {
    val throwable = js("new Error()")
    throwable.message = if (jsTypeOf(message) == "undefined" && cause != null) cause.toString() else message
    throwable.cause = cause
    return throwable
}