/**
 * Created Aug 24, 2024
 */
package com.roguelogic.toys;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FractalDrawer extends Application {
  // Global variables
  private double angle;
  private double x, y;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // Setup the JavaFX application window
    Canvas canvas = new Canvas(1024, 768);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Scene scene = new Scene(new javafx.scene.Group(canvas));
    stage.setScene(scene);
    stage.setTitle("Fractal Drawer");
    stage.show();

    // Initial drawing configuration
    gc.translate(canvas.getWidth() / 2, canvas.getHeight() / 2); // Center the drawing area
    x = 0;
    y = 0;

    // int len=(int)canvas.getWidth()/3;
    int len = 150;

    // Draw the fractals
    for (int i = 1; i <= len; i++) {
      for (int j = 1; j <= 10; j++) {
        drawFractal(gc, 15 + i);
        moveToCenter(gc);
        rotateAngle(36);
      }
    }
  }

  // Sets the drawing color based on the input
  private void setColor(GraphicsContext gc, int n) {
    gc.setStroke(getColor(n));
  }

  // Maps color indices to specific RGB values
  private Color getColor(int n) {
    switch (Math.abs(n) % 16) {
      case 0:
        return Color.rgb(10, 0, 0);
      case 1:
        return Color.rgb(0, 0, 255);
      case 2:
        return Color.rgb(30, 30, 30);
      case 3:
        return Color.rgb(255, 0, 0);
      case 4:
        return Color.rgb(255, 255, 0);
      default:
        return Color.BLACK;
    }
  }

  // Draws a fractal line recursively
  private void drawFractal(GraphicsContext gc, double length) {
    if (length < 3) {
      drawLine(gc, length);
    }
    else {
      setColor(gc, 1);
      drawFractal(gc, length / 3);
      rotateAngle(-30);
      setColor(gc, 2);
      drawFractal(gc, length / 3);
      rotateAngle(60);
      setColor(gc, 3);
      drawFractal(gc, length / 3);
      rotateAngle(-30);
      setColor(gc, 4);
      drawFractal(gc, length / 3);
    }
  }

  // Draws a line from the current position in the direction of the current angle
  private void drawLine(GraphicsContext gc, double length) {
    double newX = x + length * Math.cos(angle);
    double newY = y + length * Math.sin(angle);
    gc.strokeLine(x, y, newX, newY);
    x = newX;
    y = newY;
  }

  // Moves the drawing position to the center without drawing
  private void moveToCenter(GraphicsContext gc) {
    x = 0;
    y = 0;
    gc.moveTo(0, 0);
  }

  // Rotates the current angle
  private void rotateAngle(double degrees) {
    angle += Math.toRadians(degrees);
  }

}
