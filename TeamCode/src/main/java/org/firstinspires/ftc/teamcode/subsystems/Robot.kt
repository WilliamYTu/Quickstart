package org.firstinspires.ftc.teamcode.subsystems
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.dependencies.ActionQueue


class Robot(
    val hardwareMap: HardwareMap,
    val telemetry: Telemetry,
    val teleop: Boolean = false,
    saveTeleop: Boolean = false,
    specAuto: Boolean = false
) {
    constructor(
        linearOpmode: LinearOpMode,
        teleop: Boolean = false,
        saveTeleop: Boolean = false,
        specAuto: Boolean = false
    ) : this(
        linearOpmode.hardwareMap,
        MultipleTelemetry(linearOpmode.telemetry, FtcDashboard.getInstance().telemetry),
        teleop,
        saveTeleop,
        specAuto
    )

    val queue = ActionQueue()

//    val intake = Intake(hardwareMap, telemetry)
//    val extendo = Extendo(hardwareMap, telemetry)
//    val drive = Drive(hardwareMap, telemetry)
//    val claw = Claw(hardwareMap, telemetry)
//    val arm = Arm(hardwareMap, telemetry).apply { tieQueue(queue) }
//    val slides = Slides(hardwareMap, telemetry)
//    val leds = LEDs(hardwareMap, telemetry)
//    val sweep = Sweep(hardwareMap, telemetry)
//    val batteryMeter = BatteryMeter(telemetry, hardwareMap).apply(BatteryMeter::disable)

//    private val subsystems = listOf(drive, claw, slides, extendo, intake, arm, batteryMeter, sweep)

//    private val hubs = hardwareMap.getAll(LynxModule::class.java)
    init {

    }

    fun log() {
        telemetry.addData("goon", "sdf")
//        subsystems.forEach(Base::log)
    }


    fun update() {
//        hubs.forEach { it.clearBulkCache() }
        queue.update()
//        subsystems.forEach(Base::update)
        //log()
        telemetry.update()
    }
}
