/* Copyright (c) 2019 Phil Malone. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

@TeleOp (name = "Servo Bulk Reads", group = "Tests")
public class ServoBulkRead extends LinearOpMode {

    final int       TEST_CYCLES    = 500;   // Number of control cycles to run to determine cycle times.

    private Servo s1, s2;

    // Cycle Times
    double t1 = 0;
    double t2 = 0;
    double t3 = 0;

    @Override
    public void runOpMode() {

        int cycles;

        // Important Step 1:  Make sure you use DcMotorEx when you instantiate your motors.
        s1 = hardwareMap.get(Servo.class, "sHR");  // Configure the robot to use these 4 motor names,
        s2 = hardwareMap.get(Servo.class, "sHL");  // or change these strings to match your existing Robot Configuration.

        // Important Step 2: Get access to a list of Expansion Hub Modules to enable changing caching methods.
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        ElapsedTime timer = new ElapsedTime();

        telemetry.addData(">", "Press play to start tests");
        telemetry.addData(">", "Test results will update for each access method.");
        telemetry.update();
        waitForStart();

        // --------------------------------------------------------------------------------------
        // Run control loop using legacy encoder reads
        // In this mode, a single read is done for each encoder position, and a bulk read is done for each velocity read.
        // This is the worst case scenario.
        // This is the same as using LynxModule.BulkCachingMode.OFF
        // --------------------------------------------------------------------------------------

        displayCycleTimes("Test 1 of 3 (Wait for completion)");

        timer.reset();
        cycles = 0;
        while (opModeIsActive() && (cycles++ < TEST_CYCLES)) {
            s1.setPosition(1);
            s2.setPosition(1);

        }
        // calculate the average cycle time.
        t1 = timer.milliseconds() / cycles;
        s1.setPosition(0);
        s2.setPosition(0);
        displayCycleTimes("Test 2 of 3 (Wait for completion)");

        // --------------------------------------------------------------------------------------
        // Run test cycles using AUTO cache mode
        // In this mode, only one bulk read is done per cycle, UNLESS you read a specific encoder/velocity item AGAIN in that cycle.
        // --------------------------------------------------------------------------------------

        // Important Step 3: Option A. Set all Expansion hubs to use the AUTO Bulk Caching mode
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        timer.reset();
        cycles = 0;
        while (opModeIsActive() && (cycles++ < TEST_CYCLES)) {
            s1.setPosition(1);
            s2.setPosition(1);

        }
        // calculate the average cycle time.
        t2 = timer.milliseconds() / cycles;
        s1.setPosition(0);
        s2.setPosition(0);
        displayCycleTimes("Test 3 of 3 (Wait for completion)");

        // --------------------------------------------------------------------------------------
        // Run test cycles using MANUAL cache mode
        // In this mode, only one block read is done each control cycle.
        // This is the MOST efficient method, but it does require that the cache is cleared manually each control cycle.
        // --------------------------------------------------------------------------------------

        // Important Step 3: Option B. Set all Expansion hubs to use the MANUAL Bulk Caching mode
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        timer.reset();
        cycles = 0;
        while (opModeIsActive() && (cycles++ < TEST_CYCLES)) {

            // Important Step 4: If you are using MANUAL mode, you must clear the BulkCache once per control cycle
            for (LynxModule module : allHubs) {
                module.clearBulkCache();
            }

            s1.setPosition(1);
            s2.setPosition(1);

        }
        // calculate the average cycle time.
        t3 = timer.milliseconds() / cycles;
        s1.setPosition(0);
        s2.setPosition(0);
        displayCycleTimes("Complete");

        // wait until op-mode is stopped by user, before clearing display.
        while (opModeIsActive()) ;
    }

    // Display three comparison times.
    void displayCycleTimes(String status) {
        telemetry.addData("Testing", status);
        telemetry.addData("Cache = OFF",    "%5.1f mS/cycle", t1);
        telemetry.addData("Cache = AUTO",   "%5.1f mS/cycle", t2);
        telemetry.addData("Cache = MANUAL", "%5.1f mS/cycle", t3);
        telemetry.update();
    }
}

