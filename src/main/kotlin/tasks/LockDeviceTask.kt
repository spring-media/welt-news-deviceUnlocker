package tasks

import ShellCommands.INPUT_PRESS_POWER_BUTTON
import ShellCommands.INPUT_SLEEP_CALL
import TestDeviceManagerPlugin.Companion.GROUP_NAME
import com.android.build.gradle.AppExtension
import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import details
import devicesCanBeFound
import executeShellCommandWithOutput
import getSdkVersion
import isDisplayOn
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction


open class LockDeviceTask : DefaultTask() {

    init {
        group = GROUP_NAME
        description = "lock the device"
    }

    @Input
    lateinit var android: AppExtension

    @Input
    lateinit var bridge: AndroidDebugBridge

    @TaskAction
    fun lock() {
        bridge.devicesCanBeFound()

        bridge.devices.forEach {
            deactivateDisplay(it)
        }
    }

    private fun deactivateDisplay(device: IDevice) {
        val sdkVersion = device.getSdkVersion()

        if (sdkVersion < 20) {
            if (device.isDisplayOn()) {
                device.executeShellCommandWithOutput(INPUT_PRESS_POWER_BUTTON)
            }
        } else {
            device.executeShellCommandWithOutput(INPUT_SLEEP_CALL)
        }

        println("Screen of device ${device.details()} deactivated & locked.")
    }
}