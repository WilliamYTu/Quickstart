package org.firstinspires.ftc.teamcode.subsystems

import org.firstinspires.ftc.robotcore.external.Telemetry
//import org.firstinspires.ftc.teamcode.dependencies.ActionQueue
//import org.firstinspires.ftc.teamcode.dependencies.Loggable


abstract class Base(val telemetry: Telemetry){
    abstract fun update()
//    protected lateinit var queue: ActionQueue

    /** Optional debug function */
    abstract fun log()
}
