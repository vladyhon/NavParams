package io.github.vladyhon.navparams.internal.queue

import app.cash.turbine.test
import io.github.vladyhon.navparams.getAndConsume
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.expect

internal class NavParameterQueueTest {

    private val testScope = TestScope(StandardTestDispatcher())

    @Test
    fun `get and consume value, after queue should be empty`() = testScope.runTest {
        val queue = NavParameterQueue.create<String>()

        launch {
            queue.emit("12345")
        }

        queue.values.test {
            val item = awaitItem()
            expect("12345") { item.getAndConsume() }

            expectNoEvents()
        }
    }

    @Test
    fun `get without consume value, after queue should not be empty`() = testScope.runTest {
        val queue = NavParameterQueue.create<String>()

        launch {
            queue.emit("12345")
        }

        queue.values.test {
            val item = awaitItem()
            expect("12345") { item.value }
            expect("12345") { item.getAndConsume() }

            expectNoEvents()
        }
    }

    @Test
    fun `emit 2 values, get and consume values, after queue should be empty`() = testScope.runTest {
        val queue = NavParameterQueue.create<String>()

        launch {
            queue.emit("12345")
        }

        launch {
            queue.emit("23456")
        }

        queue.values.test {
            val item1 = awaitItem()
            expect("12345") { item1.getAndConsume() }

            val item2 = awaitItem()
            expect("23456") { item2.getAndConsume() }

            expectNoEvents()
        }
    }

    @Test
    fun `emit 2 values, get without consume values, after queue should not be empty`() =
        testScope.runTest {
            val queue = NavParameterQueue.create<String>()

            launch {
                queue.emit("12345")
            }

            launch {
                queue.emit("23456")
            }

            queue.values.test {
                val item1 = awaitItem()
                expect("12345") { item1.value }
                expect("12345") { item1.getAndConsume() }

                val item2 = awaitItem()
                expect("23456") { item2.getAndConsume() }

                expectNoEvents()
            }
        }
}
