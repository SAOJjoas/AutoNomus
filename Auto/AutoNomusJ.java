package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
@Autonomous (name = "AutoNomusJ")
public class AutoNomusJ extends LinearOpMode {
    private int estado = 0;

    public void mudarEstado(int estado){
        this.estado = estado;
    }
    private DcMotorEx lancador;
    private Servo alavanca;
    private Follower follower;
    private final Pose inicio = new Pose(0, 0, 0),
            antesLancaBola = new Pose(0,30,0);

    private PathChain andar;
    @Override
    public void runOpMode() {
        lancador = hardwareMap.get(DcMotorEx.class, "lancador");
        alavanca = hardwareMap.get(Servo.class, "alavanca");

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(inicio);

        andar = follower.pathBuilder()
                .addPath(new BezierLine(inicio, antesLancaBola))
                .setLinearHeadingInterpolation(inicio.getHeading(), antesLancaBola.getHeading())
                .build();

        waitForStart();
        while (opModeIsActive()){
            follower.update();
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.update();

            switch (estado){
                case 0:
                    follower.followPath(andar);
                    mudarEstado(1);
                    break;
                case 1:
                    if(!follower.isBusy()){
                        for(int i = 0; i < 3; i++){
                            while(lancador.getVelocity() < 1500) {
                                lancador.setPower(.65);
                            }
                            alavanca.setPosition(.3);
                            sleep(750);
                            alavanca.setPosition(0);
                            sleep(750);
                        }
                        mudarEstado(2);
                    }
                    break;
            }
        }
    }
}
