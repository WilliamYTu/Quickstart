package org.firstinspires.ftc.teamcode.dependencies

import android.util.Log
import com.qualcomm.robotcore.util.ElapsedTime
import java.util.function.BooleanSupplier
import java.util.function.Supplier

class ActionQueue {
    private var actionQueue = arrayListOf<Pair<Runnable, Pair<Double, Supplier<Boolean>>>>()
    private var inverseActionQueue = arrayListOf<Pair<Runnable, Pair<Double, Supplier<Boolean>>>>()
    private var newActions = arrayListOf<Pair<Runnable, Pair<Double, Supplier<Boolean>>>>()
    private var newInverseActions = arrayListOf<Pair<Runnable, Pair<Double, Supplier<Boolean>>>>()

    private var continuousActions = mutableListOf<BooleanSupplier>()

    private val timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    private val trueFunc: Supplier<Boolean> = Supplier { true }

    private var internalTime = 0.0


    /**
     * Runs action when condition is true OR delay is met
     */
    fun conditionBy(condition: Supplier<Boolean>, runByMs: Number, action: Runnable) = apply {
        val currTime = internalTime
        newActions.add(Pair(action, Pair(internalTime, Supplier {
            condition.get().also {
                if (it) Log.d("Queue", "cond COND true")
            } || (internalTime > currTime + runByMs.toDouble()).also {
                if (it) Log.d("Queue", "cond TIMER true")
            }
        })))
    }

    fun add(delayMs: Number, action: Runnable) = apply {
        newActions.add(Pair(action, Pair(internalTime + delayMs.toDouble(), trueFunc)))
    }

    fun add(condition: Supplier<Boolean>, action: Runnable) = apply {
        newActions.add(Pair(action, Pair(internalTime, condition)))
    }

    private var runs = mutableListOf<() -> Boolean>()

    fun conditionBy1(condition: () -> Boolean, runByMs: Long, action: () -> Unit) {
        val startTime = System.currentTimeMillis();
        runs.add {
            if (condition() || System.currentTimeMillis() - startTime >= runByMs) {
                action()
                false
            } else {
                true
            }
        }
    }


    /**
     * Runs the lambda every cycle until it returns true.
     */
    fun addContinuous(lambda: BooleanSupplier) {
        continuousActions += lambda
    }

    fun clear() {
        newActions.clear()
        newInverseActions.clear()
        actionQueue.clear()
        inverseActionQueue.clear()
        continuousActions.clear()
    }

    fun resetTimer() = timer.reset()

    /** You should use the one with no params instead */
    fun update(time: Double) {
        runs = runs.filter { it() }.toMutableList()

        internalTime = time
        actionQueue.addAll(newActions)
        newActions.clear()
        val actionIterator = actionQueue.listIterator()
        while (actionIterator.hasNext()) {
            val a = actionIterator.next()
            if (a.second.first < time && a.second.second.get()) {
                a.first.run()
                actionIterator.remove()
            }
        }

        inverseActionQueue.addAll(newInverseActions)
        newInverseActions.clear()
        val inverseActionIterator = inverseActionQueue.listIterator()
        while (inverseActionIterator.hasNext()) {
            val a = inverseActionIterator.next()
            if (a.second.first < time || a.second.second.get()) {
                a.first.run()
                inverseActionIterator.remove()
            }
        }

        continuousActions.removeAll { it.asBoolean }
    }

    fun update() = update(timer.milliseconds())

    fun getActionQueue() = actionQueue
}
