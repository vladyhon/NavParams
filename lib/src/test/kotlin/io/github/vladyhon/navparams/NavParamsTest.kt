package io.github.vladyhon.navparams

import app.cash.turbine.test
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.expect

internal class NavParamsTest {

    private val testScope = TestScope(StandardTestDispatcher())

    private val parameterName1 = "parameter_name_1"
    private val parameterName2 = "parameter_name_2"
    private val parameterName3 = "parameter_name_3"

    @Test
    fun `subscribe to one parameter with type String expect no values`() = testScope.runTest {
        val navParams = NavParams()

        navParams.subscribe<String>(parameterName1).test {
            expectNoEvents()
        }
    }

    @Test
    fun `subscribe to one parameter with type String until receives value`() = testScope.runTest {
        val navParams = NavParams()

        launch {
            navParams.add(parameterName1, "12345")
        }

        navParams.subscribe<String>(parameterName1).test {
            val item = awaitItem()
            expect("12345") { item.getAndConsume() }
            expectNoEvents()
        }
    }

    @Test
    fun `subscribe to one parameter with type String until receives 2 value`() = testScope.runTest {
        val navParams = NavParams()

        launch {
            navParams.add(parameterName1, "12345")
        }

        launch {
            navParams.add(parameterName1, "23456")
        }

        navParams.subscribe<String>(parameterName1).test {
            val item1 = awaitItem()
            expect("12345") { item1.value }
            expect("12345") { item1.getAndConsume() }

            val item2 = awaitItem()
            expect("23456") { item2.getAndConsume() }

            expectNoEvents()
        }
    }

    @Test
    fun `subscribe to two parameters with type String and Int until receives values`() = testScope.runTest {
        val navParams = NavParams()

        launch {
            navParams.add(parameterName1, "12345")
        }

        launch {
            navParams.add(parameterName2, 1)
        }

        navParams.subscribe<String, Int>(parameterName1, parameterName2).test {
            val item = awaitItem()
            expect("12345") { item.first.getAndConsume() }
            expect(1) { item.second.getAndConsume() }
        }
    }

    @Test
    fun `subscribe to three parameters with type String, Int and Enum until receives values`() =
        testScope.runTest {
            val navParams = NavParams()

            launch {
                navParams.add(parameterName3, TestEnum.Test3)
            }

            launch {
                navParams.add(parameterName1, "12345")
            }

            launch {
                navParams.add(parameterName2, 1)
            }

            navParams.subscribe<String, Int, TestEnum>(
                parameterName1,
                parameterName2,
                parameterName3,
            ).test {
                val item = awaitItem()
                expect("12345") { item.first.getAndConsume() }
                expect(1) { item.second.getAndConsume() }
                expect(TestEnum.Test3) { item.third.getAndConsume() }
            }
        }
}
